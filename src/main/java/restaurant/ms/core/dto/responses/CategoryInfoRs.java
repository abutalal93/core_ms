package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CategoryInfoRs {

    private Long id;
    private String code;
    private String nameEn;
    private String nameAr;
    private String status;
    private String avatar;
    private List<ItemInfoRs> itemList;
}
