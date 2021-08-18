package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.responses.LookupRs;
import restaurant.ms.core.entities.*;
import restaurant.ms.core.enums.RestaurantType;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.ServiceType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.*;
import restaurant.ms.core.utils.Utility;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class SeedService {

    @Autowired
    private RegionRepo regionRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private DistrictRepo districtRepo;

    @Autowired
    private SpUserRepo spUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private RestaurantUserRepo restaurantUserRepo;

    public void seed(){

        ////////////////////////////////////////////////
        Region shamalRegion = new Region();
        shamalRegion.setCode("1");
        shamalRegion.setNameEn("North Province");
        shamalRegion.setNameAr("اقليم الشمال");
        regionRepo.save(shamalRegion);

        Region centerRegion = new Region();
        centerRegion.setCode("2");
        centerRegion.setNameEn("Central province");
        centerRegion.setNameAr("اقليم الوسط");
        regionRepo.save(centerRegion);

        Region janoubRegion = new Region();
        janoubRegion.setCode("3");
        janoubRegion.setNameEn("South province");
        janoubRegion.setNameAr("اقليم الجنوب");
        regionRepo.save(janoubRegion);

        ////////////////////////////////////////////////
        City irbid = new City();
        irbid.setCode("1");
        irbid.setNameEn("Irbid");
        irbid.setNameAr("اربد");
        irbid.setRegion(shamalRegion);
        cityRepo.save(irbid);

        City jarash = new City();
        jarash.setCode("2");
        jarash.setNameEn("Jarash");
        jarash.setNameAr("جرش");
        jarash.setRegion(shamalRegion);
        cityRepo.save(jarash);

        City ajloun = new City();
        ajloun.setCode("3");
        ajloun.setNameEn("Ajloun");
        ajloun.setNameAr("عجلون");
        ajloun.setRegion(shamalRegion);
        cityRepo.save(ajloun);

        City zarqa = new City();
        zarqa.setCode("4");
        zarqa.setNameEn("Zarqa");
        zarqa.setNameAr("الزرقاء");
        zarqa.setRegion(centerRegion);
        cityRepo.save(zarqa);

        City balqa = new City();
        balqa.setCode("5");
        balqa.setNameEn("Jarash");
        balqa.setNameAr("البلقاء");
        balqa.setRegion(centerRegion);
        cityRepo.save(balqa);

        City madaba = new City();
        madaba.setCode("6");
        madaba.setNameEn("Madaba");
        madaba.setNameAr("مأدبا");
        madaba.setRegion(centerRegion);
        cityRepo.save(madaba);

        City mafraq = new City();
        mafraq.setCode("7");
        mafraq.setNameEn("Mafraq");
        mafraq.setNameAr("المفرق");
        mafraq.setRegion(centerRegion);
        cityRepo.save(mafraq);

        City amman = new City();
        amman.setCode("8");
        amman.setNameEn("Amman");
        amman.setNameAr("عمان");
        amman.setRegion(centerRegion);
        cityRepo.save(amman);

        City karak = new City();
        karak.setCode("9");
        karak.setNameEn("Karak");
        karak.setNameAr("الكرك");
        karak.setRegion(janoubRegion);
        cityRepo.save(karak);

        City tafilah = new City();
        tafilah.setCode("10");
        tafilah.setNameEn("Tafilah");
        tafilah.setNameAr("الطفيلة");
        tafilah.setRegion(janoubRegion);
        cityRepo.save(tafilah);

        City maan = new City();
        maan.setCode("11");
        maan.setNameEn("Ma'an");
        maan.setNameAr("معان");
        maan.setRegion(janoubRegion);
        cityRepo.save(maan);

        City aqaba = new City();
        aqaba.setCode("12");
        aqaba.setNameEn("Aqaba");
        aqaba.setNameAr("العقبة");
        aqaba.setRegion(janoubRegion);
        cityRepo.save(aqaba);

        ////////////////////////////////////////////////

        SpUser spUser = new SpUser();
        spUser.setFirstName("Abdallah");
        spUser.setLastName("Dabbas");
        spUser.setCreateDate(LocalDateTime.now());
        spUser.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        spUser.setEmail("adabbas@hotmail.com");
        spUser.setUsername("adabbas");
        spUser.setPassword(passwordEncoder.encode("Yewx44bn"));
        spUser.setStatus(Status.ACTIVE);

        spUserRepo.save(spUser);

        ////////////////////////////////////////////////

        Restaurant restaurant = new Restaurant();
        restaurant.setCommercialRegister("9931065088");
        restaurant.setBrandNameEn("Burger Maker");
        restaurant.setBrandNameAr("برغر ميكر");
        restaurant.setTaxNumber("00120210210");
        restaurant.setLogo("https://www.abdalimall.com/images/stores/63648583538539629731065.jpg");
        restaurant.setEmail("adabbas@bg.com");
        restaurant.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        restaurant.setPhoneNumber("053812059");
        restaurant.setRegion(centerRegion);
        restaurant.setCity(amman);
        restaurant.setStreetName("King Rania St");
        restaurant.setBuildingNumber("15");
        restaurant.setAddress("Amman, Madina monawara St , 52525");
        restaurant.setRestaurantType(RestaurantType.RESTAURANT);
        restaurant.setServiceType(ServiceType.HALL_TAKE_AWAY);
        restaurant.setSpUser(spUser);
        restaurant.setStatus(Status.ACTIVE);
        restaurant.setCreateDate(LocalDateTime.now());
        restaurant.setExpireDate(LocalDate.now().atStartOfDay().plusMonths(6));

        restaurantRepo.save(restaurant);

        ////////////////////////////////////////////////
        RestaurantUser restaurantUser = new RestaurantUser();
        restaurantUser.setRestaurant(restaurant);
        restaurantUser.setFirstName("Abdallah");
        restaurantUser.setLastName("Dabbas");
        restaurantUser.setCreateDate(LocalDateTime.now());
        restaurantUser.setRestaurantUserType(RestaurantUserType.SUPER_ADMIN);
        restaurantUser.setEmail("adabbas@bg.com");
        restaurantUser.setMobileNumber("0786789496");
        restaurantUser.setPassword(passwordEncoder.encode("Yewx44bn"));
        restaurantUser.setUsername("adabbas");
        restaurantUser.setStatus(Status.ACTIVE);

        restaurantUserRepo.save(restaurantUser);



    }

}
