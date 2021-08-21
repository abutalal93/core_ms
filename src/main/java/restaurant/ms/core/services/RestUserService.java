package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.RestUserLoginRs;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.RestaurantRepo;
import restaurant.ms.core.repositories.RestaurantUserRepo;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Locale;

@Service
@Transactional
public class RestUserService {

    @Autowired
    private RestaurantUserRepo restaurantUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public RestUserLoginRs loginRestUser(SpLoginRq spLoginRq, Locale locale){

        RestaurantUser restaurantUser = restaurantUserRepo.findRestaurantUserByUsername(spLoginRq.getUsername());

        if(restaurantUser == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_not_found",locale);
        }

        if(restaurantUser.getStatus().equals(Status.INACTIVE)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_inactive",locale);
        }

        if(restaurantUser.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_deleted",locale);
        }

        if (!passwordEncoder.matches(spLoginRq.getPassword(), restaurantUser.getPassword())) {
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED, "invalid_login", locale);
        }

        JwtUser jwtUser = new JwtUser(restaurantUser.getId(), restaurantUser.getUsername(), restaurantUser.getPassword(), "REST", restaurantUser.getRestaurantUserType().name());
        String token = jwtTokenProvider.createToken(jwtUser);

        RestUserLoginRs RestUserLoginRs = new RestUserLoginRs();
        RestUserLoginRs.setId(restaurantUser.getId());
        RestUserLoginRs.setUsername(restaurantUser.getUsername());
        RestUserLoginRs.setFirstName(restaurantUser.getFirstName());
        RestUserLoginRs.setLastName(restaurantUser.getLastName());
        RestUserLoginRs.setToken(token);
        RestUserLoginRs.setAvatar(restaurantUser.getRestaurant().getLogo());

        return RestUserLoginRs;
    }

    public RestaurantUser getRestUser(HttpServletRequest httpServletRequest) throws HttpServiceException {

        Locale locale = httpServletRequest.getLocale();

        String token = httpServletRequest.getHeader("Authorization").split(" ")[1];

        String username = jwtTokenProvider.getUsername(token);

        RestaurantUser restaurantUser = restaurantUserRepo.findRestaurantUserByUsername(username);

        if(restaurantUser == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_not_found",locale);
        }

        if(restaurantUser.getStatus().equals(Status.INACTIVE)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_inactive",locale);
        }

        if(restaurantUser.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_deleted",locale);
        }

        return restaurantUser;
    }

    public RestaurantUser findRestUserByUsername(String username,Locale locale){
        RestaurantUser restaurantUser = restaurantUserRepo.findRestaurantUserByUsername(username);

        if(restaurantUser == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"user_not_found",locale);
        }

        if (restaurantUser.getStatus().equals(Status.INACTIVE)) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "user_inactive", locale);
        }

        if (restaurantUser.getStatus().equals(Status.DELETED)) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "user_deleted", locale);
        }

        return restaurantUser;
    }

}
