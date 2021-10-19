package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DashboardPeriod {
    private String label;
    private String alias;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private DashboardValue allOrder;
    private DashboardValue pendingOrder;
    private DashboardValue closedOrder;
    private DashboardValue canceledOrder;
}
