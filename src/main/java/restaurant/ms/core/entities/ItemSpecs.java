package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.requests.ItemDetailRq;
import restaurant.ms.core.dto.requests.ItemSpecsRq;
import restaurant.ms.core.dto.responses.ItemSearchRs;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Entity
@Table(name = "item_specs")
@Setter
@Getter
@NoArgsConstructor
public class ItemSpecs {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_specs_seq")
    @SequenceGenerator(name="item_specs_seq", sequenceName = "item_specs_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "alias")
    private String alias;

    @ManyToOne
    @JoinColumn(name = "restaurant_user_id")
    private RestaurantUser restaurantUser;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "itemSpecs")
    private List<ItemDetail> itemDetailList;

    public ItemSpecs(Long id) {
        this.id = id;
    }

    public ItemSpecsRq toItemSpecsRq(){
        ItemSpecsRq itemSpecsRq = new ItemSpecsRq();

        itemSpecsRq.setAlias(this.alias);
        itemSpecsRq.setStatus(this.status);
        itemSpecsRq.setId(this.id);

        if(itemDetailList != null){
            List<ItemDetailRq> itemDetailRqList = itemDetailList.stream()
                    .map(itemDetail -> itemDetail.toItemDetailRq())
                    .collect(Collectors.toList());

            itemSpecsRq.setDetailList(itemDetailRqList);
        }

        return itemSpecsRq;
    }
}
