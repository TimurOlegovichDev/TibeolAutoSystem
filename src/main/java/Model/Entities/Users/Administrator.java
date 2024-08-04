package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;

public final class Administrator extends User{
    public Administrator(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.ADMINISTRATOR);
    }

    @Override
    public void removeAccount() {
        DataBaseHandler.remove(this);
    }
}
