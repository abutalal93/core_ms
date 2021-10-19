package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TopTenRs {

    private Long id;
    private String alias;
    private String nameEn;
    private String nameAr;
    private Long count;
}
