package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.ItemInfoRs;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderSubmitRq {

    private String qrId;
    private String name;
    private String mobile;
    private String email;
    private BigDecimal totalAmount;
    private List<ItemInfoRs> cartList;
}
