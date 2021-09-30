package restaurant.ms.core.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.LookupRs;
import restaurant.ms.core.enums.CalculationType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name = "discount")
@Setter
@Getter
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_seq")
    @SequenceGenerator(name="discount_seq", sequenceName = "discount_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private CalculationType discountType;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
