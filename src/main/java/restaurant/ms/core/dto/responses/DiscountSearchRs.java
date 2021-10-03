package restaurant.ms.core.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.CalculationType;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DiscountSearchRs {

    private Long id;
    private String name;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status;
    private List<ItemSearchRs> itemList;
}
