package restaurant.ms.core.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SpLoginRs {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String token;

    @JsonIgnore
    private String password;

}
