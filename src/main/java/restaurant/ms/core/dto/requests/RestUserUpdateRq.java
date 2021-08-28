package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RestUserUpdateRq {

    private Long userId;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
}
