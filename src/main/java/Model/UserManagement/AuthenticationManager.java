package Model.UserManagement;

import Model.DataBaseHandler;
import Model.DataFields.UsersDataFields;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.InvalidPasswordException;
import Model.Exceptions.UserExc.NoSuchUserException;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Данный класс служит для входа в систему, он сравнивает введенные данные с существующими. Пароль передается уже зашифрованным
 */

public abstract class AuthenticationManager {
    public static User authentication(String name, byte[] cryptoPass) throws InvalidPasswordException, NoSuchUserException {
        List<List<String>> table = DataBaseHandler.getData(DataBaseHandler.usersTableName);
        return AuthorizationManager.authorization(name, cryptoPass,  getUserParamIfExists(name, cryptoPass, table));
    }


    protected abstract static class AuthorizationManager {
        public static User authorization(String name, byte[] cryptoPass, List<String> parameters) throws NoSuchUserException {
            switch (AccessLevels.getAccessLevelByValue(parameters.get(UsersDataFields.ROLE.getIndex()))){
                case CLIENT -> {
                    return new Client(parameters);
                }
                case MANAGER -> {
                    return new Manager(parameters);
                }
                case ADMINISTRATOR -> {
                    return new Administrator(parameters);
                }
                default -> throw new NoSuchUserException();
            }
        }
    }

    private static List<String> getUserParamIfExists(String name, byte[] cryptoPass, List<List<String>> table) throws InvalidPasswordException, NoSuchUserException {
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
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
