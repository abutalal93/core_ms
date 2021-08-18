package restaurant.ms.core.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JwtUser {

    private Long id;
    private String username;
    private String password;
    private String userType;
    private String userRole;

    public JwtUser(Long id, String username, String password, String userType, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.userRole = userRole;
    }
}
