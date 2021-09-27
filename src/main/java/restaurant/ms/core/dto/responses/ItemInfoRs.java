package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.requests.ItemSpecsRq;

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
    private String deactivationDate;
    private BigDecimal quantity;
    private String taxType;
    private BigDecimal tax;
    private ItemSpecsRq itemSpecs;
}
