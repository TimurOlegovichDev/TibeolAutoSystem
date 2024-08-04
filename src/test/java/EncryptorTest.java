import Model.UserManagement.Encryptor;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptorTest {

    @Test
    public void testEncryptSuccessful() throws Exception {

        String password = "testPassword";
        byte[] passwordBytes = password.getBytes();

        byte[] encryptedPassword = Encryptor.encrypt(passwordBytes);

        assertNotNull(encryptedPassword);
        assertNotEquals(passwordBytes, encryptedPassword);
    }

    @Test
    public void testEncryptSamePasswordSameResult() throws Exception {

        String password = "testPassword";
        byte[] passwordBytes = password.getBytes();

        byte[] encryptedPassword1 = Encryptor.encrypt(passwordBytes);
        byte[] encryptedPassword2 = Encryptor.encrypt(passwordBytes);

        assertArrayEquals(encryptedPassword1, encryptedPassword2);
    }

    @Test
    public void testEncryptDifferentPasswordsDifferentResults() throws Exception {

        String password1 = "testPassword1";
        String password2 = "testPassword2";
        byte[] passwordBytes1 = password1.getBytes();
        byte[] passwordBytes2 = password2.getBytes();

        byte[] encryptedPassword1 = Encryptor.encrypt(passwordBytes1);
        byte[] encryptedPassword2 = Encryptor.encrypt(passwordBytes2);

        assertNotEquals(encryptedPassword1, encryptedPassword2);
    }

    @Test
    public void testPrepareSecreteKeySuccessful() {
        String myKey = "testKey";
        Encryptor.prepareSecreteKey(myKey);
        assertNotNull(Encryptor.secretKey);
    }

    @Test
    public void testPrepareSecreteKeySameKeySameResult() {
        String myKey = "testKey";
        Encryptor.prepareSecreteKey(myKey);
        SecretKeySpec secretKey1 = Encryptor.secretKey;
        Encryptor.prepareSecreteKey(myKey);
        SecretKeySpec secretKey2 = Encryptor.secretKey;
        assertEquals(secretKey1, secretKey2);
    }
}