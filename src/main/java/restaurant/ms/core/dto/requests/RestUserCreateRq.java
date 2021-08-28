package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.enums.RestaurantUserType;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
public class RestUserCreateRq {

    private String firstName;
    private String secondName;
    private String thirdName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
}
