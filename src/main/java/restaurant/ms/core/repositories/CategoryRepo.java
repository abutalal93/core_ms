package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.enums.Status;

import java.util.List;

public interface CategoryRepo extends CrudRepository<Category,String> {

    public List<Category> findAll();

    public List<Category> findCategoryByRestaurant_idAndStatus(Long restId, Status status);

    @Query("select category from Category category where category.status <>'DELETED'")
    Page<Category> findAllBy(Pageable pageable);


    public Category findCategoryById(Long categoryId);
}
