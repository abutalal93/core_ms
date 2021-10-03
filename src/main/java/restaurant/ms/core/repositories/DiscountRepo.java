package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Discount;
import restaurant.ms.core.entities.Item;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepo extends CrudRepository<Discount,String> {
    public List<Discount> findAll();

    public Discount findDiscountById(Long id);

    @Query("select discount from Discount discount where discount.status <>'DELETED' and discount.restaurant=:restaurant")
    Page<Discount> findAllBy(Restaurant restaurant, Pageable pageable);
}
