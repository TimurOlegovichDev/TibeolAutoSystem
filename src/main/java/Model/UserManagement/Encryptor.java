package Model.UserManagement;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class Encryptor {
    /**
     * Шифрует данные с помощью алгоритма SHA-256.
     * @param arg пароль для генерации ключа шифрования
     * @return шифрованные данные в формате Base64
     * @throws Exception если произошла ошибка при шифровании
     */
    public static String encrypt(String arg) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("SHA-256");
        keyGen.init(128); // 128-битный ключ
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keyGen.generateKey());
        byte[] encryptedData = cipher.doFinal(arg.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
}
