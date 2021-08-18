package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Region;

import java.util.List;

public interface RegionRepo extends CrudRepository<Region,String> {

    public List<Region> findAll();
}
