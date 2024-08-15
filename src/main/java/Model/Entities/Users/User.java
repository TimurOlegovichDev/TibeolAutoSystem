package Model.Entities.Users;

import Model.DataBaseHandler;
import Model.DataFields.UsersDataFields;
import Model.UserManagement.Encryptor;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Хранит в себе основные методы и поля пользователей, номер телефона по умолчанию "не указан", чтобы избежать NPE, так как администратору он не требуется для создания аккаунта
 */

@Getter
public abstract class User {

    @Setter
    private String name;

    User(String name, byte[] password){
        this.name = name;
        setPassword(password);
    }

    User(List<String> parametersFromDB){
        this.ID = Integer.parseInt(parametersFromDB.get(UsersDataFields.ID.getIndex()));
        this.name = parametersFromDB.get(UsersDataFields.NAME.getIndex());
        this.password = parametersFromDB.get(UsersDataFields.PASSWORD.getIndex()).getBytes(StandardCharsets.UTF_8);
        this.phoneNumber = parametersFromDB.get(UsersDataFields.PHONE_NUMBER.getIndex());
    }

    private String phoneNumber = "Не указан";

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = String.format("%s (%s) %s-%s-%s",
                phoneNumber.charAt(0),
                phoneNumber.substring(1, 4),
                phoneNumber.substring(4, 7),
                phoneNumber.substring(7, 9),
                phoneNumber.substring(9));
        DataBaseHandler.setParameterById(
                UsersDataFields.PHONE_NUMBER.getValue(),
                DataBaseHandler.usersTableName,
                this.phoneNumber,
                ID
        );
    }

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

    public void setPassword(byte[] password) {
        try {
            this.password = Encryptor.encrypt(password);
        } catch (Exception e) {
            System.out.print("Encrypt error!");
            this.password = String.valueOf(Objects.hashCode(password)).getBytes();
        }
    }

    public void setID(int id) {
        if(ID == 0)
            this.ID = id;
    }

    public abstract void removeAccount();

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
