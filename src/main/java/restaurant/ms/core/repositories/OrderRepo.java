package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Order;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;

import java.util.List;

public interface OrderRepo extends CrudRepository<Order,String> {

    public List<Order> findAll();

    @Query("select ord from Order ord " +
            "where ord.restaurant=:restaurant")
    Page<Order> findAllByRest(Restaurant restaurant,Pageable pageable);

    @Query("select ord from Order ord " +
            "where ord.qr=:qr")
    Page<Order> findAllByQr(Qr qr, Pageable pageable);

    public Order findOrderById(Long orderId);
}
