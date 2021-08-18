package restaurant.ms.core.enums;

import restaurant.ms.core.configrations.Utf8ResourceBundle;
import restaurant.ms.core.dto.responses.LookupRs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum RestaurantUserType {
    SUPER_ADMIN,
    WAITRESS;

    public static RestaurantUserType getValue(String value){
        try {
            return RestaurantUserType.valueOf(value);
        }catch (Exception ex){
            return null;
        }
    }

    public static List<LookupRs> getLookupsList() {

        List<RestaurantUserType> restaurantTypeList = Arrays.asList(RestaurantUserType.values());

        List<LookupRs> lookupRsList = new ArrayList<>();

        restaurantTypeList.forEach(restaurantType -> {

            String nameEn = Utf8ResourceBundle.getString(restaurantType.name(), Locale.ENGLISH);

            String nameAr = Utf8ResourceBundle.getString(restaurantType.name(), Locale.forLanguageTag("ar"));

            LookupRs lookupRs = new LookupRs();

            lookupRs.setCode(String.valueOf(restaurantType));
            lookupRs.setNameAr(nameEn);
            lookupRs.setNameEn(nameAr);

            lookupRsList.add(lookupRs);
        });

        return lookupRsList;
    }
}
