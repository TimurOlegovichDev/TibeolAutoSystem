package Model.UserManagement;

import Model.DataBase.DataBaseHandler;
import Model.DataBase.UsersDataFields;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.InvalidPasswordException;
import Model.Exceptions.UserExc.NoSuchUserException;
import Model.Exceptions.UserExc.RegistrationInterruptException;

import java.sql.SQLException;
import java.util.List;

/**
 * Данный класс служит для входа в систему, он сравнивает введенные данные с существующими. Пароль передается уже зашифрованным
 */

public abstract class AuthenticationManager {
    public static User authentication(String name, byte[] cryptoPass) throws InvalidPasswordException, NoSuchUserException, SQLException {
        List<List<String>> table = DataBaseHandler.getUsersData();
        if(table == null)
            throw new SQLException();
        return AuthorizationManager.authorization(name, cryptoPass,  getUserIfExists(name, cryptoPass, table));
    }


    protected abstract static class AuthorizationManager {
        public static User authorization(String name, byte[] cryptoPass, List<String> parameters) throws NoSuchUserException {
            switch (AccessLevels.valueOf(parameters.get(UsersDataFields.ROLE.getIndex()))){
                case CLIENT -> {
                    return new Client(name, cryptoPass);
                }
                case MANAGER -> {
                    return new Manager(name, cryptoPass);
                }
                case ADMINISTRATOR -> {
                    return new Administrator(name, cryptoPass);
                }
                default -> throw new NoSuchUserException();
            }
        }
    }

    private static List<String> getUserIfExists(String name, byte[] cryptoPass, List<List<String>> table) throws InvalidPasswordException, NoSuchUserException {
        for(List<String> list :  table) {
            if(list.contains(name))
                if (list.contains(bytesToString(cryptoPass)))
                    return list;
                else
                    throw new InvalidPasswordException();
        }
        throw new NoSuchUserException();
    }

    private static String bytesToString(byte[] bytes){
        return new String(bytes);
    }
}
