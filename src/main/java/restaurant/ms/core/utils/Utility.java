package restaurant.ms.core.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class Utility {

    public static boolean isValidMobileNumber(String phoneStr) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber thePhoneNumber = null;
        try {
            thePhoneNumber = phoneUtil.parse(phoneStr.trim(), "JO");
        } catch (NumberParseException e) {
            return false;
        }
        return phoneUtil.isValidNumber(thePhoneNumber);
    }

    public static String formatMobileNumber(String phoneStr) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber thePhoneNumber = null;

            thePhoneNumber = phoneUtil.parse(phoneStr.trim(), "JO");

            return phoneUtil.format(thePhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (Exception e) {
            return null;
        }

    }
}
