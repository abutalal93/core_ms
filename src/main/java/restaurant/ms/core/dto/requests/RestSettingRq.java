package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.enums.CalculationType;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RestSettingRq {

    private Long id;
    private String logo;
    private String qrLogo;
    private String brandNameEn;
    private String brandNameAr;
    private BigDecimal serviceFees;
    private String calculationType;
    private List<ItemSpecsRq> specsList;
    private List<Long> deletedSpecsIdList;

}
