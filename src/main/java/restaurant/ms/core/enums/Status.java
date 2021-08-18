package restaurant.ms.core.enums;

public enum Status {
    ACTIVE,
    INACTIVE,
    DELETED;

    public static Status getValue(String value){
        try {
            return Status.valueOf(value);
        }catch (Exception ex){
            return null;
        }
    }
}
