package Model.Entities.Users;

import Model.Entities.Order.Order;
import Model.Entities.Order.StatusesOfOrder;
import Model.Exceptions.InvalidCommandException;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Setter
public final class Manager extends User{

    @Nullable
    private PhoneNumber phoneNumber;

    public Manager(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.MANAGER);
    }

    public void changeOrderStatus(@NotNull Order order,
                                  StatusesOfOrder newStatus) throws InvalidCommandException {
        order.setStatus(this, newStatus);
    }
}
