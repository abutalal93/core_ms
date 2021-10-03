package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.DiscountCreateRq;
import restaurant.ms.core.dto.requests.DiscountUpdateRq;
import restaurant.ms.core.dto.responses.DiscountSearchRs;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.DiscountType;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.DiscountItemRepo;
import restaurant.ms.core.repositories.DiscountRepo;
import restaurant.ms.core.repositories.RestaurantRepo;
import restaurant.ms.core.utils.Utility;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiscountService {

    @Autowired
    private DiscountRepo discountRepo;

    @Autowired
    private DiscountItemRepo discountItemRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

    public PageRs searchDiscount(RestaurantUser restaurantUser,Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        if(restaurantUser.getRestaurantUserType().equals(RestaurantUserType.WAITRESS)){
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED,"user_not_allowed",locale);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Discount> discountPage = discountRepo.findAllBy(restaurantUser.getRestaurant(),pageable);

        List<Discount> discountList = discountPage.getContent();

        if (discountList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<DiscountSearchRs> discountSearchRsList = discountList.stream()
                .map(discount -> discount.toDiscountSearchRs())
                .collect(Collectors.toList());

        return new PageRs(discountPage.getTotalElements(), discountPage.getTotalPages(), discountSearchRsList);
    }

    public void createDiscount(DiscountCreateRq discountCreateRq, RestaurantUser restaurantUser, Locale locale) {
        Discount discount = new Discount();
        discount.setName(discountCreateRq.getName());
        discount.setDiscountType(DiscountType.getValue(discountCreateRq.getDiscountType()));
        discount.setDiscountValue(discountCreateRq.getDiscountValue());
        discount.setRestaurant(restaurantUser.getRestaurant());
        discount.setRestaurantUser(restaurantUser);
        discount.setStatus(Status.ACTIVE);
        discount.setStartDateTime(Utility.parseDateTimeFromString(discountCreateRq.getStartDateTime(),"yyyy-MM-dd'T'HH:mm"));
        discount.setEndDateTime(Utility.parseDateTimeFromString(discountCreateRq.getEndDateTime(),"yyyy-MM-dd'T'HH:mm"));

        discountRepo.save(discount);

        if(discountCreateRq.getItemIdList() != null && !discountCreateRq.getItemIdList().isEmpty()){
            List<DiscountItem> discountItemList = new ArrayList<>();
            for(Long itemId: discountCreateRq.getItemIdList()){
                Item item = new Item();
                item.setId(itemId);

                DiscountItem discountItem = new DiscountItem();
                discountItem.setDiscount(discount);
                discountItem.setItem(item);
                discountItemList.add(discountItem);
            }
            discountItemRepo.saveAll(discountItemList);
        }
    }

    public void updateDiscount(DiscountUpdateRq discountUpdateRq, RestaurantUser restaurantUser, Locale locale) {

        Discount currentDiscount = discountRepo.findDiscountById(discountUpdateRq.getDiscountId());

        if(currentDiscount == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"Discount not found",locale);
        }

        Discount discount = new Discount();
        discount.setId(discountUpdateRq.getDiscountId());
        discount.setName(discountUpdateRq.getName());
        discount.setDiscountType(DiscountType.getValue(discountUpdateRq.getDiscountType()));
        discount.setDiscountValue(discountUpdateRq.getDiscountValue());
        discount.setRestaurant(restaurantUser.getRestaurant());
        discount.setRestaurantUser(restaurantUser);
        discount.setStatus(currentDiscount.getStatus());
        discount.setStartDateTime(Utility.parseDateTimeFromString(discountUpdateRq.getStartDateTime(),"yyyy-MM-dd'T'HH:mm"));
        discount.setEndDateTime(Utility.parseDateTimeFromString(discountUpdateRq.getEndDateTime(),"yyyy-MM-dd'T'HH:mm"));

        discountRepo.save(discount);

        if(discountUpdateRq.getItemIdList() != null && !discountUpdateRq.getItemIdList().isEmpty()){

            discountItemRepo.deleteDiscountItemByDiscount(currentDiscount);

            List<DiscountItem> discountItemList = new ArrayList<>();
            for(Long itemId: discountUpdateRq.getItemIdList()){
                Item item = new Item();
                item.setId(itemId);

                DiscountItem discountItem = new DiscountItem();
                discountItem.setDiscount(discount);
                discountItem.setItem(item);
                discountItemList.add(discountItem);
            }

            discountItemRepo.saveAll(discountItemList);
        }
    }


    public void activeOrInactiveDiscount(Long discountId, RestaurantUser restaurantUser, Locale locale) {

        Discount discount = discountRepo.findDiscountById(discountId);

        if(discount == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(discount.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(discount.getStatus().equals(Status.INACTIVE)){
            discount.setStatus(Status.ACTIVE);
        }else{
            if(discount.getStatus().equals(Status.ACTIVE)){
                discount.setStatus(Status.INACTIVE);
            }
        }

        discountRepo.save(discount);
    }

    public void deleteDiscount(Long discountId, RestaurantUser restaurantUser, Locale locale) {

        Discount discount = discountRepo.findDiscountById(discountId);

        if(discount == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(discount.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        discount.setStatus(Status.DELETED);

        discountRepo.save(discount);
    }

}
