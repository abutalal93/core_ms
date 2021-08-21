package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryUpdateRq {

    private Long categoryId;
    private String code;
    private String nameEn;
    private String nameAr;
    private String status;
    private String avatar;
}
