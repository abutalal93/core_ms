package restaurant.ms.core.repositories;

import org.springframework.data.repository.CrudRepository;
import restaurant.ms.core.entities.Otp;
import restaurant.ms.core.enums.Status;

import java.util.List;

public interface OtpRepo extends CrudRepository<Otp,String> {

    public List<Otp> findAll();

    public Otp findOtpById(Long id);

    public Otp findOtpByCodeAndStatus(String code, Status status);
}
