package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DashboardRestRs {

    private DashboardValue allOrder;
    private DashboardValue pendingOrder;
    private DashboardValue closedOrder;
    private DashboardValue canceledOrder;

    private List<TopTenRs> topTenCategory;
    private List<TopTenRs> topTenItem;
    private List<TopTenRs> topTenQr;
    private List<TopTenRs> topTenUser;

    private List<DashboardPeriod> genericChart;
}
