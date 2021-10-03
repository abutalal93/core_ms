package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DiscountCreateRq {

    private String name;
    private String discountType;
    private BigDecimal discountValue;
    private String startDateTime;
    private String endDateTime;
    private List<Long> itemIdList;
}
