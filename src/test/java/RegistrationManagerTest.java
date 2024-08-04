import Model.DataBase.UserDataBase;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.RegistrationInterruptException;
import Model.Exceptions.UserExc.UserAlreadyExistsException;
import Model.UserManagement.RegistrationManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationManagerTest {

    @Test
    public void testRegistrationSuccessfulClient() throws UserAlreadyExistsException, RegistrationInterruptException {

        String name = "testClient";
        byte[] password = "testPassword".getBytes();
        User registeredUser = RegistrationManager.registration(AccessLevels.CLIENT, name, password);

        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getUserParameters().getName());
        assertTrue(registeredUser instanceof Client);
    }

    @Test
    public void testRegistrationSuccessfulManager() throws UserAlreadyExistsException, RegistrationInterruptException {
        String name = "testManager";
        byte[] password = "testPassword".getBytes();
        User registeredUser = RegistrationManager.registration(AccessLevels.MANAGER, name, password);
        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getUserParameters().getName());
        assertTrue(registeredUser instanceof Manager);
    }

    @Test
    public void testRegistrationSuccessfulAdministrator() throws UserAlreadyExistsException, RegistrationInterruptException {
        String name = "testAdministrator";
        byte[] password = "testPassword".getBytes();

        User registeredUser = RegistrationManager.registration(AccessLevels.ADMINISTRATOR, name, password);
        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getUserParameters().getName());
        assertTrue(registeredUser instanceof Administrator);
    }

    @Test
    public void testRegistrationUserAlreadyExists() throws UserAlreadyExistsException, RegistrationInterruptException {
        String name = "existingUser";
        byte[] password = "testPassword".getBytes();
        RegistrationManager.registration(AccessLevels.CLIENT, name, password);
        assertThrows(UserAlreadyExistsException.class, () -> RegistrationManager.registration(AccessLevels.CLIENT, name, password));
    }

    @Test
    public void testRegistrationInterruptException() {

        String name = "testUser";
        byte[] password = "testPassword".getBytes();

        assertThrows(RegistrationInterruptException.class, () -> RegistrationManager.registration(null, name, password));
    }
}