package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemInfoRs {

    private Long id;
    private String code;
    private String nameEn;
    private String nameAr;
    private String avatar;
    private String status;
    private BigDecimal unitPrice;
    private String description;
    private Long categoryId;
}
