package restaurant.ms.core.services;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.ItemCreateRq;
import restaurant.ms.core.dto.requests.ItemUpdateRq;
import restaurant.ms.core.dto.responses.ItemSearchRs;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.ItemRepo;
import restaurant.ms.core.repositories.RestaurantRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

    public PageRs searchItem(RestaurantUser restaurantUser,Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Item> itemPage = itemRepo.findAllBy(restaurantUser.getRestaurant(),pageable);

        List<Item> itemList = itemPage.getContent();

        if (itemList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<ItemSearchRs> itemSearchRsList = itemList.stream()
                .map(item -> item.toItemSearchRs())
                .collect(Collectors.toList());

        return new PageRs(itemPage.getTotalElements(), itemPage.getTotalPages(), itemSearchRsList);
    }

    public void createItem(ItemCreateRq itemCreateRq, RestaurantUser restaurantUser, Locale locale) {

        Long currentItemSequence = restaurantUser.getRestaurant().getItemSequence();

        if(currentItemSequence == null){
            currentItemSequence = 0L;
        }

        currentItemSequence += 1;

        Item item = new Item();
        item.setCode(currentItemSequence+"");
        item.setNameAr(itemCreateRq.getNameAr());
        item.setNameEn(itemCreateRq.getNameEn());
        item.setAvatar(itemCreateRq.getAvatar());
        item.setRestaurant(restaurantUser.getRestaurant());
        item.setStatus(Status.ACTIVE);
        item.setUnitPrice(itemCreateRq.getUnitPrice());
        item.setCategory(new Category(itemCreateRq.getCategoryId()));
        item.setDescription(itemCreateRq.getDescription());


        itemRepo.save(item);


        restaurantUser.getRestaurant().setItemSequence(currentItemSequence);
        restaurantRepo.save(restaurantUser.getRestaurant());
    }

    public void updateItem(ItemUpdateRq itemUpdateRq, RestaurantUser restaurantUser, Locale locale) {

        Item currentItem = itemRepo.findItemById(itemUpdateRq.getItemId());

        Item item = new Item();
        item.setId(itemUpdateRq.getItemId());
        item.setCode(currentItem.getCode());
        item.setNameAr(itemUpdateRq.getNameAr());
        item.setNameEn(itemUpdateRq.getNameEn());
        item.setAvatar(itemUpdateRq.getAvatar());
        item.setRestaurant(restaurantUser.getRestaurant());
        item.setStatus(currentItem.getStatus());
        item.setUnitPrice(itemUpdateRq.getUnitPrice());
        item.setCategory(new Category(itemUpdateRq.getCategoryId()));
        item.setDescription(itemUpdateRq.getDescription());
        itemRepo.save(item);
    }


    public void activeOrInactiveItem(Long itemId, RestaurantUser restaurantUser, Locale locale) {

        Item item = itemRepo.findItemById(itemId);

        if(item == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(item.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(item.getStatus().equals(Status.INACTIVE)){
            item.setStatus(Status.ACTIVE);
        }else{
            if(item.getStatus().equals(Status.ACTIVE)){
                item.setStatus(Status.INACTIVE);
            }
        }

        itemRepo.save(item);
    }

    public void deleteItem(Long itemId, RestaurantUser restaurantUser, Locale locale) {

        Item item = itemRepo.findItemById(itemId);

        if(item == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(item.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        item.setStatus(Status.DELETED);

        itemRepo.save(item);
    }

}
