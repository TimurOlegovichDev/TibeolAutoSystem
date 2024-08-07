import Model.UserManagement.Encryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptorTest {

    @Test
    @DisplayName("Проверка что шифратор не возвращает тот же массив")
    public void testEncryptSuccessful() throws Exception {
        String password = "testPassword";
        byte[] passwordBytes = password.getBytes();
        byte[] encryptedPassword = Encryptor.encrypt(passwordBytes);
        assertNotNull(encryptedPassword);
        assertNotEquals(passwordBytes, encryptedPassword);
    }

    @Test
    @DisplayName("Проверка шифратора на шифровку")
    public void testEncryptSamePasswordSameResult() throws Exception {
        String password = "testPassword";
        byte[] passwordBytes = password.getBytes();
        byte[] encryptedPassword1 = Encryptor.encrypt(passwordBytes);
        byte[] encryptedPassword2 = Encryptor.encrypt(passwordBytes);
        assertArrayEquals(encryptedPassword1, encryptedPassword2);
    }

    @Test
    @DisplayName("Проверка шифратора на шифровку различных паролей")
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
    @DisplayName("Проверка на изменение секретного ключа")
    public void testPrepareSecreteKeySuccessful() {
        String myKey = "testKey";
        Encryptor.prepareSecreteKey(myKey);
        assertNotNull(Encryptor.secretKey);
    }
}