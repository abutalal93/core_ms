package restaurant.ms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import restaurant.ms.core.configrations.MessageEnvelope;
import restaurant.ms.core.dto.requests.*;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.QrInfoRs;
import restaurant.ms.core.dto.responses.RestUserLoginRs;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private QrService qrService;

    @Autowired
    private OrderService orderService;


    @RequestMapping(value = "/qr/info",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> qrInfo(HttpServletRequest httpServletRequest,
                                                    @RequestParam(value = "qrId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        QrInfoRs qrInfoRs = qrService.qrInfo(qrId,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", qrInfoRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/order/submit",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> qrInfo(HttpServletRequest httpServletRequest,
                                                  @RequestBody OrderSubmitRq orderSubmitRq) {

        Locale locale = httpServletRequest.getLocale();

        if(true){
            throw new HttpServiceException(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to open database connection",locale);
        }

        orderService.createOrder(orderSubmitRq,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }
}
