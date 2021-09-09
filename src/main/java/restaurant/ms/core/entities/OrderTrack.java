package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.enums.OrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_track")
@Setter
@Getter
@NoArgsConstructor
public class OrderTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_track_seq")
    @SequenceGenerator(name="order_track_seq", sequenceName = "order_track_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "user_reference")
    private String userReference;
}
