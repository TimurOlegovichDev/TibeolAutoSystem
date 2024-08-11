package Model.UserManagement;

import Model.DataBase.DataBaseHandler;
import Model.DataBase.DataFields.UsersDataFields;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.RegistrationInterruptException;
import Model.Exceptions.UserExc.UserAlreadyExistsException;

import java.sql.SQLException;

/**
 * Служит для регистрации новых пользователей, он проверяет наличие имени в базе данных, пароль также передается уже зашифрованным
 */

public class RegistrationManager {
    public static User registration(AccessLevels accessLevel, String name, byte[] password) throws UserAlreadyExistsException, RegistrationInterruptException {
        try {
            if(DataBaseHandler.getColumnByField(DataBaseHandler.usersTableName, UsersDataFields.NAME).contains(name))
                throw new UserAlreadyExistsException();
        } catch (SQLException e) {
            throw new RegistrationInterruptException();
        }
        switch (accessLevel){
            case CLIENT -> {
                return DataBaseHandler.add(new Client(name, password));
            }
            case MANAGER -> {
                return DataBaseHandler.add(new Manager(name, password));
            }
            case ADMINISTRATOR -> {
                return DataBaseHandler.add(new Administrator(name, password));
            }
            default -> throw new RegistrationInterruptException();
        }
    }
}
