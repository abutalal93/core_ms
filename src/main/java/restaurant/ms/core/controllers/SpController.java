package restaurant.ms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restaurant.ms.core.configrations.MessageEnvelope;
import restaurant.ms.core.dto.requests.RestaurantCreateRq;
import restaurant.ms.core.dto.requests.RestaurantUpdateRq;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.*;
import restaurant.ms.core.entities.FileDb;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;
import restaurant.ms.core.services.FileDbService;
import restaurant.ms.core.services.RestaurantService;
import restaurant.ms.core.services.SpUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/sp")
public class SpController {

    @Autowired
    private SpUserService spUserService;

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> userLogin(HttpServletRequest httpServletRequest,
                                                      @RequestBody SpLoginRq spLoginRq) {
        Locale locale = httpServletRequest.getLocale();

        if(true){
            throw new HttpServiceException(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to open database connection",locale);
        }

        SpLoginRs spLoginRs = spUserService.loginSpUser(spLoginRq,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", spLoginRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/refresh",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> userLogin(HttpServletRequest httpServletRequest) {

        Locale locale = httpServletRequest.getLocale();

        SpUser spUser = spUserService.getSpUser(httpServletRequest);

        SpLoginRs spLoginRs = new SpLoginRs();
        spLoginRs.setId(spUser.getId());
        spLoginRs.setUsername(spUser.getUsername());
        spLoginRs.setFirstName(spUser.getFirstName());
        spLoginRs.setLastName(spUser.getLastName());
        spLoginRs.setPassword(spUser.getPassword());

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", spLoginRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    ////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/rest/search",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> restaurantCreate(HttpServletRequest httpServletRequest,
                                                            @RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "size", required = false) Integer size) {
        Locale locale = httpServletRequest.getLocale();

        SpUser spUser = spUserService.getSpUser(httpServletRequest);

        PageRs pageRs = restaurantService.searchRestaurant(page,size,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", pageRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/create",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> restaurantCreate(HttpServletRequest httpServletRequest,
                                                      @RequestBody RestaurantCreateRq restaurantCreateRq) {
        Locale locale = httpServletRequest.getLocale();

        SpUser spUser = spUserService.getSpUser(httpServletRequest);

        restaurantService.createRestaurant(restaurantCreateRq,spUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/update",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> restaurantUpdate(HttpServletRequest httpServletRequest,
                                                            @RequestBody RestaurantUpdateRq restaurantUpdateRq) {
        Locale locale = httpServletRequest.getLocale();

        SpUser spUser = spUserService.getSpUser(httpServletRequest);

        restaurantService.updateRestaurant(restaurantUpdateRq,spUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/activeInactive",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> activeOrInactiveRest(HttpServletRequest httpServletRequest,
                                                                @RequestParam(value = "restId", required = false) Long restId) {
        Locale locale = httpServletRequest.getLocale();

        SpUser spUser = spUserService.getSpUser(httpServletRequest);

        restaurantService.activeOrInactiveRest(restId,spUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/delete",method = RequestMethod.DELETE)
    public ResponseEntity<MessageEnvelope> deleteRest(HttpServletRequest httpServletRequest,
                                                      @RequestParam(value = "restId", required = false) Long restId) {
        Locale locale = httpServletRequest.getLocale();

        SpUser spUser = spUserService.getSpUser(httpServletRequest);

        restaurantService.deleteRest(restId,spUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }
}
