package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Discount;
import restaurant.ms.core.entities.DiscountItem;
import restaurant.ms.core.entities.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountItemRepo extends CrudRepository<DiscountItem,String> {
    public List<DiscountItem> findAll();

    public void deleteDiscountItemByDiscount(Discount discount);

    @Query("select disItm from DiscountItem disItm " +
            "where disItm.discount.startDateTime <= :dateTime " +
            "and disItm.discount.endDateTime >= :dateTime " +
            "and disItm.discount.status = 'ACTIVE' " +
            "and disItm.item.id = :itemId")
    public List<DiscountItem> findDiscountItem(@Param("dateTime") LocalDateTime dateTime,
                                               @Param("itemId") Long itemId);
}
