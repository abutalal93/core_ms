package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.City;

import java.util.List;

public interface CityRepo extends CrudRepository<City,String> {

    public List<City> findAll();

    public List<City> findCityByRegion_Id(Long id);

    public City findCityById(Long id);
}
