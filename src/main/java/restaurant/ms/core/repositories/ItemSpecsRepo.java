package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.ItemSpecs;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface ItemSpecsRepo extends CrudRepository<ItemSpecs,String> {

    public List<ItemSpecs> findAll();

    public ItemSpecs findItemSpecsById(Long id);

    @Query("select item from ItemSpecs item where item.status <>'DELETED' and item.restaurant=:restaurant")
    List<ItemSpecs> findAllBy(Restaurant restaurant);
}
