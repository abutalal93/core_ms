package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.Restaurant;

@Setter
@Getter
@NoArgsConstructor
public class RestaurantSearchRs {

    private Long id;
    private String commercialRegister;
    private String brandNameEn;
    private String brandNameAr;
    private String taxNumber;
    private String logo;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private String status;
}
