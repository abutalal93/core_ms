package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Item;

import java.util.List;

public interface ItemRepo extends CrudRepository<Item,String> {

    public List<Item> findAll();
}
