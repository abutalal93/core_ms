package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class QrInfoRs {

    private Long id;
    private String brandName;
    private String logo;
    private List<CategoryInfoRs> categoryList;
    private List<OrderSearchRs> orderList;
}
