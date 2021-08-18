package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Category;

import java.util.List;

public interface CategoryRepo extends CrudRepository<Category,String> {

    public List<Category> findAll();
}
