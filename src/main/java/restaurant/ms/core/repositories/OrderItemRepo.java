package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Order;
import restaurant.ms.core.entities.OrderItem;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;

import java.util.List;

public interface OrderItemRepo extends CrudRepository<OrderItem,String> {

}
