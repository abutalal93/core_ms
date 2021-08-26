package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.Restaurant;

import java.util.List;

public interface RestaurantRepo extends CrudRepository<Restaurant,String> {

    public List<Restaurant> findAll();

    public Restaurant findRestaurantById(Long id);

    @Query("select rest from Restaurant rest where rest.status <> 'DELETED'")
    Page<Restaurant> findAllBy(Pageable pageable);

}
