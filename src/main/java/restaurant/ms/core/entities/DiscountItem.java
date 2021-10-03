package restaurant.ms.core.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.ItemSearchRs;
import restaurant.ms.core.enums.CalculationType;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discount_item")
@Setter
@Getter
@NoArgsConstructor
public class DiscountItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_item_seq")
    @SequenceGenerator(name="discount_item_seq", sequenceName = "discount_item_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemSearchRs toItemSearchRs(){
        ItemSearchRs itemSearchRs = new ItemSearchRs();
        itemSearchRs.setAvatar(this.item.getAvatar());
        itemSearchRs.setNameEn(this.item.getNameEn());
        itemSearchRs.setNameAr(this.item.getNameAr());
        itemSearchRs.setUnitPrice(this.item.getUnitPrice());
        itemSearchRs.setDescription(this.item.getDescription());
        itemSearchRs.setCode(this.item.getCode());
        itemSearchRs.setStatus(this.item.getStatus().name());
        itemSearchRs.setId(this.item.getId());
        itemSearchRs.setCategoryId(this.item.getCategory().getId());
        itemSearchRs.setDeactivationDate(Utility.parseDateFromString(this.item.getDeactivationDate(),"yyyy-MM-dd"));
        itemSearchRs.setTaxType(this.item.getTaxType() != null ? this.item.getTaxType().name(): null);
        itemSearchRs.setTax(this.item.getTax() == null ? BigDecimal.ZERO: this.item.getTax());
        itemSearchRs.setCategoryName(this.item.getCategory() != null ? this.item.getCategory().getNameEn(): null);
        return itemSearchRs;
    }
}
