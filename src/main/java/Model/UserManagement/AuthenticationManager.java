package Model.UserManagement;

import Model.DataBase.DataBaseHandler;
import Model.DataBase.UserDataBase;
import Model.Entities.Users.User;
import Model.Entities.Users.UserParameters;
import Model.Exceptions.UserExc.InvalidPasswordException;
import Model.Exceptions.UserExc.NoSuchUserException;

import java.util.Arrays;

/**
 * Данный класс служит для входа в систему, он сравнивает введенные данные с существующими. Пароль передается уже зашифрованным
 */

public abstract class AuthenticationManager {
    public static User authentication(String name, byte[] cryptoPass) throws InvalidPasswordException, NoSuchUserException {
        if(!UserDataBase.getCredentials().containsKey(name))
            throw new NoSuchUserException();
        if(!Arrays.equals(UserDataBase.getCredentials().get(name).getPassword(), cryptoPass))
            throw new InvalidPasswordException();
        return AuthorizationManager.authorization(UserDataBase.getCredentials().get(name));
    }


    protected abstract static class AuthorizationManager {
        public static User authorization(UserParameters userParameters){
            return DataBaseHandler.getUserData().get(userParameters.getID());
        }
    }
}
