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
import restaurant.ms.core.dto.responses.*;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.*;
import restaurant.ms.core.security.AES;
import restaurant.ms.core.utils.Utility;

import javax.transaction.Transactional;
import java.time.LocalDate;
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

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AES aes;

    public PageRs searchQr(RestaurantUser restaurantUser,Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Qr> qrPage = qrRepo.findAllByRestaurant(restaurantUser.getRestaurant(),pageable);

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

        qr.setCode(qrUrl.replace("QR_ID", aes.encrypt(qr.getId() + "")));

        qrRepo.save(qr);
    }

    public void updateQr(QrUpdateRq qrUpdateRq, RestaurantUser restaurantUser, Locale locale) {

        Qr currentQr = qrRepo.findQrById(qrUpdateRq.getQrId());

        Qr qr = new Qr();
        qr.setId(qrUpdateRq.getQrId());
        qr.setAlias(qrUpdateRq.getAlias());
        qr.setCreateDate(currentQr.getCreateDate());
        qr.setRestaurant(restaurantUser.getRestaurant());
        qr.setStatus(currentQr.getStatus());
        qr.setCode(currentQr.getCode());

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


    public QrInfoRs qrInfo(String qrIdEncrypted, Locale locale) {

        String decryptedQrId = (aes.decrypt(qrIdEncrypted));

        Long qrId = Utility.parseLong(decryptedQrId);

        if(qrId == null){
            qrId = Utility.parseLong(qrIdEncrypted);
        }

        Qr qr = qrRepo.findQrById(qrId);

        if(qr == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(qr.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        QrInfoRs qrInfoRs = new QrInfoRs();
        qrInfoRs.setId(qrId);
        qrInfoRs.setBrandName(qr.getRestaurant().getBrandNameEn());
        qrInfoRs.setLogo(qr.getRestaurant().getLogo());
        qrInfoRs.setCategoryList(categoryInfo(qr,locale));

        List<Order> orderList = orderRepo.findCurrentRunningOrder(qr);

        if(orderList != null && !orderList.isEmpty()){

            List<OrderSearchRs> orderSearchRsList = orderList.stream()
                    .map(order -> order.toOrderSearchRs())
                    .collect(Collectors.toList());

            qrInfoRs.setOrderList(orderSearchRsList);
        }

        return qrInfoRs;
    }

    public List<CategoryInfoRs> categoryInfo(Qr qr, Locale locale){

        List<Category> categoryList = categoryRepo.findCategoryByRestaurant_idAndStatus(qr.getRestaurant().getId(), Status.ACTIVE);

        if(categoryList == null){
            return null;
        }

        List<CategoryInfoRs> categoryInfoRsList = categoryList.stream()
                .map(category -> category.toCategoryInfoRs())
                .collect(Collectors.toList());

        for(CategoryInfoRs categoryInfoRs: categoryInfoRsList){

            List<Item> itemList = itemRepo.findItemByCategory_IdAndStatus(categoryInfoRs.getId(),Status.ACTIVE);

            if(itemList == null){
                continue;
            }

            List<ItemInfoRs> itemInfoRsList = itemList.stream()
                    .map(item -> item.toItemInfoRs())
                    .collect(Collectors.toList());

            categoryInfoRs.setItemList(itemInfoRsList);
        }

        return categoryInfoRsList;
    }

}
