package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurant.ms.core.dto.requests.SpLoginRq;
import restaurant.ms.core.dto.responses.LookupRs;
import restaurant.ms.core.dto.responses.RestaurantSearchRs;
import restaurant.ms.core.dto.responses.SpLoginRs;
import restaurant.ms.core.entities.City;
import restaurant.ms.core.entities.District;
import restaurant.ms.core.entities.Region;
import restaurant.ms.core.entities.SpUser;
import restaurant.ms.core.enums.RestaurantType;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.ServiceType;
import restaurant.ms.core.enums.Status;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.CityRepo;
import restaurant.ms.core.repositories.DistrictRepo;
import restaurant.ms.core.repositories.RegionRepo;
import restaurant.ms.core.repositories.SpUserRepo;
import restaurant.ms.core.security.CustomUserDetails;
import restaurant.ms.core.security.JwtTokenProvider;
import restaurant.ms.core.security.JwtUser;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class LookupService {

    @Autowired
    private RegionRepo regionRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private DistrictRepo districtRepo;

    public List<LookupRs> findLookup(Long lookupId, Long parentId, String category, Locale locale){

        if(category == null || category.isEmpty()){
            throw new HttpServiceException(HttpStatus.BAD_REQUEST,"category_should_not_empty", locale);
        }


        List<LookupRs> lookupRsList = null;

        switch (category){

            case "RIG":

                List<Region> regionList = regionRepo.findAll();

                if(regionList == null){
                    throw new HttpServiceException(HttpStatus.BAD_REQUEST,"no_data_found",locale);
                }

                lookupRsList = regionList.stream()
                        .map(region -> region.toLookupRs())
                        .collect(Collectors.toList());
                break;

            case "CIT":

                List<City> cityList = null;

                if(parentId == null){
                    cityList = cityRepo.findAll();
                }else{
                    cityList = cityRepo.findCityByRegion_Id(parentId);
                }

                if(cityList == null){
                    throw new HttpServiceException(HttpStatus.BAD_REQUEST,"no_data_found",locale);
                }

                lookupRsList = cityList.stream()
                        .map(city -> city.toLookupRs())
                        .collect(Collectors.toList());

                break;

            case "DIST":

                List<District> districtList = null;

                if(parentId == null){
                    districtList = districtRepo.findAll();
                }else{
                    districtList = districtRepo.findDistinctByCity_Id(parentId);
                }

                if(districtList == null){
                    throw new HttpServiceException(HttpStatus.BAD_REQUEST,"no_data_found",locale);
                }

                lookupRsList = districtList.stream()
                        .map(district -> district.toLookupRs())
                        .collect(Collectors.toList());
                break;

            case "REST_TYPE":

                lookupRsList = RestaurantType.getLookupsList();

                break;
            case "REST_USER_TYPE":

                lookupRsList = RestaurantUserType.getLookupsList();

                break;
            case "SERV_TYPE":

                lookupRsList = ServiceType.getLookupsList();

                break;
            case "STS":
                break;
        }

        return lookupRsList;
    }

}
