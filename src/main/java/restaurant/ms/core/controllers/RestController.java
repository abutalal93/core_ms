package restaurant.ms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import restaurant.ms.core.configrations.MessageEnvelope;
import restaurant.ms.core.dto.requests.*;
import restaurant.ms.core.dto.responses.PageRs;
import restaurant.ms.core.dto.responses.RestUserLoginRs;
import restaurant.ms.core.entities.RestaurantUser;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private RestUserService restUserService;

    @Autowired
    private QrService qrService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> userLogin(HttpServletRequest httpServletRequest,
                                                      @RequestBody SpLoginRq spLoginRq) {
        Locale locale = httpServletRequest.getLocale();

        RestUserLoginRs restUserLoginRs = restUserService.loginRestUser(spLoginRq,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", restUserLoginRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/refresh",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> userLogin(HttpServletRequest httpServletRequest) {

        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        RestUserLoginRs RestUserLoginRs = new RestUserLoginRs();
        RestUserLoginRs.setId(restaurantUser.getId());
        RestUserLoginRs.setUsername(restaurantUser.getUsername());
        RestUserLoginRs.setFirstName(restaurantUser.getFirstName());
        RestUserLoginRs.setLastName(restaurantUser.getLastName());
        RestUserLoginRs.setAvatar(restaurantUser.getRestaurant().getLogo());

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", RestUserLoginRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/search",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> searchUser(HttpServletRequest httpServletRequest,
                                                    @RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "size", required = false) Integer size) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        PageRs pageRs = restUserService.searchUser(restaurantUser,page,size,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", pageRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/user/add",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> addUser(HttpServletRequest httpServletRequest,
                                                   @RequestBody RestUserCreateRq restUserCreateRq) {

        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            restUserService.createRestUser(restUserCreateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("username_unique")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Username alias already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/update",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> upadteUser(HttpServletRequest httpServletRequest,
                                                   @RequestBody RestUserUpdateRq restUserUpdateRq) {

        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            restUserService.updateRestUser(restUserUpdateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("username_unique")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Username alias already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/activeInactive",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> activeOrInactiveUser(HttpServletRequest httpServletRequest,
                                                              @RequestParam(value = "userId", required = false) Long userId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        restUserService.activeOrInactiveRestUser(userId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/delete",method = RequestMethod.DELETE)
    public ResponseEntity<MessageEnvelope> deleteUser(HttpServletRequest httpServletRequest,
                                                    @RequestParam(value = "userId", required = false) Long userId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        restUserService.deleteRestUser(userId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/changePass",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> changeUserPassword(HttpServletRequest httpServletRequest,
                                                      @RequestBody ChangeRestUserPasswordRq changeRestUserPasswordRq) {

        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        restUserService.changeRestUserPassword(changeRestUserPasswordRq,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    ////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/qr/search",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> searchQr(HttpServletRequest httpServletRequest,
                                                    @RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "size", required = false) Integer size) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        PageRs pageRs = qrService.searchQr(restaurantUser,page,size,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", pageRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/qr/create",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> createQr(HttpServletRequest httpServletRequest,
                                                    @RequestBody QrCreateRq qrCreateRq) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            qrService.createQr(qrCreateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("qralias")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "QR alias already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/qr/update",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> updateQr(HttpServletRequest httpServletRequest,
                                                    @RequestBody QrUpdateRq qrUpdateRq) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            qrService.updateQr(qrUpdateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("qralias")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "QR alias already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/qr/activeInactive",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> activeOrInactiveQr(HttpServletRequest httpServletRequest,
                                                              @RequestParam(value = "qrId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        qrService.activeOrInactiveQr(qrId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/qr/delete",method = RequestMethod.DELETE)
    public ResponseEntity<MessageEnvelope> deleteQr(HttpServletRequest httpServletRequest,
                                                    @RequestParam(value = "qrId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        qrService.deleteQr(qrId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    ////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/category/search",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> searchCategory(HttpServletRequest httpServletRequest,
                                                          @RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        PageRs pageRs = categoryService.searchCategory(restaurantUser,page,size,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", pageRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/category/create",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> createCategory(HttpServletRequest httpServletRequest,
                                                          @RequestBody CategoryCreateRq categoryCreateRq) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            categoryService.createCategory(categoryCreateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("categoryrestidwithnameen")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in english already exist", locale);
            }

            if(message != null && message.contains("categoryrestidwithnamear")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in arabic already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/category/update",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> updateCategory(HttpServletRequest httpServletRequest,
                                                          @RequestBody CategoryUpdateRq categoryUpdateRq) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            categoryService.updateCategory(categoryUpdateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("categoryrestidwithnameen")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in english already exist", locale);
            }

            if(message != null && message.contains("categoryrestidwithnamear")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in arabic already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/activeInactive",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> activeOrInactiveCategory(HttpServletRequest httpServletRequest,
                                                                    @RequestParam(value = "categoryId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        categoryService.activeOrInactiveCategory(qrId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/delete",method = RequestMethod.DELETE)
    public ResponseEntity<MessageEnvelope> deleteCategory(HttpServletRequest httpServletRequest,
                                                          @RequestParam(value = "categoryId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        categoryService.deleteCategory(qrId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    ////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/item/search",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> searchItem(HttpServletRequest httpServletRequest,
                                                          @RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        PageRs pageRs = itemService.searchItem(restaurantUser,page,size,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", pageRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/item/create",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> createItem(HttpServletRequest httpServletRequest,
                                                          @RequestBody ItemCreateRq itemCreateRq) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            itemService.createItem(itemCreateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("itemrestidwithnameen")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in english already exist", locale);
            }

            if(message != null && message.contains("itemrestidwithnamear")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in arabic already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    @RequestMapping(value = "/item/update",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> updateItem(HttpServletRequest httpServletRequest,
                                                          @RequestBody ItemUpdateRq itemUpdateRq) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        try {
            itemService.updateItem(itemUpdateRq,restaurantUser,locale);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMessage();

            if(message != null && message.contains("itemrestidwithnameen")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in english already exist", locale);
            }

            if(message != null && message.contains("itemrestidwithnamear")){
                throw new HttpServiceException(HttpStatus.BAD_REQUEST, "Category name in arabic already exist", locale);
            }

            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "ٍSomething wrong, please contact system administrator", locale);
        }

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/item/activeInactive",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> activeOrInactiveItem(HttpServletRequest httpServletRequest,
                                                                    @RequestParam(value = "itemId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        itemService.activeOrInactiveItem(qrId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/item/delete",method = RequestMethod.DELETE)
    public ResponseEntity<MessageEnvelope> deleteItem(HttpServletRequest httpServletRequest,
                                                          @RequestParam(value = "itemId", required = false) Long qrId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        itemService.deleteItem(qrId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }


    ////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "/order/search",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> searchOrder(HttpServletRequest httpServletRequest,
                                                      @RequestParam(value = "page", required = false) Integer page,
                                                      @RequestParam(value = "size", required = false) Integer size) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        PageRs pageRs = orderService.searchOrder(restaurantUser,page,size,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", pageRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/pay",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> orderPay(HttpServletRequest httpServletRequest,
                                                                @RequestParam(value = "orderId", required = false) Long orderId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        orderService.payOrder(orderId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/deliver",method = RequestMethod.PUT)
    public ResponseEntity<MessageEnvelope> orderDeliver(HttpServletRequest httpServletRequest,
                                                                @RequestParam(value = "orderId", required = false) Long orderId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        orderService.deliverOrder(orderId,restaurantUser,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/timeline",method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> orderTimeline(HttpServletRequest httpServletRequest,
                                                        @RequestParam(value = "orderId", required = false) Long orderId) {
        Locale locale = httpServletRequest.getLocale();

        RestaurantUser restaurantUser = restUserService.getRestUser(httpServletRequest);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

}
