package Model.Entities.Order;

import Model.Entities.Users.*;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/**
 * Класс заказа, он позволяет оформлять покупку иди обслуживание автомобиля,
 * Клиент способен создавать их, а менеджеры отвечать на них.
 * Все действия менеджеров на заказом дублируются в виде сообщений пользоватю.
 * @see Client
 */

@Getter
public class Order {

    @Getter
    private final OrderTypes orderType;
    private final Client owner;
    private final int carId;
    private final Instant dateOfCreation;
    private StatusesOfOrder status = StatusesOfOrder.NEW;
    @Setter
    private String orderText;

    public Order(OrderTypes orderType,
          Client owner,
          String orderText,
          int carId){

        this.orderType = orderType;
        this.owner = owner;
        this.orderText = orderText;
        this.carId = carId;
        dateOfCreation = Instant.now();
    }

    @Override
    public String toString() {
        return "| Тип: " + getOrderType().toString() +
                " | Текст: " + getOrderText() +
                " | " + getStatus() +
                " | Получено: " + getDateOfCreation() + " | ";
    }
}
