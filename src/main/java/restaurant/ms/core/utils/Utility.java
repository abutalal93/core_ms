package restaurant.ms.core.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sendgrid.*;
import restaurant.ms.core.entities.RestaurantUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

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

    public static LocalDateTime parseDateTimeFromString(String date, String pattern) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localDateTime = LocalDateTime.parse(date.trim(), formatter);
            return localDateTime;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String parseDateTimeFromString(LocalDateTime dateTime, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String localDateTimeString = dateTime.format(formatter);
            return localDateTimeString;
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public static Long parseLong(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(date);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String toQueryString(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            return null;
        }
        return value.toLowerCase();
    }

    public static String generatePassword(int len) {
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";


        String values = Capital_chars + Small_chars +
                numbers + symbols;

        // Using random method
        Random rndm_method = new Random();

        char[] password = new char[len];

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            password[i] = values.charAt(rndm_method.nextInt(values.length()));

        }
        return password.toString();
    }

    public static void sendEmailResetPassword(RestaurantUser restaurantUser, String password) {
        String html = getHtmlForPasswordReset(restaurantUser, password);

        Email from = new Email("adabbas@wajdtuha.com");
        Email to = new Email(restaurantUser.getEmail());
        Content content = new Content("text/html", html);
        Mail mail = new Mail(from, "Wajdtuha Reset Password", to, content);

        SendGrid sg = new SendGrid("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\n");

        Response response = null;
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            response = sg.api(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getHtmlForPasswordReset(RestaurantUser restaurantUser, String password) {
        String emailTemp = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "   <head>\n" +
                "      <style>@font-face {    font-family: sultan1;    src: url(sultan-medium.ttf);}@font-face {    font-family: gesslight;    src: url(gesslight.otf);}#tab20 {font-family:gesslight !important;}#tab21 {font-family:gesslight !important;}.bor td{ padding:1%;} .classFont {font-size:1.5vw; color:black; font-family:gesslight !important;}#typeo {background:black; border-radius: 25px;}tbody {height:100%;}</style>\n" +
                "      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf8\" />\n" +
                "       <meta name=\"viewport\" content=\"initial-scale=2.0, user-scalable=1.0\">\n" +
                "      <title>Forget User</title>\n" +
                "   </head>\n" +
                "  <body marginheight=\"0\" marginwidth=\"0\" style=\"background-color: #FFFFFF\">\n" +
                "  <div bgcolor=\"#fafafa\" style=\"margin:0px;padding:0px\">\n" +
                "        \n" +
                "        <table style=\"padding:0;max-width:100%;margin-left:auto;margin-right:auto;border-spacing:0;border-collapse:collapse;background-color:#fafafa;font-family:sans-serif\" width=\"100%\">\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td style=\"min-width:10px\">&nbsp;</td>\n" +
                "                    <td style=\"font-size:14px;color:rgb(50,50,50);font-family:Arial;margin:0;padding:0 0 30px;background:#fafafa;text-align:center;width:584px;padding-top:0;padding-left:0;padding-right:0;padding-bottom:0;margin-left:0;margin-right:0;width:585;max-width:585px\">\n" +
                "                        <table align=\"center\" bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 auto;border-spacing:0;text-align:left;border:1px solid #cbcbcb;border-radius:10px;padding:20px;max-width:585px\" width=\"100%\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td>\n" +
                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"font-family:Arial,Helvetica,sans-serif;font-size:11px;line-height:13px;color:#333333;max-width:585px\">&nbsp;</td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td>\n" +
                "                                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td width=\"197\"><a href=\"http://www.balamenu.com/\" target=\"_blank\"><img alt=\"ُُbalamenu\" width=\"200\" src=\"http://wajdtuha.com/img/wjadtoha-removebg-preview.png\" style=\"display:block;border-width:0px;border-style:solid\"></a></td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td height=\"20\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td height=\"15\" style=\"font-size:12px;line-height:20px;padding:0;border-top:1px solid #dddddd;width:510px\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td align=\"center\" style=\"font-size:14px;line-height:26px;color:#111111;font-family:Helvetica,Arial,sans-serif\">\n" +
                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Helvetica,Arial,sans-serif;font-size:15px\" width=\"100%\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding-bottom:10px;padding-top:10px\"><strong>Dear " + restaurantUser.getFirstName() + " " + restaurantUser.getLastName() + " </strong>,\n" +
                "                                                        <p>Welcome to balamenu,<br/>\n" +
                "                                                        To sign in when visiting our site just click <a href=\"http://www.balamenu.com/\" target=\"_blank\">Login</a>, and then enter your credentials.</p>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<p>Use the following password when prompted to sign in</p>\n" +
                "                                                        <table style=\"width:100%;padding-bottom:15px;background-color: #f1f0f0;border:1px solid #cbcbcb;border-radius:10px\">\n" +
                "                                                            <tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "                                                                <td style=\"text-align:center;font-size:16px\">\n" +
                "                                                                    <span style=\"font-weight:bold;color:#999999\">Password:</span><strong>" + password + "</strong>\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "                                                    <p>Any doubts? Questions?</p>\n" +
                "                                                    <p>Send us an email to <a href=\"mailto:support@balamenu.com\" target=\"_blank\">support@balamenu.com</a> we will be happy to help.</p>\n" +
                "                                                    <p>Have a great trip!</p>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td height=\"10\" style=\"font-size:12px;line-height:10px;padding:0;border-top:1px solid #dddddd;max-width:510px;padding-top:15px\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td>\n" +
                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse\" width=\"100%\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td width=\"61\">\n" +
                "                                                        <img alt=\"-\" src=\"http://wajdtuha.com/img/wjadtoha-removebg-preview.png\" style=\"display:block;border-width:0px;border-style:solid\" width=\"61\" class=\"CToWUd\">\n" +
                "                                                    </td>\n" +
                "                                                    <td style=\"line-height:16px;color:#333333;font-family:Helvetica,Arial,sans-serif;color:#333333;font-size:13px\" width=\"228\">\n" +
                "                                                        <p>balamenu Team<br>\n" +
                "                                                            <a href=\"mailto:support@balamenu.com\" target=\"_blank\">support@balamenu.com</a>\n" +
                "                                                        </p>\n" +
                "                                                    </td>\n" +
                "                                                    <td width=\"285\">\n" +
                "                                                        <table align=\"right\" cellpadding=\"5\">\n" +
                "                                                            \n" +
                "                                                            \n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"background-color:#fafafa;min-width:10px\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"background-color:#fafafa;min-width:10px\">&nbsp;</td>\n" +
                "                    <td height=\"30\" style=\"text-align:left;width:585px;max-width:585px\" width=\"585\">\n" +
                "                        <p style=\"font-family:Arial,sans-serif;font-size:12px;color:rgb(100,100,100);line-height:18px;padding:0;font-family:sans-serif;text-align:left;padding-top:15px;padding-bottom:15px\"></p>\n" +
                "                    </td>\n" +
                "                    <td style=\"background-color:#fafafa;min-width:10px\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "    <img src=\"https://ci4.googleusercontent.com/proxy/LS3wlGf-vnu7FBuCXiku7COlyZtuF9Gi0Cb70nGI1pC3F29RaVMcuZaM2R8SHs977f-7sy28aUI9IkFnu7x6J71ikko5F21cJC1c6G9-XdosCSytUW3rHyB5wSw3Z5AH5a0f2z3qSxgI2Uolo8XCQVCz=s0-d-e1-ft#http://trans-backend.mdirector.com/pixels/open/2399041450/hash/92ca2721e7d518ab130ff6fef40da262\" style=\"display:none\" border=\"0\" width=\"1\" height=\"1\" alt=\"\" class=\"CToWUd\"><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "</div></div>\n" +
                "</body>\n" +
                "</html>";

        return emailTemp;
    }
}
