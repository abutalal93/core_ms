package restaurant.ms.core.enums;

public enum TaxType {
    NA,
    EX,
    AP;

    public static TaxType getValue(String value){
        try {
            return TaxType.valueOf(value);
        }catch (Exception ex){
            return null;
        }
    }
}
