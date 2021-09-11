package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.CategoryCreateRq;
import restaurant.ms.core.dto.requests.OrderSubmitRq;
import restaurant.ms.core.dto.responses.ItemInfoRs;
import restaurant.ms.core.dto.responses.OrderSearchRs;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.OrderStatus;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.*;
import restaurant.ms.core.utils.Utility;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private QrRepo qrRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private OrderTrackRepo orderTrackRepo;

    public PageRs searchOrder(RestaurantUser restaurantUser,Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Order> orderPage = orderRepo.findAllBy(restaurantUser.getRestaurant(),pageable);

        List<Order> orderList = orderPage.getContent();

        if (orderList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<OrderSearchRs> orderSearchRsList = orderList.stream()
                .map(order -> order.toOrderSearchRs())
                .collect(Collectors.toList());

        return new PageRs(orderPage.getTotalElements(), orderPage.getTotalPages(), orderSearchRsList);
    }


    public void createOrder(OrderSubmitRq orderSubmitRq, Locale locale) {

        Order order = new Order();
        order.setCreateDate(LocalDateTime.now());
        order.setReference(System.currentTimeMillis()+"");
        order.setCustomerName(orderSubmitRq.getName());
        order.setEmail(orderSubmitRq.getEmail());
        order.setMobileNumber(orderSubmitRq.getMobile());
        order.setTotalAmount(orderSubmitRq.getTotalAmount());
        order.setStatus(OrderStatus.INIT);
        order.setQr(qrRepo.findQrById(orderSubmitRq.getQrId()));
        order.setRestaurant(order.getQr() != null ? order.getQr().getRestaurant() : null);

        orderRepo.save(order);

        for(ItemInfoRs itemInfoRs : orderSubmitRq.getCartList()){

            Item item = itemRepo.findItemById(itemInfoRs.getId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(itemInfoRs.getQuantity());
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItem.setLineTotal(orderItem.getQuantity().multiply(orderItem.getUnitPrice()));

            orderItemRepo.save(orderItem);
        }

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setCreateDate(LocalDateTime.now());
        orderTrack.setUserReference("------");
        orderTrack.setStatus(OrderStatus.INIT);

        orderTrackRepo.save(orderTrack);
    }

}
