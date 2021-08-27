package restaurant.ms.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import restaurant.ms.core.dto.responses.RestaurantSearchRs;
import restaurant.ms.core.enums.RestaurantType;
import restaurant.ms.core.enums.ServiceType;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant")
@Setter
@Getter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    @SequenceGenerator(name="restaurant_seq", sequenceName = "restaurant_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "cr")
    private String commercialRegister;

    @Column(name = "brand_name_en")
    private String brandNameEn;

    @Column(name = "brand_name_ar")
    private String brandNameAr;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "logo")
    private String logo;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "contract")
    private String contract;

    @Enumerated(EnumType.STRING)
    @Column(name = "restaurant_type")
    private RestaurantType restaurantType;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "sp_user_id")
    private SpUser spUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @Column(name = "authorized_first_name")
    private String authorizedFirstName;

    @Column(name = "authorized_second_name")
    private String authorizedSecondName;

    @Column(name = "authorized_third_name")
    private String authorizedThirdName;

    @Column(name = "authorized_last_name")
    private String authorizedLastName;

    @Column(name = "authorized_mobile_number")
    private String authorizedMobileNumber;

    @Column(name = "authorized_email")
    private String authorizedEmail;

    @Column(name = "category_sequence")
    private Long categorySequence;

    @Column(name = "item_sequence")
    private Long itemSequence;

    public RestaurantSearchRs toRestaurantSearchRs(){
        RestaurantSearchRs restaurantSearchRs = new RestaurantSearchRs();

        restaurantSearchRs.setId(this.id);
        restaurantSearchRs.setBrandNameEn(this.brandNameEn);
        restaurantSearchRs.setBrandNameAr(this.brandNameAr);
        restaurantSearchRs.setCommercialRegister(this.commercialRegister);
        restaurantSearchRs.setLogo(this.logo);
        restaurantSearchRs.setTaxNumber(this.taxNumber);
        restaurantSearchRs.setEmail(this.email);
        restaurantSearchRs.setMobileNumber(this.mobileNumber);
        restaurantSearchRs.setPhoneNumber(this.phoneNumber);
        restaurantSearchRs.setStatus(this.status.name());

        return restaurantSearchRs;
    }
}
