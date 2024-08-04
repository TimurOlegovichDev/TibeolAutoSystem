package Model.Entities.Order;

import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Order {

    private final OrderTypes orderType;
    private final Client owner;
    private final Car car;
    private final Instant dateOfCreation;
    private StatusesOfOrder status = StatusesOfOrder.NEW;
    @Setter
    private String orderText;

    public Order(OrderTypes orderType,
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
                          StatusesOfOrder newStatus,
                          boolean isAutomatic
    ) throws InvalidCommandException {
        checkStatus(newStatus, isAutomatic);
        if(newStatus.equals(status)) return; // Чтобы не выполнять лишних действий, возвращаем управление в случае того же статуса
        status = newStatus;
        notifyClient(
                new Message(

                manager,

                "Статус заявки на " + orderType + " " + car.getBrand() + " " + car.getModel() +
                        " изменен на " + status + "!"),
                owner
        );
    }

    private void checkStatus(StatusesOfOrder status, boolean isAutomatic) throws InvalidCommandException {

        if(!isAutomatic && (status.equals(StatusesOfOrder.NEW) || status.equals(StatusesOfOrder.ARCHIVED)))
            throw new InvalidCommandException();

    }

    @Override
    public String toString() {
        return "| Тип: " + getOrderType().toString() +
                " | Текст: " + getOrderText() +
                " | " + getStatus() + " | ";
    }

    private void notifyClient(Message message, Client client){
        client.receiveMessage(message);
    }
}
