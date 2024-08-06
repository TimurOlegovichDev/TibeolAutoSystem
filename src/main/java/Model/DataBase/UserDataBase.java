package Model.DataBase;

import Model.Entities.Users.User;
import Model.Entities.Users.UserParameters;
import Model.Exceptions.UserExc.InvalidInputException;

import java.util.*;

public abstract class UserDataBase {

    private static final Map<Integer, User> userData = new HashMap<>();
    private static final Map<String, UserParameters> credentials = new HashMap<>();

    protected static Map<Integer, User> getUserData(){
        return new HashMap<>(userData);
    }

    protected static void add(User user){
        userData.put(user.getUserParameters().getID(), user);
        addCredentials(user);
    }

    protected static void remove(Integer id){
        removeCredentials(userData.get(id));
        userData.remove(id);
    }

    public static void updateName(String name, String newName) throws InvalidInputException {
        UserParameters parameters = credentials.remove(name);
        parameters.setName(newName);
        credentials.put(parameters.getName(), parameters);
    }

    private static void addCredentials(User user){
        credentials.put(user.getUserParameters().getName(), user.getUserParameters());
    }

    private static void removeCredentials(User user){
        credentials.remove(user.getUserParameters().getName(), user.getUserParameters());
    }

    public static Map<String, UserParameters> getCredentials(){
        return new HashMap<>(credentials);
    }

}
