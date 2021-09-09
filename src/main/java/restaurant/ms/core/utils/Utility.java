package restaurant.ms.core.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static LocalDate parseDateFromString(String date, String pattern) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate localDate = LocalDate.parse(date.trim(), formatter);
            return localDate;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String parseDateFromString(LocalDate localDate, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formattedDateTime = localDate.format(formatter);
            return formattedDateTime;
        } catch (Exception ex) {
            return null;
        }

    }
}
