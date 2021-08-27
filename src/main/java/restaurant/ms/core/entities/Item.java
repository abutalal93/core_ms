package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.CategorySearchRs;
import restaurant.ms.core.dto.responses.ItemInfoRs;
import restaurant.ms.core.dto.responses.ItemSearchRs;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Locale;

@Entity
@Table(name = "item",
        uniqueConstraints = {
                @UniqueConstraint(name = "RestIdWithNameEn", columnNames = {"restaurant_id", "name_en"}),
                @UniqueConstraint(name = "RestIdWithNameAr", columnNames = {"restaurant_id", "name_ar"})})
@Setter
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name="item_seq", sequenceName = "item_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public ItemSearchRs toItemSearchRs(){
        ItemSearchRs itemSearchRs = new ItemSearchRs();

        itemSearchRs.setAvatar(this.avatar);
        itemSearchRs.setNameEn(this.nameEn);
        itemSearchRs.setNameAr(this.nameAr);
        itemSearchRs.setUnitPrice(this.unitPrice);
        itemSearchRs.setDescription(this.description);
        itemSearchRs.setCode(this.code);
        itemSearchRs.setStatus(this.status.name());
        itemSearchRs.setId(this.id);
        itemSearchRs.setCategoryId(this.category.getId());

        return itemSearchRs;
    }

    public ItemInfoRs toItemInfoRs(){
        ItemInfoRs itemInfoRs = new ItemInfoRs();

        itemInfoRs.setAvatar(this.avatar);
        itemInfoRs.setNameEn(this.nameEn);
        itemInfoRs.setNameAr(this.nameAr);
        itemInfoRs.setUnitPrice(this.unitPrice);
        itemInfoRs.setDescription(this.description);
        itemInfoRs.setCode(this.code);
        itemInfoRs.setStatus(this.status.name());
        itemInfoRs.setId(this.id);
        itemInfoRs.setCategoryId(this.category.getId());

        return itemInfoRs;
    }

    public String getName(Locale locale){
        if(locale.getISO3Language().equalsIgnoreCase("eng")){
            return this.nameEn;
        }else {
            return this.nameAr;
        }
    }
}
