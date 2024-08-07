import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.in.Validator;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
    @Test
    @DisplayName("Тестирование валидации имени")
    public void testNameInput() {
        String[] input = new String[]{
                "Name",
                "123456789101112131415161718",
                "null",
                "",
                null,
        };
        for(String i : input)
            assertThrows(InvalidInputException.class,() -> Validator.validName(i));
    }
    @Test
    @DisplayName("Тестирование валидации пароля")
    public void testPassInput() {
        String[] input = new String[]{
                "passwor",
                "12345678910111213141516dsadsadadsadsdasdsdasadsadadasdasdadsdad1718",
                "null",
                "",
                null,
        };
        for(String i : input)
            assertThrows(InvalidInputException.class,() -> Validator.validPassword(i));
    }
    @Test
    @DisplayName("Тестирование ошибки выбора роли")
    public void testCommandInputOnStart() {
        String[] input = new String[]{
                "",
                "Aдминистраторы",
                "манагеры",
                "ва",
                "",
                "-",
                "asdas",
                "Клиетны",
                null,
        };
        for(String i : input)
            assertThrows(Exception.class,() -> Validator.validCommand(i));
    }
}
