package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class DashboardValue {

    private Long count;
    private BigDecimal value;

    public DashboardValue(Long count, BigDecimal value) {
        this.count = count;
        this.value = value != null ? value : BigDecimal.ZERO;
    }
}
