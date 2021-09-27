package restaurant.ms.core.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.ItemDetail;
import restaurant.ms.core.entities.ItemSpecs;
import restaurant.ms.core.entities.Restaurant;

import java.util.List;

public interface ItemDetailRepo extends CrudRepository<ItemDetail,String> {

    public List<ItemDetail> findAll();

    public void deleteAllByItemSpecs(ItemSpecs itemSpecs);
}
