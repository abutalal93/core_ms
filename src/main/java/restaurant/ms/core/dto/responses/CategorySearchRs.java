package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CategorySearchRs {

    private Long id;
    private String code;
    private String nameEn;
    private String nameAr;
    private String status;
    private String avatar;
    private String deactivationDate;
}
