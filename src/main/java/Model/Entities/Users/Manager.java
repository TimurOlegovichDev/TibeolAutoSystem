package Model.Entities.Users;

import Model.DataBaseHandler;
import lombok.Setter;

import java.util.List;

@Setter
public final class Manager extends User{

    public Manager(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.MANAGER);
    }

    public Manager(List<String> parameters) {
        super(parameters);
        setAccessLevel(AccessLevels.MANAGER);
    }

    @Override
    public void removeAccount() {
        DataBaseHandler.removeRowById(DataBaseHandler.usersTableName,getID());
    }
}
