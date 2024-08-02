package Model.UserManagement;

import Model.DataBase.DataManager;
import Model.DataBase.UserData;
import Model.Entities.Users.*;
import Model.Exceptions.RegistrationInterruptException;


public class RegistrationManager {
    public static User registration(AccessLevels accessLevel, String name, byte[] password) throws RegistrationInterruptException {
        if(UserData.getCredentials().containsKey(name)) throw new RegistrationInterruptException();
        switch (accessLevel){
            case CLIENT -> {
                return DataManager.add(new Client(name, password));
            }
            case MANAGER -> {
                return DataManager.add(new Manager(name, password));
            }
            case ADMINISTRATOR -> {
                return DataManager.add(new Administrator(name, password));
            }
            default -> throw new RegistrationInterruptException();
        }
    }
}
