package ui.in;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Служит для валидации российских номеров, можно вводить и с пробелами, и со скобками, и с тире
 */

public class PhoneNumberValidator {

    private static final String PHONE_NUMBER_PATTERN = "^((8|\\+7|7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{8,9}$$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "");
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}