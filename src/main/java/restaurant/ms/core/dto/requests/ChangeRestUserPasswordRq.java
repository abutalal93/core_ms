package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChangeRestUserPasswordRq {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
