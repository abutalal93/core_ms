package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.DiscountCreateRq;
import restaurant.ms.core.dto.requests.DiscountUpdateRq;
import restaurant.ms.core.dto.responses.*;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.DiscountType;
import restaurant.ms.core.enums.OrderStatus;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.*;
import restaurant.ms.core.utils.Utility;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardService {


    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private QrRepo qrRepo;

    @Autowired
    private RestaurantUserRepo restaurantUserRepo;

    public DashboardRestRs generateRestDashboard(RestaurantUser restaurantUser, String dashboardType, String dateFrom, String dateTo, Locale locale) {

        if(dashboardType == null){
            return generateDailyRestDashboard(restaurantUser,locale);
        }

        switch (dashboardType){
            case "DAILY":
                return generateDailyRestDashboard(restaurantUser,locale);
            case "MONTHLY":
                return generateMonthlyRestDashboard(restaurantUser,locale);
            case "YEARLY":
                return generateYearlyRestDashboard(restaurantUser,locale);
            case "DATE":
                return generateDateRestDashboard(restaurantUser,dateFrom,dateTo,locale);
            default:
                return generateDailyRestDashboard(restaurantUser,locale);
        }
    }

    private DashboardRestRs generateDailyRestDashboard(RestaurantUser restaurantUser, Locale locale){

        Restaurant restaurant = restaurantUser.getRestaurant();

        DashboardRestRs dashboardRestRs = new DashboardRestRs();

        LocalDateTime startDate = LocalDate.now().atStartOfDay();
        LocalDateTime endDate = startDate.plusHours(23).plusMinutes(59).plusSeconds(59);

        Long countOfAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startDate,endDate);
        BigDecimal sumOfAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startDate,endDate);
        dashboardRestRs.setAllOrder(new DashboardValue(countOfAllOrders,sumOfAllOrders));

        Long countOfPendingOrders = orderRepo.countOfRunningOrderByRest(restaurant,startDate,endDate);
        BigDecimal sumOfPendingOrders = orderRepo.sumOfRunningOrderByRest(restaurant,startDate,endDate);
        dashboardRestRs.setPendingOrder(new DashboardValue(countOfPendingOrders,sumOfPendingOrders));

        Long countOfClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startDate,endDate);
        BigDecimal sumOfClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startDate,endDate);
        dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

        Long countOfCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        BigDecimal sumOfCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

        //daily chart
        List<DashboardPeriod> dashboardPeriodList = new ArrayList<>();

        LocalDateTime startChartTime = LocalDate.now().atStartOfDay();
        LocalDateTime endChartTime = startChartTime.plusHours(1);

        for(int hour = 0 ; hour < 24 ; hour++){

            Long countOfChartAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startChartTime,endChartTime);
            BigDecimal sumOfChartAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startChartTime,endChartTime);

            Long countOfChartClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startChartTime,endChartTime);
            BigDecimal sumOfChartClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startChartTime,endChartTime);
            dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

            Long countOfChartCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            BigDecimal sumOfChartCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

            DashboardPeriod dashboardPeriod = new DashboardPeriod();
            dashboardPeriod.setLabel("Hours");
            dashboardPeriod.setAlias((hour+1)+"");
            dashboardPeriod.setStartDateTime(startChartTime);
            dashboardPeriod.setEndDateTime(endChartTime);
            dashboardPeriod.setAllOrder(new DashboardValue(countOfChartAllOrders,sumOfChartAllOrders));
            dashboardPeriod.setClosedOrder(new DashboardValue(countOfChartClosedOrders,sumOfChartClosedOrders));
            dashboardPeriod.setCanceledOrder(new DashboardValue(countOfChartCanceledOrders,sumOfChartCanceledOrders));

            startChartTime = startChartTime.plusHours(1);
            endChartTime = endChartTime.plusHours(1);

            dashboardPeriodList.add(dashboardPeriod);
        }

        dashboardRestRs.setGenericChart(dashboardPeriodList);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<Item> itemList = itemRepo.findAllBy(restaurant,pageable).getContent();
        List<ItemSearchRs> itemSearchRsList = itemList.stream()
                .map(item -> item.toItemSearchRs())
                .collect(Collectors.toList());

        List<Qr> qrList = qrRepo.findAllByRestaurant(restaurant,pageable).getContent();
        List<QrSearchRs> qrSearchRsList = qrList.stream()
                .map(item -> item.toQrSearchRs())
                .collect(Collectors.toList());

        List<RestaurantUser> userList = restaurantUserRepo.findAllBy(restaurant,pageable).getContent();
        List<RestUserSearchRs> restUserSearchRs = userList.stream()
                .map(item -> item.toRestUserSearchRs())
                .collect(Collectors.toList());

        dashboardRestRs.setTopTenItem(itemSearchRsList);
        dashboardRestRs.setTopTenUser(restUserSearchRs);
        dashboardRestRs.setTopTenQr(qrSearchRsList);

        return dashboardRestRs;
    }

    private DashboardRestRs generateMonthlyRestDashboard(RestaurantUser restaurantUser, Locale locale){
        Restaurant restaurant = restaurantUser.getRestaurant();

        DashboardRestRs dashboardRestRs = new DashboardRestRs();

        LocalDate initial = LocalDate.now();
        LocalDateTime startDate = initial.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = initial.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59);

        Long countOfAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startDate,endDate);
        BigDecimal sumOfAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startDate,endDate);
        dashboardRestRs.setAllOrder(new DashboardValue(countOfAllOrders,sumOfAllOrders));

        Long countOfPendingOrders = orderRepo.countOfRunningOrderByRest(restaurant,startDate,endDate);
        BigDecimal sumOfPendingOrders = orderRepo.sumOfRunningOrderByRest(restaurant,startDate,endDate);
        dashboardRestRs.setPendingOrder(new DashboardValue(countOfPendingOrders,sumOfPendingOrders));

        Long countOfClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startDate,endDate);
        BigDecimal sumOfClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startDate,endDate);
        dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

        Long countOfCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        BigDecimal sumOfCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

        //monthly chart
        List<DashboardPeriod> dashboardPeriodList = new ArrayList<>();

        LocalDateTime startChartTime = initial.withDayOfMonth(1).atStartOfDay();;
        LocalDateTime endChartTime = startChartTime.plusDays(1);

        for(int day = 1 ; day <= endDate.getDayOfMonth() ; day++){

            Long countOfChartAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startChartTime,endChartTime);
            BigDecimal sumOfChartAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startChartTime,endChartTime);

            Long countOfChartClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startChartTime,endChartTime);
            BigDecimal sumOfChartClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startChartTime,endChartTime);
            dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

            Long countOfChartCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            BigDecimal sumOfChartCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

            DashboardPeriod dashboardPeriod = new DashboardPeriod();
            dashboardPeriod.setLabel("Days");
            dashboardPeriod.setAlias(day+"");
            dashboardPeriod.setStartDateTime(startChartTime);
            dashboardPeriod.setEndDateTime(endChartTime);
            dashboardPeriod.setAllOrder(new DashboardValue(countOfChartAllOrders,sumOfChartAllOrders));
            dashboardPeriod.setClosedOrder(new DashboardValue(countOfChartClosedOrders,sumOfChartClosedOrders));
            dashboardPeriod.setCanceledOrder(new DashboardValue(countOfChartCanceledOrders,sumOfChartCanceledOrders));

            startChartTime = startChartTime.plusDays(1);
            endChartTime = endChartTime.plusDays(1);

            dashboardPeriodList.add(dashboardPeriod);
        }

        dashboardRestRs.setGenericChart(dashboardPeriodList);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<Item> itemList = itemRepo.findAllBy(restaurant,pageable).getContent();
        List<ItemSearchRs> itemSearchRsList = itemList.stream()
                .map(item -> item.toItemSearchRs())
                .collect(Collectors.toList());

        List<Qr> qrList = qrRepo.findAllByRestaurant(restaurant,pageable).getContent();
        List<QrSearchRs> qrSearchRsList = qrList.stream()
                .map(item -> item.toQrSearchRs())
                .collect(Collectors.toList());

        List<RestaurantUser> userList = restaurantUserRepo.findAllBy(restaurant,pageable).getContent();
        List<RestUserSearchRs> restUserSearchRs = userList.stream()
                .map(item -> item.toRestUserSearchRs())
                .collect(Collectors.toList());

        dashboardRestRs.setTopTenItem(itemSearchRsList);
        dashboardRestRs.setTopTenUser(restUserSearchRs);
        dashboardRestRs.setTopTenQr(qrSearchRsList);

        return dashboardRestRs;
    }

    private DashboardRestRs generateYearlyRestDashboard(RestaurantUser restaurantUser, Locale locale){
        Restaurant restaurant = restaurantUser.getRestaurant();

        DashboardRestRs dashboardRestRs = new DashboardRestRs();

        LocalDate initial = LocalDate.now();
        LocalDateTime startDate = initial.withDayOfYear(1).atStartOfDay();
        LocalDateTime endDate = initial.with(TemporalAdjusters.lastDayOfYear()).atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59);

        Long countOfAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startDate,endDate);
        BigDecimal sumOfAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startDate,endDate);
        dashboardRestRs.setAllOrder(new DashboardValue(countOfAllOrders,sumOfAllOrders));

        Long countOfPendingOrders = orderRepo.countOfRunningOrderByRest(restaurant,startDate,endDate);
        BigDecimal sumOfPendingOrders = orderRepo.sumOfRunningOrderByRest(restaurant,startDate,endDate);
        dashboardRestRs.setPendingOrder(new DashboardValue(countOfPendingOrders,sumOfPendingOrders));

        Long countOfClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startDate,endDate);
        BigDecimal sumOfClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startDate,endDate);
        dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

        Long countOfCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        BigDecimal sumOfCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

        //yearly chart
        List<DashboardPeriod> dashboardPeriodList = new ArrayList<>();

        LocalDateTime startChartTime = initial.withDayOfYear(1).atStartOfDay();;
        LocalDateTime endChartTime = startChartTime.plusMonths(1);

        for(int month = 1 ; month <= endDate.getMonthValue() ; month++){

            Long countOfChartAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startChartTime,endChartTime);
            BigDecimal sumOfChartAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startChartTime,endChartTime);

            Long countOfChartClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startChartTime,endChartTime);
            BigDecimal sumOfChartClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startChartTime,endChartTime);
            dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

            Long countOfChartCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            BigDecimal sumOfChartCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

            DashboardPeriod dashboardPeriod = new DashboardPeriod();
            dashboardPeriod.setLabel("Month");
            dashboardPeriod.setAlias(Month.of(month).name());
            dashboardPeriod.setStartDateTime(startChartTime);
            dashboardPeriod.setEndDateTime(endChartTime);
            dashboardPeriod.setAllOrder(new DashboardValue(countOfChartAllOrders,sumOfChartAllOrders));
            dashboardPeriod.setClosedOrder(new DashboardValue(countOfChartClosedOrders,sumOfChartClosedOrders));
            dashboardPeriod.setCanceledOrder(new DashboardValue(countOfChartCanceledOrders,sumOfChartCanceledOrders));

            startChartTime = startChartTime.plusMonths(1);
            endChartTime = endChartTime.plusMonths(1);

            dashboardPeriodList.add(dashboardPeriod);
        }

        dashboardRestRs.setGenericChart(dashboardPeriodList);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<Item> itemList = itemRepo.findAllBy(restaurant,pageable).getContent();
        List<ItemSearchRs> itemSearchRsList = itemList.stream()
                .map(item -> item.toItemSearchRs())
                .collect(Collectors.toList());

        List<Qr> qrList = qrRepo.findAllByRestaurant(restaurant,pageable).getContent();
        List<QrSearchRs> qrSearchRsList = qrList.stream()
                .map(item -> item.toQrSearchRs())
                .collect(Collectors.toList());

        List<RestaurantUser> userList = restaurantUserRepo.findAllBy(restaurant,pageable).getContent();
        List<RestUserSearchRs> restUserSearchRs = userList.stream()
                .map(item -> item.toRestUserSearchRs())
                .collect(Collectors.toList());

        dashboardRestRs.setTopTenItem(itemSearchRsList);
        dashboardRestRs.setTopTenUser(restUserSearchRs);
        dashboardRestRs.setTopTenQr(qrSearchRsList);

        return dashboardRestRs;
    }


    private DashboardRestRs generateDateRestDashboard(RestaurantUser restaurantUser,String dateFrom, String dateTo, Locale locale){
        Restaurant restaurant = restaurantUser.getRestaurant();

        DashboardRestRs dashboardRestRs = new DashboardRestRs();

        LocalDate fromDate = Utility.parseDateFromString(dateFrom,"yyyy-MM-dd");
        if(fromDate == null){
            fromDate = LocalDate.of(2000,1,1);
        }

        LocalDate toDate = Utility.parseDateFromString(dateTo,"yyyy-MM-dd");
        if(toDate == null){
            toDate = LocalDate.of(2030,1,1);
        }

        LocalDateTime startDate = fromDate.atStartOfDay();
        LocalDateTime endDate = toDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59);

        Long countOfAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startDate,endDate);
        BigDecimal sumOfAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startDate,endDate);
        dashboardRestRs.setAllOrder(new DashboardValue(countOfAllOrders,sumOfAllOrders));

        Long countOfPendingOrders = orderRepo.countOfRunningOrderByRest(restaurant,startDate,endDate);
        BigDecimal sumOfPendingOrders = orderRepo.sumOfRunningOrderByRest(restaurant,startDate,endDate);
        dashboardRestRs.setPendingOrder(new DashboardValue(countOfPendingOrders,sumOfPendingOrders));

        Long countOfClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startDate,endDate);
        BigDecimal sumOfClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startDate,endDate);
        dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

        Long countOfCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        BigDecimal sumOfCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startDate,endDate);
        dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

        //monthly chart
        List<DashboardPeriod> dashboardPeriodList = new ArrayList<>();

        LocalDateTime startChartTime = startDate;
        LocalDateTime endChartTime = endDate;

        for(int day = 1 ; day <= endDate.getDayOfMonth() ; day++){

            Long countOfChartAllOrders = orderRepo.countOfOrderByRest(restaurant,null,startChartTime,endChartTime);
            BigDecimal sumOfChartAllOrders = orderRepo.sumOfOrderByRest(restaurant,null,startChartTime,endChartTime);

            Long countOfChartClosedOrders = orderRepo.countOfOrderByRest(restaurant, OrderStatus.CLOSED,startChartTime,endChartTime);
            BigDecimal sumOfChartClosedOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CLOSED,startChartTime,endChartTime);
            dashboardRestRs.setClosedOrder(new DashboardValue(countOfClosedOrders,sumOfClosedOrders));

            Long countOfChartCanceledOrders = orderRepo.countOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            BigDecimal sumOfChartCanceledOrders = orderRepo.sumOfOrderByRest(restaurant,OrderStatus.CANCELED,startChartTime,endChartTime);
            dashboardRestRs.setCanceledOrder(new DashboardValue(countOfCanceledOrders,sumOfCanceledOrders));

            DashboardPeriod dashboardPeriod = new DashboardPeriod();
            dashboardPeriod.setLabel("Days");
            dashboardPeriod.setAlias(day+"");
            dashboardPeriod.setStartDateTime(startChartTime);
            dashboardPeriod.setEndDateTime(endChartTime);
            dashboardPeriod.setAllOrder(new DashboardValue(countOfChartAllOrders,sumOfChartAllOrders));
            dashboardPeriod.setClosedOrder(new DashboardValue(countOfChartClosedOrders,sumOfChartClosedOrders));
            dashboardPeriod.setCanceledOrder(new DashboardValue(countOfChartCanceledOrders,sumOfChartCanceledOrders));

            startChartTime = startChartTime.plusDays(1);
            endChartTime = endChartTime.plusDays(1);

            dashboardPeriodList.add(dashboardPeriod);
        }

        dashboardRestRs.setGenericChart(dashboardPeriodList);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<Item> itemList = itemRepo.findAllBy(restaurant,pageable).getContent();
        List<ItemSearchRs> itemSearchRsList = itemList.stream()
                .map(item -> item.toItemSearchRs())
                .collect(Collectors.toList());

        List<Qr> qrList = qrRepo.findAllByRestaurant(restaurant,pageable).getContent();
        List<QrSearchRs> qrSearchRsList = qrList.stream()
                .map(item -> item.toQrSearchRs())
                .collect(Collectors.toList());

        List<RestaurantUser> userList = restaurantUserRepo.findAllBy(restaurant,pageable).getContent();
        List<RestUserSearchRs> restUserSearchRs = userList.stream()
                .map(item -> item.toRestUserSearchRs())
                .collect(Collectors.toList());

        dashboardRestRs.setTopTenItem(itemSearchRsList);
        dashboardRestRs.setTopTenUser(restUserSearchRs);
        dashboardRestRs.setTopTenQr(qrSearchRsList);

        return dashboardRestRs;
    }

}
