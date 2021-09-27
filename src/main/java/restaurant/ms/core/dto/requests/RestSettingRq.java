package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<ItemSpecsRq> specsList;

}
