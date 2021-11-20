package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ForgetRestUserPasswordRq {

    private String username;
    private String otp;
    private String newPassword;
    private String confirmPassword;
}
