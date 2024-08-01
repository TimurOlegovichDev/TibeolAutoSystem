package Model.Entities.Users;

public final class Administrator extends User{
    Administrator(String name, String password){
        super(name, password);
        setAccessLevel(AccessLevels.ADMINISTRATOR);
    }
}
