import Model.DataBase.UserDataBase;
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
        String name = "testClient";
        byte[] password = "testPassword".getBytes();
        User registeredUser = RegistrationManager.registration(AccessLevels.CLIENT, name, password);
        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getUserParameters().getName());
        assertInstanceOf(Client.class, registeredUser);
    }

    @Test
    @DisplayName("Проверка регистрации менеджера")
    public void testRegistrationSuccessfulManager() throws UserAlreadyExistsException, RegistrationInterruptException {
        String name = "testManager";
        byte[] password = "testPassword".getBytes();
        User registeredUser = RegistrationManager.registration(AccessLevels.MANAGER, name, password);
        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getUserParameters().getName());
        assertTrue(registeredUser instanceof Manager);
    }

    @Test
    @DisplayName("Проверка регистрации администратора")
    public void testRegistrationSuccessfulAdministrator() throws UserAlreadyExistsException, RegistrationInterruptException {
        String name = "testAdministrator";
        byte[] password = "testPassword".getBytes();
        User registeredUser = RegistrationManager.registration(AccessLevels.ADMINISTRATOR, name, password);
        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getUserParameters().getName());
        assertInstanceOf(Administrator.class, registeredUser);
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