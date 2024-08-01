package Model.Entities.Users;

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
public class UserParameters extends UserData {
    /**
     * Уникальный номер каждого пользователя.
     */
    private final int id = Id.getUniqueId(UserData.getUserData());

    /**
     * Пароль пользователя, который шифруется с помощью {@link Encryptor}.
     */
    private String password;
    private String name;

    public UserParameters(String name,
                          String password) {
        try {
            this.password = Encryptor.encrypt(password);
        } catch (Exception e) {
            System.out.print("Encrypt error!");
            this.password = String.valueOf(Objects.hashCode(password));
        }
        this.name = name;
    }

    private UserParameters(){}

    public void setPassword(String password) {
        try {
            this.password = Encryptor.encrypt(password);
        } catch (Exception e) {
            System.out.print("Encrypt error!");
            this.password = String.valueOf(Objects.hashCode(password));
        }
    }

}
