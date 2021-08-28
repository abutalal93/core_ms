package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RestUserSearchRs {

    private Long id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String lastName;
    private String username;
    private String email;
    private String mobileNumber;
    private String status;
    private String userType;
}
