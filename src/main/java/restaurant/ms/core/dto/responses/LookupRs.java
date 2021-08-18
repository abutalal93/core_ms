package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
public class LookupRs {

    private Long id;
    private String code;
    private String nameEn;
    private String nameAr;
    private Long parentId;
}
