package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.requests.ItemSpecsRq;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DiscountInfoRs {

    private Long id;
    private String name;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
