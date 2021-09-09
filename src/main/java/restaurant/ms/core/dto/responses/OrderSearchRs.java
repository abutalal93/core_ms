package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.OrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class OrderSearchRs {

    private Long id;
    private String reference;
    private LocalDateTime createDate;
    private BigDecimal totalAmount;
    private String mobileNumber;
    private String email;
    private String customerName;
    private Long qrId;
    private Long restaurantId;
    private String status;
}
