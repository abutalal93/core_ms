package restaurant.ms.core.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PageRs {

    private Long total;
    private Integer numberOfPages;
    private Object pageList;

    public PageRs(Long total, Integer numberOfPages, Object pageList) {
        this.total = total;
        this.numberOfPages = numberOfPages;
        this.pageList = pageList;
    }
}
