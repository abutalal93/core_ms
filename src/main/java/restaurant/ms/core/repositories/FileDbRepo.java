package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.FileDb;

import java.util.List;

public interface FileDbRepo extends CrudRepository<FileDb,String> {

    public List<FileDb> findAllBy();
}
