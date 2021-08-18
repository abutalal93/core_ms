package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FileRs {

    private String name;
    private String url;
    private String type;
    private Long size;
}
