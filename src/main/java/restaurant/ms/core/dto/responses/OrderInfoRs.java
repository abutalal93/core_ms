package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.OrderItem;
import restaurant.ms.core.entities.OrderTrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderInfoRs {

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
    private String qrAlias;
    private List<OrderItem> itemList;
    private List<OrderTrack> trackList;

}
