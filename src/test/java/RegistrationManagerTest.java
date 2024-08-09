import Model.Entities.Users.*;
import Model.Exceptions.UserExc.RegistrationInterruptException;
import Model.Exceptions.UserExc.UserAlreadyExistsException;
import Model.UserManagement.RegistrationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationManagerTest {

    @Test
    @DisplayName("Проверка регистрации клиента")
    public void testRegistrationSuccessfulClient() throws UserAlreadyExistsException, RegistrationInterruptException {

    }

    @Test
    @DisplayName("Проверка регистрации менеджера")
    public void testRegistrationSuccessfulManager() throws UserAlreadyExistsException, RegistrationInterruptException {

    }

    @Test
    @DisplayName("Проверка регистрации администратора")
    public void testRegistrationSuccessfulAdministrator() throws UserAlreadyExistsException, RegistrationInterruptException {

    }

    @Test
    @DisplayName("Тестирование выбрасывания исключения при существующем пользователе")
    public void testRegistrationUserAlreadyExists() throws UserAlreadyExistsException, RegistrationInterruptException {
        String name = "existingUser";
        byte[] password = "testPassword".getBytes();
        RegistrationManager.registration(AccessLevels.CLIENT, name, password);
        assertThrows(UserAlreadyExistsException.class, () -> RegistrationManager.registration(AccessLevels.CLIENT, name, password));
    }

    @Test
    @DisplayName("Тестирование выбрасывания исключения при неверной регистрации пользователе")
    public void testRegistrationInterruptException() {
        String name = "testUser";
        byte[] password = "testPassword".getBytes();
        assertThrows(RegistrationInterruptException.class, () -> RegistrationManager.registration(null, name, password));
    }
}