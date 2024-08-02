package Model.UserManagement;

import Model.DataBase.UserData;
import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.User;
import Model.Entities.Users.UserParameters;
import Model.Exceptions.InvalidPasswordException;
import Model.Exceptions.NoSuchUserException;

import java.util.Arrays;

public abstract class AuthenticationManager {
    public static User authentication(String name, byte[] cryptoPass) throws InvalidPasswordException, NoSuchUserException {
        if(!UserData.getCredentials().containsKey(name))
            throw new NoSuchUserException();
        if(!Arrays.equals(UserData.getCredentials().get(name).getPassword(), cryptoPass))
            throw new InvalidPasswordException();
        return AuthorizationManager.authorization(UserData.getCredentials().get(name));
    }


    abstract static class AuthorizationManager {
        public static User authorization(UserParameters userParameters){
            return UserData.getUserData().get(userParameters.getID());
        }
    }
}
