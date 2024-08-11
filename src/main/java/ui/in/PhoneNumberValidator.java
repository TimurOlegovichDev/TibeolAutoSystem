package ui.in;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Служит для валидации российских номеров 11 цифр
 */

public class PhoneNumberValidator {

    private static final String PHONE_NUMBER_PATTERN = "\\d{11}";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "");
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}