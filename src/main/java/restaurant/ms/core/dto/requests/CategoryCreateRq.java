package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryCreateRq {

    private String nameEn;
    private String nameAr;
    private String status;
    private String avatar;
}
