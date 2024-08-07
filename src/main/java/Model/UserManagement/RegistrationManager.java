package Model.UserManagement;

import Model.DataBase.DataBaseHandler;
import Model.DataBase.UserDataBase;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.RegistrationInterruptException;
import Model.Exceptions.UserExc.UserAlreadyExistsException;

/**
 * Служит для регистрации новых пользователей, он проверяет наличие имени в базе данных, пароль также передается уже зашифрованным
 */

public class RegistrationManager {
    public static User registration(AccessLevels accessLevel, String name, byte[] password) throws UserAlreadyExistsException, RegistrationInterruptException {
        if(UserDataBase.getCredentials().containsKey(name))
            throw new UserAlreadyExistsException();
        if(accessLevel == null || name == null || password == null)
            throw new RegistrationInterruptException();
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
