package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface ItemRepo extends CrudRepository<Item,String> {

    public List<Item> findAll();

    @Query("select item from Item item where item.status <>'DELETED' and item.restaurant=:restaurant")
    Page<Item> findAllBy(Restaurant restaurant,Pageable pageable);

    @Query("select item from Item item " +
            "where item.status <>'DELETED' " +
            "and item.restaurant=:restaurant " +
            "and (:code is null or item.code=:code) " +
            "and (:categoryId is null or item.category.id=:categoryId) " +
            "and (:nameEn is null or lower(item.nameEn) like %:nameEn%) " +
            "and (:nameAr is null or lower(item.nameAr) like %:nameAr%) ")
    Page<Item> findAllBy(@Param("restaurant") Restaurant restaurant,
                             @Param("code") String code,
                             @Param("nameEn") String nameEn,
                             @Param("nameAr") String nameAr,
                             @Param("categoryId") Long categoryId,
                             Pageable pageable);


    public List<Item> findItemByCategory_IdAndStatus(Long categoryId, Status status);

    public Item findItemById(Long itemId);

    @Modifying
    @Query("update Item itm " +
            "set itm.status='INACTIVE' " +
            "where (itm.deactivationDate is not null and itm.deactivationDate <=:deactivateDate)")
    public void updateDeactivatedItem(@Param("deactivateDate") LocalDate deactivateDate);
}
