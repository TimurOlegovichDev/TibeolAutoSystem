package Model.Entities.Order;

import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/**
 * Класс заказа, он позволяет оформлять покупку иди обслуживание автомобиля,
 * Клиент способен создавать их, а менеджеры отвечать на них.
 * Все действия менеджеров на заказом дублируются в виде сообщений пользоватю.
 * @see Message
 * @see Client
 */

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

    /**
     * @param manager - используется для получения отправителя, чтобы пользователь знал, от кого пришел ответ
     * @param newStatus - новый статус, который будет установлен на заказе
     * @param isAutomatic - позволяет отключить выбрасывание отключений при автоматическом изменении статуса на "архивировано"
     * @see StatusesOfOrder
     */

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

    /**
     * Не позволяет вручную ставь статус заказа "акхивировано" или "новое", так как заказы пребывают в таком состоянии только после полного выполнения или наоборот, после самого отправления
     */

    private void checkStatus(StatusesOfOrder status, boolean isAutomatic) throws InvalidCommandException {

        if(!isAutomatic && (status.equals(StatusesOfOrder.NEW) || status.equals(StatusesOfOrder.ARCHIVED)))
            throw new InvalidCommandException();

    }

    @Override
    public String toString() {
        return "| Тип: " + getOrderType().toString() +
                " | Текст: " + getOrderText() +
                " | " + getStatus() +
                " | Получено: " + getDateOfCreation() + " | ";
    }

    public void notifyClient(Message message, Client client){
        client.receiveMessage(message);
    }
}
