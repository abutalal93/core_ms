package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.Status;

import java.util.List;

public interface ItemRepo extends CrudRepository<Item,String> {

    public List<Item> findAll();

    @Query("select item from Item item where item.status <>'DELETED' and item.restaurant=:restaurant")
    Page<Item> findAllBy(Restaurant restaurant,Pageable pageable);


    public List<Item> findItemByCategory_IdAndStatus(Long categoryId, Status status);

    public Item findItemById(Long itemId);
}
