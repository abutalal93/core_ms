package restaurant.ms.core.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RestUserLoginRs {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String token;
    private String avatar;
    private String type;
    private Long restId;

    @JsonIgnore
    private String password;

}
