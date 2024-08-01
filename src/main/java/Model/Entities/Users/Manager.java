package Model.Entities.Users;

import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Order.StatusesOfOrder;
import Model.Exceptions.InvalidCommandException;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public final class Manager extends User{
    Manager(String name, String password){
        super(name, password);
        setAccessLevel(AccessLevels.MANAGER);
    }

    public void changeOrderStatus(@NotNull Order order,
                                  StatusesOfOrder newStatus) throws InvalidCommandException {
        order.setStatus(this, newStatus);
    }
}
