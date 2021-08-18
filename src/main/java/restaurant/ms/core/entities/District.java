package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.LookupRs;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "district")
@Setter
@Getter
@NoArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_seq")
    @SequenceGenerator(name="district_seq", sequenceName = "district_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAr;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public District(Long id) {
        this.id = id;
    }

    public LookupRs toLookupRs(){
        LookupRs lookupRs = new LookupRs();
        lookupRs.setId(this.id);
        lookupRs.setCode(this.code);
        lookupRs.setNameEn(this.nameEn);
        lookupRs.setNameAr(this.nameAr);
        lookupRs.setParentId(city.getId());

        return lookupRs;
    }

    public String getName(Locale locale){
        if(locale.getISO3Language().equalsIgnoreCase("eng")){
            return this.nameEn;
        }else {
            return this.nameAr;
        }
    }
}
