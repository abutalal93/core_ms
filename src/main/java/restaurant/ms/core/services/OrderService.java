package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.responses.OrderSearchRs;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.entities.Category;
import restaurant.ms.core.entities.Order;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.OrderRepo;
import restaurant.ms.core.repositories.RestaurantRepo;
import restaurant.ms.core.utils.Utility;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

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

}
