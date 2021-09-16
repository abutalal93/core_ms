package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.CategoryInfoRs;
import restaurant.ms.core.dto.responses.CategorySearchRs;
import restaurant.ms.core.dto.responses.QrSearchRs;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Locale;

@Entity
@Table(name = "category",
        uniqueConstraints = {
                @UniqueConstraint(name = "CategoryRestIdWithNameEn", columnNames = {"restaurant_id", "name_en"}),
                @UniqueConstraint(name = "CategoryRestIdWithNameAr", columnNames = {"restaurant_id", "name_ar"})})
@Setter
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name="category_seq", sequenceName = "category_seq", initialValue = 1, allocationSize = 1)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "deactivation_date")
    private LocalDate deactivationDate;

    public Category(Long id) {
        this.id = id;
    }

    public CategorySearchRs toCategorySearchRs(){
        CategorySearchRs categorySearchRs = new CategorySearchRs();

        categorySearchRs.setAvatar(this.avatar);
        categorySearchRs.setNameEn(this.nameEn);
        categorySearchRs.setNameAr(this.nameAr);
        categorySearchRs.setCode(this.code);
        categorySearchRs.setStatus(this.status.name());
        categorySearchRs.setId(this.id);
        categorySearchRs.setDeactivationDate(Utility.parseDateFromString(deactivationDate,"yyyy-MM-dd"));

        return categorySearchRs;
    }

    public CategoryInfoRs toCategoryInfoRs(){
        CategoryInfoRs categoryInfoRs = new CategoryInfoRs();

        categoryInfoRs.setAvatar(this.avatar);
        categoryInfoRs.setNameEn(this.nameEn);
        categoryInfoRs.setNameAr(this.nameAr);
        categoryInfoRs.setCode(this.code);
        categoryInfoRs.setStatus(this.status.name());
        categoryInfoRs.setId(this.id);
        categoryInfoRs.setDeactivationDate(Utility.parseDateFromString(deactivationDate,"yyyy-MM-hh"));

        return categoryInfoRs;
    }

    public String getName(Locale locale){
        if(locale.getISO3Language().equalsIgnoreCase("eng")){
            return this.nameEn;
        }else {
            return this.nameAr;
        }
    }
}
