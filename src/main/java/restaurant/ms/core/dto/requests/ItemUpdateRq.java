package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemUpdateRq {

    private Long itemId;
    private String nameEn;
    private String nameAr;
    private String avatar;
    private BigDecimal unitPrice;
    private String description;
    private Long categoryId;
    private String deactivationDate;
}
