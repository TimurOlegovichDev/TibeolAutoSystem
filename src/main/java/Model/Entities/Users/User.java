package Model.Entities.Users;

import Model.DataBase.UserDataBase;
import Model.Exceptions.UserExc.InvalidInputException;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

/**
 * Хранит в себе основные методы и поля пользователей, номер телефона не указан, чтобы избежать NPE, так как администратору он не требуется для создания аккаунта
 */

@Getter
public abstract class User {

    private final UserParameters userParameters;
    @Setter
    private String phoneNumber = "Не указан";
    @Setter
    private AccessLevels accessLevel;
    User(String name, byte[] password){
        this.userParameters = new UserParameters(name, password);
    }

    public void setName(String newName) throws InvalidInputException {
        UserDataBase.updateName(this.getUserParameters().getName(), newName);
        this.getUserParameters().setName(newName);
    }

    public abstract void removeAccount();

    @Override
    public String toString() {
        return "| ID: " + userParameters.getID() +
                " | Имя: " + userParameters.getName() +
                " | Контактный номер: " + getPhoneNumber() +
                " | Роль: " + getAccessLevel().toString() + " | ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userParameters, user.userParameters) && accessLevel == user.accessLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userParameters, accessLevel);
    }
}
