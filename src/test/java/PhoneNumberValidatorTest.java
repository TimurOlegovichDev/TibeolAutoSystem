import Model.Exceptions.UserExc.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.in.PhoneNumberValidator;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberValidatorTest {

    @Test
    void testValidPhoneNumber() {
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("+7 (123) 456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7 (123) 456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("8 (123) 456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7 123 456 78 90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("8 123 456 78 90"));
    }

    @Test
    void testInvalidPhoneNumber() {
        assertFalse(PhoneNumberValidator.isValidPhoneNumber("(123) 4567890"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("123-456-7890"));
        assertFalse(PhoneNumberValidator.isValidPhoneNumber("123.456.7890"));
        assertFalse(PhoneNumberValidator.isValidPhoneNumber("abcdefg"));
        assertFalse(PhoneNumberValidator.isValidPhoneNumber("1234567"));
    }

    @Test
    void testPhoneNumberWithSpaces() {
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7 123 456 78 90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("8 123 456 78 90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7 123 4567 890"));
    }

    @Test
    void testPhoneNumberWithDashes() {
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7-123-456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("8-123-456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7-123-4567-890"));
    }

    @Test
    void testPhoneNumberWithParentheses() {
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("+7 (123) 456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7 (123) 456-78-90"));
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("7 (123) 4567-890"));
    }
}
