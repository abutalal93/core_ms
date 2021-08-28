package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.RestUserCreateRq;
import restaurant.ms.core.dto.requests.RestUserUpdateRq;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.RestUserLoginRs;
import restaurant.ms.core.dto.responses.RestUserSearchRs;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.RestaurantUserRepo;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    public PageRs searchUser(RestaurantUser restaurantUser, Integer page, Integer size, Locale locale) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<RestaurantUser> restUserPage = restaurantUserRepo.findAllBy(restaurantUser.getRestaurant(),pageable);

        List<RestaurantUser> restaurantUserList = restUserPage.getContent();

        if (restaurantUserList == null) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "no_data_found", locale);
        }

        List<RestUserSearchRs> restUserSearchRsList = restaurantUserList.stream()
                .map(user -> user.toRestUserSearchRs())
                .collect(Collectors.toList());

        return new PageRs(restUserPage.getTotalElements(), restUserPage.getTotalPages(), restUserSearchRsList);
    }

    public void createRestUser(RestUserCreateRq restUserCreateRq, RestaurantUser restaurantUser, Locale locale) {

        RestaurantUser restUser = new RestaurantUser();
        restUser.setFirstName(restUserCreateRq.getFirstName());
        restUser.setSecondName(restUserCreateRq.getSecondName());
        restUser.setThirdName(restUserCreateRq.getThirdName());
        restUser.setLastName(restUserCreateRq.getLastName());
        restUser.setUsername(restUserCreateRq.getUsername());
        restUser.setRestaurant(restaurantUser.getRestaurant());
        restUser.setRestaurantUserType(RestaurantUserType.WAITRESS);
        restUser.setEmail(restUserCreateRq.getEmail());
        restUser.setMobileNumber(restUserCreateRq.getMobileNumber());
        restUser.setCreateDate(LocalDateTime.now());
        restUser.setStatus(Status.ACTIVE);
        restUser.setPassword(passwordEncoder.encode(restUserCreateRq.getPassword()));

        restaurantUserRepo.save(restUser);
    }

    public void updateRestUser(RestUserUpdateRq restUserUpdateRq, RestaurantUser restaurantUser, Locale locale) {

        RestaurantUser currentRestUser = restaurantUserRepo.findRestaurantUserById(restUserUpdateRq.getUserId());

        RestaurantUser restUser = new RestaurantUser();
        restUser.setId(restUserUpdateRq.getUserId());
        restUser.setFirstName(restUserUpdateRq.getFirstName());
        restUser.setSecondName(restUserUpdateRq.getSecondName());
        restUser.setThirdName(restUserUpdateRq.getThirdName());
        restUser.setLastName(restUserUpdateRq.getLastName());
        restUser.setUsername(restUserUpdateRq.getUsername());
        restUser.setRestaurant(restaurantUser.getRestaurant());
        restUser.setRestaurantUserType(currentRestUser.getRestaurantUserType());
        restUser.setEmail(restUserUpdateRq.getEmail());
        restUser.setMobileNumber(restUserUpdateRq.getMobileNumber());
        restUser.setCreateDate(currentRestUser.getCreateDate());
        restUser.setStatus(currentRestUser.getStatus());
        restUser.setPassword(currentRestUser.getPassword());

        restaurantUserRepo.save(restUser);
    }


    public void activeOrInactiveRestUser(Long restUserId, RestaurantUser restaurantUser, Locale locale) {

        RestaurantUser restUser = restaurantUserRepo.findRestaurantUserById(restUserId);

        if(restUser == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"User not found",locale);
        }

        if(restUser.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"User not found",locale);
        }

        if(restUser.getStatus().equals(Status.INACTIVE)){
            restUser.setStatus(Status.ACTIVE);
        }else{
            if(restUser.getStatus().equals(Status.ACTIVE)){
                restUser.setStatus(Status.INACTIVE);
            }
        }

        restaurantUserRepo.save(restUser);
    }

    public void deleteRestUser(Long restUserId, RestaurantUser restaurantUser, Locale locale) {

        RestaurantUser restUser = restaurantUserRepo.findRestaurantUserById(restUserId);

        if(restUser == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(restUser.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        restUser.setStatus(Status.DELETED);

        restaurantUserRepo.save(restUser);
    }

}
