package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.Restaurant;

import java.util.List;

public interface RestaurantRepo extends CrudRepository<Restaurant,String> {

    public List<Restaurant> findAll();

    public Restaurant findRestaurantById(Long id);

    @Query("select rest from Restaurant rest where rest.status <> 'DELETED'")
    public Page<Restaurant> findAllBy(Pageable pageable);

    @Query("select rest from Restaurant rest where rest.status <> 'DELETED' and rest.commercialRegister=:commercialRegister")
    public Restaurant findRestaurant(@Param("commercialRegister") String commercialRegister);
}
