package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.ItemInfoRs;
import restaurant.ms.core.dto.responses.ItemSearchRs;
import restaurant.ms.core.dto.responses.OrderSearchRs;
import restaurant.ms.core.enums.OrderStatus;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name = "order")
@Setter
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name="order_seq", sequenceName = "order_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "customer_name")
    private String customerName;

    @ManyToOne
    @JoinColumn(name = "qr_id")
    private Qr qr;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;

    public OrderSearchRs toOrderSearchRs(){

        OrderSearchRs orderSearchRs = new OrderSearchRs();

        orderSearchRs.setId(this.id);
        orderSearchRs.setReference(this.reference);
        orderSearchRs.setCreateDate(this.createDate);
        orderSearchRs.setCustomerName(this.customerName);
        orderSearchRs.setEmail(this.email);
        orderSearchRs.setMobileNumber(this.mobileNumber);
        orderSearchRs.setQrId(this.qr.getId());
        orderSearchRs.setRestaurantId(this.restaurant.getId());
        orderSearchRs.setStatus(this.status.name());
        orderSearchRs.setTotalAmount(this.totalAmount);

        return orderSearchRs;
    }
}
