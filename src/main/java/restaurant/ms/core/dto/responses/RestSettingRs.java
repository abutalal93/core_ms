package restaurant.ms.core.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.requests.ItemSpecsRq;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RestSettingRs {

    private Long id;
    private String logo;
    private String qrLogo;
    private String brandNameEn;
    private String brandNameAr;
    private BigDecimal serviceFees;
    private String calculationType;
    private List<ItemSpecsRq> specsList;
}
