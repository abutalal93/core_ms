package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.requests.ItemDetailRq;
import restaurant.ms.core.dto.requests.ItemSpecsRq;
import restaurant.ms.core.dto.responses.LookupRs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Entity
@Table(name = "item_detail")
@Setter
@Getter
@NoArgsConstructor
public class ItemDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_detail_seq")
    @SequenceGenerator(name="item_detail_seq", sequenceName = "item_detail_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "item_specs_id")
    private ItemSpecs itemSpecs;

    public ItemDetailRq toItemDetailRq(){
        ItemDetailRq itemDetailRq = new ItemDetailRq();

        itemDetailRq.setId(this.id);
        itemDetailRq.setNameEn(this.nameEn);
        itemDetailRq.setNameAr(this.nameAr);
        itemDetailRq.setUnitPrice(this.unitPrice);


        return itemDetailRq;
    }

    public String getName(Locale locale){
        if(locale.getISO3Language().equalsIgnoreCase("eng")){
            return this.nameEn;
        }else {
            return this.nameAr;
        }
    }
}
