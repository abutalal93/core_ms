package restaurant.ms.core.enums;

public enum CalculationType {
    FIXED,
    PERCENTAGE;

    public static CalculationType getValue(String value){
        try {
            return CalculationType.valueOf(value);
        }catch (Exception ex){
            return null;
        }
    }
}
