package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.entities.RestaurantUser;

import java.util.List;

public interface RestaurantUserRepo extends CrudRepository<RestaurantUser,String> {

    public List<RestaurantUser> findAll();

    @Query("select restaurantUser from RestaurantUser restaurantUser " +
            "where restaurantUser.username=:username " +
            "and restaurantUser.status <>'DELETED'")
    public RestaurantUser findRestaurantUserByUsername(@Param("username") String username);

    @Query("select restaurantUser from RestaurantUser restaurantUser " +
            "where restaurantUser.status <>'DELETED' " +
            "and restaurantUser.restaurant=:restaurant")
    Page<RestaurantUser> findAllBy(@Param("restaurant") Restaurant restaurant, Pageable pageable);

    public RestaurantUser findRestaurantUserById(Long userId);
}
