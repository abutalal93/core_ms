package restaurant.ms.core.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.DiscountInfoRs;
import restaurant.ms.core.dto.responses.DiscountSearchRs;
import restaurant.ms.core.dto.responses.ItemSearchRs;
import restaurant.ms.core.dto.responses.LookupRs;
import restaurant.ms.core.enums.CalculationType;
import restaurant.ms.core.enums.DiscountType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
    private DiscountType discountType;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_user_id")
    private RestaurantUser restaurantUser;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "discount")
    private List<DiscountItem> discountItemList;

    public DiscountSearchRs toDiscountSearchRs(){
        DiscountSearchRs discountSearchRs = new DiscountSearchRs();

        discountSearchRs.setId(this.id);
        discountSearchRs.setStatus(this.status.name());
        discountSearchRs.setName(this.name);
        discountSearchRs.setDiscountType(this.discountType.name());
        discountSearchRs.setDiscountValue(this.discountValue);
        discountSearchRs.setStartDateTime(Utility.parseDateTimeFromString(this.startDateTime,"yyyy-MM-dd'T'HH:mm"));
        discountSearchRs.setEndDateTime(Utility.parseDateTimeFromString(this.endDateTime,"yyyy-MM-dd'T'HH:mm"));

        if(discountItemList != null && !discountItemList.isEmpty()){
            discountSearchRs.setItemList(discountItemList.stream()
                    .map(discountItem -> discountItem.toItemSearchRs())
                    .collect(Collectors.toList()));
        }

        return discountSearchRs;
    }

    public DiscountInfoRs toDiscountInfoRs(){
        DiscountInfoRs discountInfoRs = new DiscountInfoRs();

        discountInfoRs.setId(this.id);
        discountInfoRs.setName(this.name);
        discountInfoRs.setDiscountType(this.discountType.name());
        discountInfoRs.setDiscountValue(this.discountValue);
        discountInfoRs.setStartDateTime(this.startDateTime);
        discountInfoRs.setEndDateTime(this.endDateTime);

        return discountInfoRs;
    }
}
