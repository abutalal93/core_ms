package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.District;
import restaurant.ms.core.entities.Region;

import java.util.List;

public interface DistrictRepo extends CrudRepository<District,String> {

    public List<District> findAll();

    public List<District> findDistinctByCity_Id(Long cityId);

    public District findDistinctById(Long id);

}
