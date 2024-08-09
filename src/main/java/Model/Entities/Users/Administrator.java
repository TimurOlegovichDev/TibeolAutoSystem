package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;

import java.util.List;

public final class Administrator extends User{
    public Administrator(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.ADMINISTRATOR);
    }

    public Administrator(List<String> parameters) {
        super(parameters);
        setAccessLevel(AccessLevels.ADMINISTRATOR);
    }

    @Override
    public void removeAccount() {
        DataBaseHandler.removeRowById(DataBaseHandler.usersTableName,getID());
    }
}
