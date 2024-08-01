package Model.Entities.Users;

import org.jetbrains.annotations.NotNull;

public final class Administrator extends User{
    public Administrator(String name, String password){
        super(name, password);
        setAccessLevel(AccessLevels.ADMINISTRATOR);
    }
}
