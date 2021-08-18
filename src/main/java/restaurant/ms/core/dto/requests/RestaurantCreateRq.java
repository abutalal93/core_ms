package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.RestaurantType;
import restaurant.ms.core.enums.ServiceType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class RestaurantCreateRq {

    private String commercialRegister;
    private String brandNameEn;
    private String brandNameAr;
    private String taxNumber;
    private String logo;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private Long regionId;
    private Long cityId;
    private Long districtId;
    private String longitude;
    private String latitude;
    private String streetName;
    private String buildingNumber;
    private String address;
    private String contract;
    private String restaurantType;
    private String serviceType;
    private String authorizedFirstName;
    private String authorizedSecondName;
    private String authorizedThirdName;
    private String authorizedLastName;
    private String authorizedMobileNumber;
    private String authorizedEmail;

    public Restaurant toRestaurant(){

        Restaurant restaurant = new Restaurant();

        restaurant.setCommercialRegister(this.commercialRegister);
        restaurant.setBrandNameEn(this.brandNameEn);
        restaurant.setBrandNameAr(this.brandNameAr);
        restaurant.setTaxNumber(this.taxNumber);
        restaurant.setLogo(this.logo);
        restaurant.setEmail(this.email);
        restaurant.setPhoneNumber(this.phoneNumber);
        restaurant.setMobileNumber(Utility.formatMobileNumber(this.mobileNumber));
        restaurant.setRegion(new Region(this.regionId));
        restaurant.setCity(new City(this.cityId));
        restaurant.setDistrict(new District(this.districtId));
        restaurant.setLongitude(this.longitude);
        restaurant.setLatitude(this.latitude);
        restaurant.setStreetName(this.streetName);
        restaurant.setBuildingNumber(this.buildingNumber);
        restaurant.setAddress(this.address);
        restaurant.setContract(this.contract);
        restaurant.setRestaurantType(RestaurantType.getValue(this.restaurantType));
        restaurant.setServiceType(ServiceType.getValue(this.serviceType));
        restaurant.setAuthorizedFirstName(this.authorizedFirstName);
        restaurant.setAuthorizedSecondName(this.authorizedSecondName);
        restaurant.setAuthorizedThirdName(this.authorizedThirdName);
        restaurant.setAuthorizedLastName(this.authorizedLastName);
        restaurant.setAuthorizedMobileNumber(Utility.formatMobileNumber(this.authorizedMobileNumber));
        restaurant.setAuthorizedEmail(this.authorizedEmail);

        return restaurant;
    }
}
