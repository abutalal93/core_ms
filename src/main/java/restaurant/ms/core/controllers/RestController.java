package restaurant.ms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import restaurant.ms.core.configrations.MessageEnvelope;
import restaurant.ms.core.dto.requests.RestaurantCreateRq;
import restaurant.ms.core.dto.requests.RestaurantUpdateRq;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.RestUserLoginRs;
import restaurant.ms.core.dto.responses.SpLoginRs;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.repositories.RestaurantUserRepo;
import restaurant.ms.core.services.RestUserService;
import restaurant.ms.core.services.RestaurantService;
import restaurant.ms.core.services.SpUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private RestUserService restUserService;

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> userLogin(HttpServletRequest httpServletRequest,
                                                      @RequestBody SpLoginRq spLoginRq) {
        Locale locale = httpServletRequest.getLocale();

        RestUserLoginRs restUserLoginRs = restUserService.loginRestUser(spLoginRq,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", restUserLoginRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }
}
