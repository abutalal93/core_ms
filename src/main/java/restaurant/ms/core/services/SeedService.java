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

        City madaba = new City();
        madaba.setCode("5");
        madaba.setNameEn("Madaba");
        madaba.setNameAr("مأدبا");
        madaba.setRegion(centerRegion);
        cityRepo.save(madaba);

        City mafraq = new City();
        mafraq.setCode("6");
        mafraq.setNameEn("Mafraq");
        mafraq.setNameAr("المفرق");
        mafraq.setRegion(centerRegion);
        cityRepo.save(mafraq);

        City amman = new City();
        amman.setCode("7");
        amman.setNameEn("Amman");
        amman.setNameAr("عمان");
        amman.setRegion(centerRegion);
        cityRepo.save(amman);

        City balqa = new City();
        balqa.setCode("8");
        balqa.setNameEn("Balqaa");
        balqa.setNameAr("البلقاء");
        balqa.setRegion(centerRegion);
        cityRepo.save(balqa);

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

        SpUser abdallah = new SpUser();
        abdallah.setFirstName("Abdallah");
        abdallah.setLastName("Dabbas");
        abdallah.setCreateDate(LocalDateTime.now());
        abdallah.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        abdallah.setEmail("adabbas@hotmail.com");
        abdallah.setUsername("adabbas");
        abdallah.setPassword(passwordEncoder.encode("Yewx44bn"));
        abdallah.setStatus(Status.ACTIVE);

        spUserRepo.save(abdallah);

        SpUser osama = new SpUser();
        osama.setFirstName("Osama");
        osama.setLastName("Bsharat");
        osama.setCreateDate(LocalDateTime.now());
        osama.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        osama.setEmail("bsharat@hotmail.com");
        osama.setUsername("obsharat");
        osama.setPassword(passwordEncoder.encode("Yewx44bn"));
        osama.setStatus(Status.ACTIVE);

        spUserRepo.save(osama);

        SpUser maen = new SpUser();
        maen.setFirstName("Maen");
        maen.setLastName("Omari");
        maen.setCreateDate(LocalDateTime.now());
        maen.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        maen.setEmail("momari@hotmail.com");
        maen.setUsername("momari");
        maen.setPassword(passwordEncoder.encode("Yewx44bn"));
        maen.setStatus(Status.ACTIVE);

        spUserRepo.save(maen);

        ////////////////////////////////////////////////

        Restaurant bm = new Restaurant();
        bm.setCommercialRegister("9931065088");
        bm.setBrandNameEn("Burger Maker");
        bm.setBrandNameAr("برغر ميكر");
        bm.setTaxNumber("00120210210");
        bm.setLogo("https://www.abdalimall.com/images/stores/63648583538539629731065.jpg");
        bm.setEmail("adabbas@bg.com");
        bm.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        bm.setPhoneNumber("053812059");
        bm.setRegion(centerRegion);
        bm.setCity(amman);
        bm.setStreetName("King Rania St");
        bm.setBuildingNumber("15");
        bm.setAddress("Amman, Madina monawara St , 52525");
        bm.setRestaurantType(RestaurantType.RESTAURANT);
        bm.setServiceType(ServiceType.HALL_TAKE_AWAY);
        bm.setSpUser(abdallah);
        bm.setStatus(Status.ACTIVE);
        bm.setCreateDate(LocalDateTime.now());
        bm.setExpireDate(LocalDate.now().atStartOfDay().plusMonths(6));

        restaurantRepo.save(bm);

        ////////////////////////////////////////////////
        RestaurantUser restaurantUserAbd = new RestaurantUser();
        restaurantUserAbd.setRestaurant(bm);
        restaurantUserAbd.setFirstName("Abdallah");
        restaurantUserAbd.setLastName("Dabbas");
        restaurantUserAbd.setCreateDate(LocalDateTime.now());
        restaurantUserAbd.setRestaurantUserType(RestaurantUserType.SUPER_ADMIN);
        restaurantUserAbd.setEmail("adabbas@bg.com");
        restaurantUserAbd.setMobileNumber("0786789496");
        restaurantUserAbd.setPassword(passwordEncoder.encode("Yewx44bn"));
        restaurantUserAbd.setUsername("adabbas");
        restaurantUserAbd.setStatus(Status.ACTIVE);

        restaurantUserRepo.save(restaurantUserAbd);

        ////////////////////////////////////////////////

        Restaurant ff = new Restaurant();
        ff.setCommercialRegister("9931065088");
        ff.setBrandNameEn("Firefly Burger");
        ff.setBrandNameAr("فايرفلاي ميكر");
        ff.setTaxNumber("00120210210");
        ff.setLogo("https://is4-ssl.mzstatic.com/image/thumb/Purple125/v4/15/79/76/15797642-84c1-e07d-90f0-805eabec2cbc/AppIcon-0-0-1x_U007emarketing-0-0-0-10-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/600x600wa.png");
        ff.setEmail("onsharat@ff.com");
        ff.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        ff.setPhoneNumber("053812059");
        ff.setRegion(centerRegion);
        ff.setCity(amman);
        ff.setStreetName("King Rania St");
        ff.setBuildingNumber("15");
        ff.setAddress("Amman, Madina monawara St , 52525");
        ff.setRestaurantType(RestaurantType.RESTAURANT);
        ff.setServiceType(ServiceType.HALL_TAKE_AWAY);
        ff.setSpUser(abdallah);
        ff.setStatus(Status.ACTIVE);
        ff.setCreateDate(LocalDateTime.now());
        ff.setExpireDate(LocalDate.now().atStartOfDay().plusMonths(6));

        restaurantRepo.save(ff);

        ////////////////////////////////////////////////
        RestaurantUser restaurantUserOs = new RestaurantUser();
        restaurantUserOs.setRestaurant(ff);
        restaurantUserOs.setFirstName("Osama");
        restaurantUserOs.setLastName("Bsharat");
        restaurantUserOs.setCreateDate(LocalDateTime.now());
        restaurantUserOs.setRestaurantUserType(RestaurantUserType.SUPER_ADMIN);
        restaurantUserOs.setEmail("obsharat@ff.com");
        restaurantUserOs.setMobileNumber("0786789496");
        restaurantUserOs.setPassword(passwordEncoder.encode("Yewx44bn"));
        restaurantUserOs.setUsername("obsharat");
        restaurantUserOs.setStatus(Status.ACTIVE);

        restaurantUserRepo.save(restaurantUserOs);

        Restaurant kfc = new Restaurant();
        kfc.setCommercialRegister("9931065088");
        kfc.setBrandNameEn("KFC");
        ff.setBrandNameAr("دجاج كنتاكي");
        kfc.setTaxNumber("00120210210");
        kfc.setLogo("https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/KFC_Logo.svg/640px-KFC_Logo.svg.png");
        kfc.setEmail("onsharat@ff.com");
        kfc.setMobileNumber(Utility.formatMobileNumber("0786789496"));
        kfc.setPhoneNumber("053812059");
        kfc.setRegion(centerRegion);
        kfc.setCity(amman);
        kfc.setStreetName("King Rania St");
        kfc.setBuildingNumber("15");
        kfc.setAddress("Amman, Madina monawara St , 52525");
        kfc.setRestaurantType(RestaurantType.RESTAURANT);
        kfc.setServiceType(ServiceType.HALL_TAKE_AWAY);
        kfc.setSpUser(abdallah);
        kfc.setStatus(Status.ACTIVE);
        kfc.setCreateDate(LocalDateTime.now());
        kfc.setExpireDate(LocalDate.now().atStartOfDay().plusMonths(6));

        restaurantRepo.save(kfc);

        ////////////////////////////////////////////////
        RestaurantUser restaurantUserKfc= new RestaurantUser();
        restaurantUserKfc.setRestaurant(kfc);
        restaurantUserKfc.setFirstName("Maen");
        restaurantUserKfc.setLastName("Omari");
        restaurantUserKfc.setCreateDate(LocalDateTime.now());
        restaurantUserKfc.setRestaurantUserType(RestaurantUserType.SUPER_ADMIN);
        restaurantUserKfc.setEmail("momari@ff.com");
        restaurantUserKfc.setMobileNumber("0786789496");
        restaurantUserKfc.setPassword(passwordEncoder.encode("Yewx44bn"));
        restaurantUserKfc.setUsername("momari");
        restaurantUserKfc.setStatus(Status.ACTIVE);

        restaurantUserRepo.save(restaurantUserKfc);



    }

}
