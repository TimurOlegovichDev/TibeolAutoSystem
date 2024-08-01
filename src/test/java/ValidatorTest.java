import Model.Exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import ui.In.Validator;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
    @Test
    public void testNameInput() {
        String[] input = new String[]{
                "Name",
                "123456789101112131415161718",
                "null",
                "",
                null,
        };
        for(String i : input){
                try {
                    assertThrows(InvalidInputException.class,() -> Validator.validName(i));
                } catch (Exception ignored){}
        }

    }
    @Test
    public void testPassInput() {
        String[] input = new String[]{
                "passwor",
                "12345678910111213141516dsadsadadsadsdasdsdasadsadadasdasdadsdad1718",
                "null",
                "",
                null,
        };
        for(String i : input){
            try {
                assertThrows(InvalidInputException.class,() -> Validator.validPassword(i));
            } catch (Exception ignored){}
        }
    }
    @Test
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
        for(String i : input){
            try {
                assertThrows(InvalidInputException.class,() -> Validator.validCommand(i));
            } catch (Exception ignored){}
        }
    }
}
