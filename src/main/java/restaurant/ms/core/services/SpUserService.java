package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.FileRs;
import restaurant.ms.core.dto.responses.SpLoginRs;
import restaurant.ms.core.entities.FileDb;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.FileDbRepo;
import restaurant.ms.core.repositories.SpUserRepo;
import restaurant.ms.core.security.CustomUserDetails;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@Transactional
public class SpUserService {

    @Autowired
    private SpUserRepo spUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public SpLoginRs loginSpUser(SpLoginRq spLoginRq, Locale locale){

        SpUser spUser = spUserRepo.findSpUserByUsername(spLoginRq.getUsername());

        if(spUser == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_not_found",locale);
        }

        if(spUser.getStatus().equals(Status.INACTIVE)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_inactive",locale);
        }

        if(spUser.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_deleted",locale);
        }

        if (!passwordEncoder.matches(spLoginRq.getPassword(), spUser.getPassword())) {
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED, "invalid_login", locale);
        }

        JwtUser jwtUser = new JwtUser(spUser.getId(), spUser.getUsername(), spUser.getPassword(), "SP", "SUPER_ADMIN");
        String token = jwtTokenProvider.createToken(jwtUser);

        SpLoginRs spLoginRs = new SpLoginRs();
        spLoginRs.setId(spUser.getId());
        spLoginRs.setUsername(spUser.getUsername());
        spLoginRs.setFirstName(spUser.getFirstName());
        spLoginRs.setLastName(spUser.getLastName());
        spLoginRs.setPassword(spUser.getPassword());
        spLoginRs.setToken(token);

        return spLoginRs;
    }

    public SpUser getSpUser(HttpServletRequest httpServletRequest) throws HttpServiceException {

        Locale locale = httpServletRequest.getLocale();

        String token = httpServletRequest.getHeader("Authorization").split(" ")[1];

        String username = jwtTokenProvider.getUsername(token);

        SpUser spUser = spUserRepo.findSpUserByUsername(username);

        if(spUser == null){
            throw new HttpServiceException(HttpStatus.FORBIDDEN, "invalid_token", locale);
        }

        if(spUser.getStatus().equals(Status.INACTIVE)){
            throw new HttpServiceException(HttpStatus.FORBIDDEN, "user_inactive", locale);
        }

        if(spUser.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.FORBIDDEN, "user_deleted", locale);
        }

        return spUser;
    }

    public SpUser findSpUserByUsername(String username,Locale locale){
        SpUser spUser = spUserRepo.findSpUserByUsername(username);

        if (spUser == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "user_not_found", locale);
        }

        if (spUser.getStatus().equals(Status.INACTIVE)) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "user_inactive", locale);
        }

        if (spUser.getStatus().equals(Status.DELETED)) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "user_deleted", locale);
        }

        return spUser;
    }

}
