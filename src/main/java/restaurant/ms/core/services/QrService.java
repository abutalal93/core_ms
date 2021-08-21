package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.QrCreateRq;
import restaurant.ms.core.dto.requests.QrUpdateRq;
import restaurant.ms.core.dto.requests.RestaurantCreateRq;
import restaurant.ms.core.dto.requests.RestaurantUpdateRq;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.QrSearchRs;
import restaurant.ms.core.dto.responses.RestaurantSearchRs;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.QrRepo;
import restaurant.ms.core.repositories.RestaurantRepo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class QrService {

    @Autowired
    private QrRepo qrRepo;

    @Value("${qrUrl}")
    private String qrUrl;

    public PageRs searchQr(Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Qr> qrPage = qrRepo.findAllBy(pageable);

        List<Qr> qrList = qrPage.getContent();

        if (qrList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<QrSearchRs> qrSearchRsList = qrList.stream()
                .map(qr -> qr.toQrSearchRs())
                .collect(Collectors.toList());

        return new PageRs(qrPage.getTotalElements(), qrPage.getTotalPages(), qrSearchRsList);
    }

    public void createQr(QrCreateRq qrCreateRq, RestaurantUser restaurantUser, Locale locale) {

        Qr qr = new Qr();
        qr.setCreateDate(LocalDateTime.now());
        qr.setRestaurant(restaurantUser.getRestaurant());
        qr.setStatus(Status.ACTIVE);
        qr.setAlias(qrCreateRq.getAlias());

        qrRepo.save(qr);

        qr.setCode(qrUrl.replace("QR_ID", qr.getId() + ""));

        qrRepo.save(qr);
    }

    public void updateQr(QrUpdateRq qrUpdateRq, RestaurantUser restaurantUser, Locale locale) {

        Qr qr = new Qr();
        qr.setId(qrUpdateRq.getQrId());
        qr.setAlias(qrUpdateRq.getAlias());

        qrRepo.save(qr);
    }


    public void activeOrInactiveQr(Long qrId, RestaurantUser restaurantUser, Locale locale) {

        Qr qr = qrRepo.findQrById(qrId);

        if(qr == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(qr.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(qr.getStatus().equals(Status.INACTIVE)){
            qr.setStatus(Status.ACTIVE);
        }else{
            if(qr.getStatus().equals(Status.ACTIVE)){
                qr.setStatus(Status.INACTIVE);
            }
        }

        qrRepo.save(qr);
    }

    public void deleteQr(Long qrId, RestaurantUser restaurantUser, Locale locale) {

        Qr qr = qrRepo.findQrById(qrId);

        if(qr == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(qr.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        qr.setStatus(Status.DELETED);

        qrRepo.save(qr);
    }

}
