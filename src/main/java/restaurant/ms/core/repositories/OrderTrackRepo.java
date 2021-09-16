package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.OrderStatus;

import java.util.List;

public interface OrderTrackRepo extends CrudRepository<OrderTrack,String> {

    @Query("select itm from OrderTrack itm where itm.order=:order order by itm.id asc")
    public List<OrderTrack> findOrderTrackByOrder(Order order);

    @Query("select itm from OrderTrack itm where itm.order=:order and itm.status=:status order by itm.id asc")
    public List<OrderTrack> findOrderTrackByOrderAndStatus(Order order, OrderStatus status);
}
