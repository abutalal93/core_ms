package restaurant.ms.core.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.RestaurantCreateRq;
import restaurant.ms.core.dto.requests.RestaurantUpdateRq;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.RestaurantSearchRs;
import restaurant.ms.core.dto.responses.SpLoginRs;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.RestaurantRepo;
import restaurant.ms.core.repositories.SpUserRepo;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;
import restaurant.ms.core.utils.BeanConverter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantService {

    @Autowired
    private RestaurantRepo restaurantRepo;

    public PageRs searchRestaurant(Integer page,Integer size, Locale locale){
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Restaurant> restaurantPage = restaurantRepo.findAllBy(pageable);

        List<Restaurant> restaurantList = restaurantPage.getContent();

        if(restaurantList == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"no_data_found",locale);
        }

        List<RestaurantSearchRs> restaurantSearchRsList = restaurantList.stream()
                .map(restaurant -> restaurant.toRestaurantSearchRs())
                .collect(Collectors.toList());

        return new PageRs(restaurantPage.getTotalElements(), restaurantPage.getTotalPages(), restaurantSearchRsList);
    }

    public void createRestaurant(RestaurantCreateRq restaurantCreateRq,SpUser spUser, Locale locale){

        Restaurant restaurant = restaurantCreateRq.toRestaurant();
        restaurant.setCreateDate(LocalDateTime.now());
        restaurant.setExpireDate(LocalDateTime.now().plusMonths(6));
        restaurant.setSpUser(spUser);

        restaurantRepo.save(restaurant);
    }

    public void updateRestaurant(RestaurantUpdateRq restaurantUpdateRq, SpUser spUser, Locale locale){

        Restaurant currentRestaurant = restaurantRepo.findRestaurantById(restaurantUpdateRq.getId());

        if(currentRestaurant == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"restaurant_not_found",locale);
        }

        Restaurant restaurant = restaurantUpdateRq.toRestaurant();
//        restaurant.setCreateDate(LocalDateTime.now());
//        restaurant.setExpireDate(LocalDateTime.now().plusMonths(6));
//        restaurant.setSpUser(spUser);

        restaurantRepo.save(restaurant);
    }

}
