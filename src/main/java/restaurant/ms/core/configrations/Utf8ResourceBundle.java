package restaurant.ms.core.configrations;

import java.util.Locale;
import java.util.ResourceBundle;

public class Utf8ResourceBundle {

    public static String getString(String key, Locale locale) {
        try {
            String value = ResourceBundle.getBundle("strings", locale).getString(key);
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            return key;
        }
    }
}
