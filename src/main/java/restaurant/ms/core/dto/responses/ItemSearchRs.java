package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.enums.TaxType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemSearchRs {

    private Long id;
    private String code;
    private String nameEn;
    private String nameAr;
    private String avatar;
    private String status;
    private BigDecimal unitPrice;
    private String description;
    private Long categoryId;
    private Long specsId;
    private String deactivationDate;
    private String taxType;
    private BigDecimal tax;
}
