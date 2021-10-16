package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class QrInfoRs {

    private Long id;
    private Long restId;
    private String brandName;
    private String logo;
    private String serviceFeesType;
    private BigDecimal serviceFees;
    private List<CategoryInfoRs> categoryList;
    private List<OrderSearchRs> orderList;
}
