package restaurant.ms.core.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.entities.ItemDetail;
import restaurant.ms.core.entities.Restaurant;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ItemSpecsRq {

    private Long id;
    private String alias;
    private Status status;
    private List<ItemDetailRq> detailList;
}
