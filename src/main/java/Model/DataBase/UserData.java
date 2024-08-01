package Model.DataBase;

import Model.Entities.Users.User;
import Model.Entities.Users.UserParameters;

import java.util.*;

public abstract class UserData {

    private static final Map<Integer, User> userData = new HashMap<>();
    private static final Map<String, UserParameters> credentials = new HashMap<>();

    public static Map<Integer, User> getUserData(){
        return new HashMap<>(userData);
    }

    public static void add(User user){
        userData.put(user.getUserParameters().getId(), user);
        addCredentials(user);
    }

    public static void remove(Integer id){
        removeCredentials(userData.get(id));
        userData.remove(id);
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

    public static void print(){
        for(Map.Entry<Integer, User> entry : userData.entrySet()){
            System.out.println(entry.getKey() +
                    " " + entry.getValue().getUserParameters().getName() +
                    " " + entry.getValue().getUserParameters().getPassword() +
                    " " + entry.getValue().getAccessLevel());
        }
    }
}
