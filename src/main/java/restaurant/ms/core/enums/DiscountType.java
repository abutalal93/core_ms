package restaurant.ms.core.enums;

public enum DiscountType {
    FIXED,
    PERCENTAGE;

    public static DiscountType getValue(String value){
        try {
            return DiscountType.valueOf(value);
        }catch (Exception ex){
            return null;
        }
    }
}
