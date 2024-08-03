package Model.Entities.Users;

import Model.DataBase.DataManager;
import Model.DataBase.UserData;
import Model.UserManagement.Encryptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * Класс для хранения данных пользователя, таких как имя, пароль и идентификационный номер.
 *
 * @see #ID
 * @see #password
 */
@Getter
@Setter
public class UserParameters {
    /**
     * Уникальный номер каждого пользователя.
     */
    private final int ID = Id.getUniqueId(DataManager.getUserData());

    /**
     * Пароль пользователя, который шифруется с помощью {@link Encryptor}.
     */
    private byte[] password;
    private String name;

    UserParameters(String name,
                          byte[] password) {
        setPassword(password);
        this.name = name;
    }

    private UserParameters(){}

    void setPassword(byte[] password) {
        try {
            this.password = Encryptor.encrypt(password);
        } catch (Exception e) {
            System.out.print("Encrypt error!");
            this.password = String.valueOf(Objects.hashCode(password)).getBytes();
        }
    }

}
