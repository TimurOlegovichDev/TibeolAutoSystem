package Model.Entities.Order;

import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Users.Client;
import Model.Entities.Users.Manager;
import Model.Entities.Users.User;
import Model.Exceptions.InvalidCommandException;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Order {

    private final OrderTypes orderType;
    private final UUID ID = UUID.randomUUID();
    private final Client owner;
    private final Car car;
    private final Instant dateOfCreation;
    private StatusesOfOrder status = StatusesOfOrder.NEW;
    @Setter
    private String orderText;

    Order(OrderTypes orderType,
          Client owner,
          String orderText,
          Car car){

        this.orderType = orderType;
        this.owner = owner;
        this.orderText = orderText;
        this.car = car;
        dateOfCreation = Instant.now();

    }

    public void setStatus(Manager manager,
                          StatusesOfOrder newStatus) throws InvalidCommandException {
        checkStatus(newStatus);
        if(newStatus.equals(status)) return; // Чтобы не выполнять лишних действий, возвращаем управление в случае того же статуса
        status = newStatus;
        notifyClient(
                new Message(

                manager,

                "Статус заявки с идентификационным номером " +
                        ID + "\n"
                        + "насчет \"" + orderType + car.getBrand() +
                        " изменен на " + status + "!"),
                owner
        );
    }

    private void checkStatus(StatusesOfOrder status) throws InvalidCommandException {

        if(status.equals(StatusesOfOrder.NEW) || status.equals(StatusesOfOrder.ARCHIVED))
            throw new InvalidCommandException();

    }

    private void notifyClient(Message message, Client client){
        client.receiveMessage(message);
    }
}
