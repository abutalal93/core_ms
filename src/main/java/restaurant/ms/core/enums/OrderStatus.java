package restaurant.ms.core.enums;

public enum OrderStatus {
    INIT,
    CANCELED,
    APPROVED,
    PAID,
    DELIVERED;

    public static OrderStatus getValue(String value){
        try {
            return OrderStatus.valueOf(value);
        }catch (Exception ex){
            return null;
        }
    }
}
