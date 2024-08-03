package Model.UserManagement;

import Model.DataBase.DataBaseHandler;
import Model.DataBase.UserDataBase;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.RegistrationInterruptException;


public class RegistrationManager {
    public static User registration(AccessLevels accessLevel, String name, byte[] password) throws RegistrationInterruptException {
        if(UserDataBase.getCredentials().containsKey(name)) throw new RegistrationInterruptException();
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
