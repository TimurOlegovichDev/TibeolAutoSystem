import Model.Exceptions.UserExc.NoSuchUserException;
import Model.UserManagement.AuthenticationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationManagerTest {

    @Test
    @DisplayName("Проверка на вход не существующего пользователя")
    public void testAuthenticationNoSuchUser() {
        String name = "nonExistentUser";
        byte[] cryptoPass = "testPassword".getBytes();
        assertThrows(NoSuchUserException.class, () -> AuthenticationManager.authentication(name, cryptoPass));
    }
}