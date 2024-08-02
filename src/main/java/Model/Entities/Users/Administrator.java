package Model.Entities.Users;

public final class Administrator extends User{
    public Administrator(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.ADMINISTRATOR);
    }
}
