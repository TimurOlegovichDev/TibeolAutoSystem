package Model.Entities.Users;

import Model.UserManagement.Encryptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

/**
 * Хранит в себе основные методы и поля пользователей, номер телефона по умолчанию "не указан", чтобы избежать NPE, так как администратору он не требуется для создания аккаунта
 */

@Getter
public abstract class User {

    @Setter
    private String phoneNumber = "Не указан";

    @Setter
    private AccessLevels accessLevel;
    /**
     * Уникальный номер каждого пользователя.
     */
    private int ID = 0;
    /**
     * Пароль пользователя, который шифруется с помощью {@link Encryptor}.
     */
    private byte[] password;

    @Setter
    private String name;

    User(String name, byte[] password){
        this.name = name;
        setPassword(password);
    }

    public void setID(int ID) {
        if(ID == 0)
            this.ID = ID;
    }

    public abstract void removeAccount();

    public void setPassword(byte[] password) {
        try {
            this.password = Encryptor.encrypt(password);
        } catch (Exception e) {
            System.out.print("Encrypt error!");
            this.password = String.valueOf(Objects.hashCode(password)).getBytes();
        }
    }

    @Override
    public String toString() {
        return "| ID: " + ID +
                " | Имя: " + name +
                " | Контактный номер: " + phoneNumber +
                " | Роль: " + getAccessLevel() + " | ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID && accessLevel == user.accessLevel && Objects.deepEquals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessLevel, ID, Arrays.hashCode(password));
    }
}
