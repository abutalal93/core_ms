package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Order;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends CrudRepository<Order,String> {

    public List<Order> findAll();

    @Query("select ord from Order ord " +
            "where ord.restaurant=:restaurant " +
            "and (ord.status ='INIT' or ord.status ='APPROVED' or ord.status ='PAID' or ord.status ='DELIVERED' )")
    Page<Order> findCurrentRunningOrderByRest(Restaurant restaurant,Pageable pageable);

    @Query("select ord from Order ord " +
            "where ord.qr=:qr")
    Page<Order> findAllByQr(Qr qr, Pageable pageable);

    @Query("select ord from Order ord " +
            "where ord.qr=:qr " +
            "and (ord.status ='INIT' or ord.status ='APPROVED' or ord.status ='PAID' or ord.status ='DELIVERED' )")
    List<Order> findCurrentRunningOrder(Qr qr);

    public Order findOrderById(Long orderId);

    @Query("select ord from Order ord " +
            "where ord.restaurant=:restaurant " +
            "and (:reference is null or ord.reference=:reference) " +
            "and (cast(:fromDate as date) is null or ord.createDate>=:fromDate) " +
            "and (cast(:toDate as date) is null or ord.createDate<=:toDate) " +
            "and (ord.status = 'CLOSED' or ord.status = 'CANCELED')")
    Page<Order> searchOrder(@Param("restaurant")Restaurant restaurant,
                            @Param("reference") String reference,
                            @Param("fromDate")LocalDateTime fromDate,
                            @Param("toDate")LocalDateTime toDate,
                            Pageable pageable);

}
