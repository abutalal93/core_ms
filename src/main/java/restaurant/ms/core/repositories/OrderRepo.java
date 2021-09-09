package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Order;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.Status;

import java.util.List;

public interface OrderRepo extends CrudRepository<Order,String> {

    public List<Order> findAll();

    @Query("select order from Order order where order.restaurant=:restaurant")
    Page<Order> findAllBy(Restaurant restaurant,Pageable pageable);

    @Query("select order from Order order where order.qr=:qr")
    Page<Order> findAllBy(Qr qr, Pageable pageable);

    public Order findOrderById(Long orderId);
}
