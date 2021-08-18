package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.SpUser;

import java.util.List;

public interface SpUserRepo extends CrudRepository<SpUser,String> {

    public List<SpUser> findAll();

    @Query("select spUser from SpUser spUser " +
            "where spUser.username=:username and spUser.status <>'DELETED'")
    public SpUser findSpUserByUsername(@Param("username") String username);

    Page<SpUser> findAll(Pageable pageable);
}
