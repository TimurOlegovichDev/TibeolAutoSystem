package Model.UserManagement;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public abstract class Encryptor {
    /**
     * Шифрует данные с помощью алгоритма SHA-256.
     *
     * @param arg пароль для генерации ключа шифрования
     * @return шифрованные данные в формате Base64
     * @throws Exception если произошла ошибка при шифровании
     */
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";

    public static void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException ignored) {}
    }

    public static byte[] encrypt(byte[] password) throws Exception {
        prepareSecreteKey("4S$eJ#8dLpR3aGfN2mB");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encode(cipher.doFinal(password));
    }
}
