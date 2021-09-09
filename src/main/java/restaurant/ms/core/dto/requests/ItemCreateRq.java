package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.Category;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemCreateRq {

    private String nameEn;
    private String nameAr;
    private String avatar;
    private BigDecimal unitPrice;
    private String description;
    private Long categoryId;
    private String deactivationDate;
}
