package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Qr;

import java.util.List;

public interface QrRepo extends CrudRepository<Qr,String> {

    public List<Qr> findAll();
}
