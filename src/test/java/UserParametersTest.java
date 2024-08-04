import Model.Entities.Users.UserParameters;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserParametersTest {

    @Test
    public void testConstructorSuccessful() {
        String name = "testUser";
        byte[] password = "testPassword".getBytes();
        UserParameters userParameters = new UserParameters(name, password);
        assertNotNull(userParameters);
        assertEquals(name, userParameters.getName());
        assertNotNull(userParameters.getPassword());
    }

    @Test
    public void testSetPasswordSuccessful() throws Exception {
        String name = "testUser";
        byte[] password = "testPassword".getBytes();
        UserParameters userParameters = new UserParameters(name, password);
        userParameters.setPassword(password);
        assertNotNull(userParameters.getPassword());
        assertNotEquals(password, userParameters.getPassword());
    }


    @Test
    public void testEqualsDifferentUsers() {
        String name1 = "testUser1";
        String name2 = "testUser2";
        byte[] password = "testPassword".getBytes();
        UserParameters userParameters1 = new UserParameters(name1, password);
        UserParameters userParameters2 = new UserParameters(name2, password);
        assertFalse(userParameters1.equals(userParameters2));
    }

}