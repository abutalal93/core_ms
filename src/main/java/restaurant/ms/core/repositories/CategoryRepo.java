package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface CategoryRepo extends CrudRepository<Category,String> {

    public List<Category> findAll();

    public List<Category> findCategoryByRestaurant_idAndStatus(Long restId, Status status);

    @Query("select category from Category category where category.status <>'DELETED' and category.restaurant=:restaurant")
    Page<Category> findAllBy(@Param("restaurant") Restaurant restaurant, Pageable pageable);

    @Query("select category from Category category " +
            "where category.status <>'DELETED' " +
            "and category.restaurant=:restaurant " +
            "and category.nameEn=:nameEn")
    List<Category> findCategoryNameEn(@Param("restaurant") Restaurant restaurant,@Param("nameEn") String nameEn);

    @Query("select category from Category category " +
            "where category.status <>'DELETED' " +
            "and category.restaurant=:restaurant " +
            "and category.nameAr=:nameAr")
    List<Category> findCategoryNameAr(@Param("restaurant") Restaurant restaurant,@Param("nameAr") String nameAr);


    public Category findCategoryById(Long categoryId);


    @Modifying
    @Query("update Category cat " +
            "set cat.status='INACTIVE' " +
            "where (cat.deactivationDate is not null and cat.deactivationDate <=:deactivateDate)")
    public void updateDeactivatedCategory(@Param("deactivateDate") LocalDate deactivateDate);
}
