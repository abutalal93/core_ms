package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Qr;

import java.util.List;

public interface QrRepo extends CrudRepository<Qr,String> {

    public List<Qr> findAll();

    @Query("select qr from Qr qr where qr.status <>'DELETED'")
    Page<Qr> findAllBy(Pageable pageable);


    public Qr findQrById(Long qrId);
}
