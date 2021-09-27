package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.ItemSpecs;
import restaurant.ms.core.enums.Status;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ItemDetailRq {

    private Long id;
    private String nameEn;
    private String nameAr;
    private BigDecimal unitPrice;
}
