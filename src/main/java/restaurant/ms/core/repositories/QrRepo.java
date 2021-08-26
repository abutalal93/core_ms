package restaurant.ms.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import restaurant.ms.core.entities.Qr;
import restaurant.ms.core.entities.Restaurant;

import java.util.List;

public interface QrRepo extends CrudRepository<Qr,String> {

    public List<Qr> findAll();

    @Query("select qr from Qr qr where qr.status <>'DELETED' and qr.restaurant=:restaurant")
    Page<Qr> findAllByRestaurant(@Param("restaurant") Restaurant restaurant, Pageable pageable);


    public Qr findQrById(Long qrId);
}
