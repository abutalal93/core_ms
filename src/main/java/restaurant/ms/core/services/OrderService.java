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
import restaurant.ms.core.dto.responses.OrderInfoRs;
import restaurant.ms.core.dto.responses.OrderSearchRs;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.OrderStatus;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.*;
import restaurant.ms.core.security.AES;
import restaurant.ms.core.utils.Utility;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
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

    @Autowired
    private AES aes;

    public PageRs searchOrder(RestaurantUser restaurantUser,Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Order> orderPage = orderRepo.findCurrentRunningOrderByRest(restaurantUser.getRestaurant(),pageable);

        List<Order> orderList = orderPage.getContent();

        if (orderList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<OrderSearchRs> orderSearchRsList = orderList.stream()
                .map(order -> order.toOrderSearchRs())
                .collect(Collectors.toList());

        return new PageRs(orderPage.getTotalElements(), orderPage.getTotalPages(), orderSearchRsList);
    }

    public PageRs searchOrder(RestaurantUser restaurantUser,String reference, String dateFromStr, String dateToStr, Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        LocalDate fromDate = Utility.parseDateFromString(dateFromStr,"yyyy-MM-dd");
        if(fromDate == null){
            fromDate = LocalDate.of(2000,1,1);
        }

        LocalDate toDate = Utility.parseDateFromString(dateToStr,"yyyy-MM-dd");
        if(toDate == null){
            toDate = LocalDate.of(2030,1,1);
        }

        if(reference != null && reference.isEmpty()){
            reference = null;
        }

        Page<Order> orderPage = orderRepo.searchOrder(restaurantUser.getRestaurant(),reference,fromDate.atStartOfDay(),toDate.atStartOfDay(),pageable);

        List<Order> orderList = orderPage.getContent();

        if (orderList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<OrderSearchRs> orderSearchRsList = orderList.stream()
                .map(order -> order.toOrderSearchRs())
                .collect(Collectors.toList());

        return new PageRs(orderPage.getTotalElements(), orderPage.getTotalPages(), orderSearchRsList);
    }


    public String createOrder(OrderSubmitRq orderSubmitRq, Locale locale) {

        String qrIdDecrypted = aes.decrypt(orderSubmitRq.getQrId());

        Long qrId = Utility.parseLong(qrIdDecrypted);

        if(qrIdDecrypted == null){
            qrId = Utility.parseLong(orderSubmitRq.getQrId());
        }

        Order order = new Order();
        order.setCreateDate(LocalDateTime.now());
        order.setReference(System.currentTimeMillis()+"");
        order.setCustomerName(orderSubmitRq.getName());
        order.setEmail(orderSubmitRq.getEmail());
        order.setMobileNumber(orderSubmitRq.getMobile());
        order.setTotalAmount(orderSubmitRq.getTotalAmount());
        order.setStatus(OrderStatus.INIT);
        order.setQr((qrRepo.findQrById(qrId)));
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

        return order.getReference();
    }

    public void approveOrder(Long orderId, RestaurantUser restaurantUser, Locale locale) {

        Order order = orderRepo.findOrderById(orderId);
        order.setStatus(OrderStatus.APPROVED);
        orderRepo.save(order);

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setCreateDate(LocalDateTime.now());
        orderTrack.setUserReference(restaurantUser.getUsername());
        orderTrack.setStatus(OrderStatus.APPROVED);

        orderTrackRepo.save(orderTrack);
    }

    public void payOrder(Long orderId, RestaurantUser restaurantUser, Locale locale) {

        OrderStatus orderStatus = null;

        if(restaurantUser.getRestaurantUserType().equals(RestaurantUserType.SUPER_ADMIN)){
            orderStatus = OrderStatus.PAID;
        }else {
            orderStatus = OrderStatus.PAY_REQUEST;
        }

        Order order = orderRepo.findOrderById(orderId);
        order.setStatus(orderStatus);
        orderRepo.save(order);

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setCreateDate(LocalDateTime.now());
        orderTrack.setUserReference(restaurantUser.getUsername());
        orderTrack.setStatus(orderStatus);

        orderTrackRepo.save(orderTrack);

        if(orderStatus.equals(OrderStatus.PAID)){
            List<OrderTrack> trackList = orderTrackRepo.findOrderTrackByOrderAndStatus(order,OrderStatus.DELIVERED);

            if(trackList != null && !trackList.isEmpty()){

                order.setStatus(OrderStatus.CLOSED);
                orderRepo.save(order);

                OrderTrack orderTrackClosed = new OrderTrack();
                orderTrackClosed.setOrder(order);
                orderTrackClosed.setCreateDate(LocalDateTime.now());
                orderTrackClosed.setUserReference(restaurantUser.getUsername());
                orderTrackClosed.setStatus(OrderStatus.CLOSED);

                orderTrackRepo.save(orderTrackClosed);
            }
        }
    }

    public void cancelOrder(Long orderId, RestaurantUser restaurantUser, Locale locale) {

        Order order = orderRepo.findOrderById(orderId);
        order.setStatus(OrderStatus.CANCELED);
        orderRepo.save(order);

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setCreateDate(LocalDateTime.now());
        orderTrack.setUserReference(restaurantUser.getUsername());
        orderTrack.setStatus(OrderStatus.CANCELED);

        orderTrackRepo.save(orderTrack);

    }

    public void deliverOrder(Long orderId, RestaurantUser restaurantUser, Locale locale) {

        Order order = orderRepo.findOrderById(orderId);

        if(order.getStatus().equals(OrderStatus.PAY_REQUEST)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"Please approve payment before doing any action",locale);
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setCreateDate(LocalDateTime.now());
        orderTrack.setUserReference(restaurantUser.getUsername());
        orderTrack.setStatus(OrderStatus.DELIVERED);

        orderTrackRepo.save(orderTrack);

        List<OrderTrack> trackList = orderTrackRepo.findOrderTrackByOrderAndStatus(order,OrderStatus.PAID);

        if(trackList != null && !trackList.isEmpty()){

            order.setStatus(OrderStatus.CLOSED);
            orderRepo.save(order);

            OrderTrack orderTrackClosed = new OrderTrack();
            orderTrackClosed.setOrder(order);
            orderTrackClosed.setCreateDate(LocalDateTime.now());
            orderTrackClosed.setUserReference(restaurantUser.getUsername());
            orderTrackClosed.setStatus(OrderStatus.CLOSED);

            orderTrackRepo.save(orderTrackClosed);
        }
    }


    public OrderInfoRs findOrderInfo(Long orderId, Locale locale){


        Order order = orderRepo.findOrderById(orderId);

        if(order == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        OrderInfoRs orderInfoRs = order.toOrderInfoRs();

        List<OrderItem> itemList = orderItemRepo.findOrderItemByOrder(order);
        orderInfoRs.setItemList(itemList);

        List<OrderTrack> trackList = orderTrackRepo.findOrderTrackByOrder(order);
        orderInfoRs.setTrackList(trackList);

        return orderInfoRs;
    }

    public void cancelOrderJob() {

        LocalDateTime currentDateTime = LocalDateTime.now();

        System.out.println("cancelOrderJob: "+currentDateTime);

        List<Order> orderList = orderRepo.findAllRunningOrders(currentDateTime);

        if (orderList == null) {
            return;
        }

        System.out.println("size: "+orderList.size());

        orderList.forEach(order -> {

            Duration duration = Duration.between(order.getCreateDate(),currentDateTime);
            if(duration.toMinutes() > 5){
                System.out.println("cancel: "+order.getReference());
                order.setStatus(OrderStatus.CANCELED);

                orderRepo.save(order);

                OrderTrack orderTrackCancel = new OrderTrack();
                orderTrackCancel.setOrder(order);
                orderTrackCancel.setCreateDate(LocalDateTime.now());
                orderTrackCancel.setUserReference("System");
                orderTrackCancel.setStatus(OrderStatus.CANCELED);

                orderTrackRepo.save(orderTrackCancel);
            }
        });

        return;
    }

}
