package restaurant.ms.core.schedul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import restaurant.ms.core.services.OrderService;

@Configuration
@EnableScheduling
public class MainScheduler {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedDelay = 20000)
    public void cancelBillDailyJob() {
        //orderService.cancelOrderJob();
    }
}
