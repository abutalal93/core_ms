package restaurant.ms.core.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.*;
import restaurant.ms.core.dto.responses.*;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.RestaurantType;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.ServiceType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.*;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;
import restaurant.ms.core.utils.BeanConverter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantService {

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestaurantUserRepo restaurantUserRepo;

    @Autowired
    private RegionRepo regionRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private DistrictRepo districtRepo;

    @Autowired
    private ItemSpecsRepo itemSpecsRepo;

    @Autowired
    private ItemDetailRepo itemDetailRepo;


    public PageRs searchRestaurant(Integer page,Integer size, Locale locale){
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Page<Restaurant> restaurantPage = restaurantRepo.findAllBy(pageable);

        List<Restaurant> restaurantList = restaurantPage.getContent();

        if(restaurantList == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"no_data_found",locale);
        }

        List<RestaurantSearchRs> restaurantSearchRsList = restaurantList.stream()
                .map(restaurant -> restaurant.toRestaurantSearchRs())
                .collect(Collectors.toList());

        return new PageRs(restaurantPage.getTotalElements(), restaurantPage.getTotalPages(), restaurantSearchRsList);
    }

    public RestSettingRs findRestSetting(RestaurantUser restaurantUser, Locale locale){

        if(restaurantUser.getRestaurantUserType().equals(RestaurantUserType.WAITRESS)){
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED,"user_not_allowed",locale);
        }

        Restaurant currentRest = restaurantRepo.findRestaurantById(restaurantUser.getRestaurant().getId());

        if(currentRest == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"Rest not found");
        }

        RestSettingRs restaurantSetting = new RestSettingRs();

        restaurantSetting.setId(currentRest.getId());
        restaurantSetting.setLogo(currentRest.getLogo());
        restaurantSetting.setBrandNameEn(currentRest.getBrandNameEn());
        restaurantSetting.setBrandNameAr(currentRest.getBrandNameAr());
        restaurantSetting.setQrLogo(currentRest.getQrLogo());
        restaurantSetting.setServiceFees(currentRest.getServiceFees());

        List<ItemSpecs> itemSpecsList = itemSpecsRepo.findAllBy(currentRest);

        if(itemSpecsList != null){
            List<ItemSpecsRq> itemSpecsRqList = itemSpecsList.stream()
                    .map(itemSpecs -> itemSpecs.toItemSpecsRq())
                    .collect(Collectors.toList());

            restaurantSetting.setSpecsList(itemSpecsRqList);
        }

        return restaurantSetting;
    }

    public void saveRestSetting(RestSettingRq restSettingRq, RestaurantUser restaurantUser, Locale locale){

        if(restaurantUser.getRestaurantUserType().equals(RestaurantUserType.WAITRESS)){
            throw new HttpServiceException(HttpStatus.UNAUTHORIZED,"user_not_allowed",locale);
        }

        Restaurant currentRest = restaurantRepo.findRestaurantById(restSettingRq.getId());

        if(currentRest == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"Rest not found");
        }

        currentRest.setLogo(restSettingRq.getLogo());
        currentRest.setQrLogo(restSettingRq.getQrLogo());
        currentRest.setBrandNameEn(restSettingRq.getBrandNameEn());
        currentRest.setBrandNameAr(restSettingRq.getBrandNameAr());
        currentRest.setServiceFees(restSettingRq.getServiceFees());

        restaurantRepo.save(currentRest);

        if(restSettingRq.getSpecsList() != null){

            for(ItemSpecsRq itemSpecsRq: restSettingRq.getSpecsList()){
                ItemSpecs itemSpecs = itemSpecsRepo.findItemSpecsById(itemSpecsRq.getId());
                if(itemSpecs != null){
                    itemSpecs = itemSpecsRepo.findItemSpecsById(itemSpecsRq.getId());
                    itemSpecs.setRestaurant(currentRest);
                    itemSpecs.setRestaurantUser(restaurantUser);
                    itemSpecs.setAlias(itemSpecsRq.getAlias());
                    itemSpecs.setStatus(Status.ACTIVE);
                }else {
                    itemSpecs = new ItemSpecs();
                    itemSpecs.setRestaurant(currentRest);
                    itemSpecs.setRestaurantUser(restaurantUser);
                    itemSpecs.setAlias(itemSpecsRq.getAlias());
                    itemSpecs.setStatus(Status.ACTIVE);
                }

                itemSpecsRepo.save(itemSpecs);

                List<ItemDetailRq> itemDetailRqList = itemSpecsRq.getDetailList();
                if(itemDetailRqList != null){
                    if(itemSpecs.getId() != null){
                        itemDetailRepo.deleteAllByItemSpecs(itemSpecs);
                    }

                    List<ItemDetail> itemDetailList = new ArrayList<>();
                    for(ItemDetailRq itemDetailRq: itemDetailRqList){
                        ItemDetail itemDetail = new ItemDetail();
                        itemDetail.setItemSpecs(itemSpecs);
                        itemDetail.setNameAr(itemDetailRq.getNameAr());
                        itemDetail.setNameEn(itemDetailRq.getNameEn());
                        itemDetail.setUnitPrice(itemDetailRq.getUnitPrice());
                        itemDetailList.add(itemDetail);
                    }
                    itemDetailRepo.saveAll(itemDetailList);
                }


            }

        }
    }

    public void createRestaurant(RestaurantCreateRq restaurantCreateRq,SpUser spUser, Locale locale){

        Restaurant currentRest = restaurantRepo.findRestaurant(restaurantCreateRq.getCommercialRegister());

        if(currentRest != null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"Commercial Register already exist");
        }

        Restaurant restaurant = restaurantCreateRq.toRestaurant();
        restaurant.setCreateDate(LocalDateTime.now());
        restaurant.setExpireDate(LocalDateTime.now().plusMonths(6));
        restaurant.setSpUser(spUser);
        restaurant.setRegion(regionRepo.findRegionById(restaurantCreateRq.getRegionId()));
        restaurant.setCity(cityRepo.findCityById(restaurantCreateRq.getCityId()));
        restaurant.setDistrict(districtRepo.findDistinctById(restaurantCreateRq.getDistrictId()));
        restaurant.setServiceType(ServiceType.HALL_TAKE_AWAY);
        restaurant.setRestaurantType(RestaurantType.RESTAURANT_COFFEE);
        restaurant.setStatus(Status.ACTIVE);

        restaurant = restaurantRepo.save(restaurant);

        RestaurantUser restaurantUser = new RestaurantUser();
        restaurantUser.setRestaurant(restaurant);
        restaurantUser.setFirstName(restaurant.getAuthorizedFirstName());
        restaurantUser.setSecondName(restaurant.getAuthorizedSecondName());
        restaurantUser.setThirdName(restaurant.getAuthorizedThirdName());
        restaurantUser.setLastName(restaurant.getAuthorizedLastName());
        restaurantUser.setMobileNumber(restaurant.getAuthorizedMobileNumber());
        restaurantUser.setEmail(restaurant.getAuthorizedEmail());
        restaurantUser.setUsername(restaurant.getCommercialRegister());
        restaurantUser.setRestaurantUserType(RestaurantUserType.SUPER_ADMIN);
        restaurantUser.setStatus(Status.ACTIVE);
        restaurantUser.setCreateDate(LocalDateTime.now());
        restaurantUser.setPassword(passwordEncoder.encode("Yewx44bn"));

        restaurantUserRepo.save(restaurantUser);
    }

    public void updateRestaurant(RestaurantUpdateRq restaurantUpdateRq, SpUser spUser, Locale locale){

        Restaurant currentRestaurant = restaurantRepo.findRestaurantById(restaurantUpdateRq.getId());

        if(currentRestaurant == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"restaurant_not_found",locale);
        }

        Restaurant restaurant = restaurantUpdateRq.toRestaurant();
//        restaurant.setCreateDate(LocalDateTime.now());
//        restaurant.setExpireDate(LocalDateTime.now().plusMonths(6));
//        restaurant.setSpUser(spUser);

        restaurantRepo.save(restaurant);
    }

    public void activeOrInactiveRest(Long itemId, SpUser spUser, Locale locale) {

        Restaurant restaurant = restaurantRepo.findRestaurantById(itemId);

        if(restaurant == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(restaurant.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(restaurant.getStatus().equals(Status.INACTIVE)){
            restaurant.setStatus(Status.ACTIVE);
        }else{
            if(restaurant.getStatus().equals(Status.ACTIVE)){
                restaurant.setStatus(Status.INACTIVE);
            }
        }

        restaurantRepo.save(restaurant);
    }

    public void deleteRest(Long itemId, SpUser spUser, Locale locale) {

        Restaurant restaurant = restaurantRepo.findRestaurantById(itemId);

        if(restaurant == null){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        if(restaurant.getStatus().equals(Status.DELETED)){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"QR not found",locale);
        }

        restaurant.setStatus(Status.DELETED);

        restaurantRepo.save(restaurant);
    }

}
