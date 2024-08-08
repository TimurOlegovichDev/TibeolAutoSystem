package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;
import Model.UserManagement.Encryptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

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
    private final int ID = 1;

    /**
     * Пароль пользователя, который шифруется с помощью {@link Encryptor}.
     */

    private byte[] password;
    private String name;


    public UserParameters(String name,
                          byte[] password) {
        setPassword(password);
        this.name = name;
    }

    private UserParameters(){}

    public void setPassword(byte[] password) {
        try {
            this.password = Encryptor.encrypt(password);
        } catch (Exception e) {
            System.out.print("Encrypt error!");
            this.password = String.valueOf(Objects.hashCode(password)).getBytes();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserParameters that = (UserParameters) o;
        return ID == that.ID && Objects.deepEquals(password, that.password) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, Arrays.hashCode(password));
    }
}
