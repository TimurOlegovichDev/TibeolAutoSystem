package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Order.Order;
import Model.Entities.Order.StatusesOfOrder;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Setter
public final class Manager extends User{

    @Nullable
    private String phoneNumber;

    public Manager(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.MANAGER);
    }

    @Override
    public void removeAccount() {
        DataBaseHandler.remove(this);
    }
}
