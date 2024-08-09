package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;
import Model.DataBase.UsersDataFields;
import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Getter;
import ui.out.Printer;

import java.util.*;

/** Класс, обладающий обширными возможностями, такие как получение сообщений от менеджеров, оформление различных заказов.
 */

@Getter
public final class Client extends User {

    private final List<Order> orderList = new ArrayList<>();
    private final Map<Integer, Car> carData = new HashMap<>();
    private final List<Message> messages = new ArrayList<>();

    public Client(String name, byte[] password) {
        super(name, password);
        setAccessLevel(AccessLevels.CLIENT);
    }
    public Client(List<String> parameters) {
        super(parameters);
        setAccessLevel(AccessLevels.CLIENT);
    }

    @Override
    public void removeAccount() {
        for (Order order : orderList) {
            if (!order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                DataBaseHandler.remove(order);
        }
        DataBaseHandler.removeRowById(DataBaseHandler.usersTableName,getID());
        DataBaseHandler.removeRowById(DataBaseHandler.clientMessagesTableName, getID());
        DataBaseHandler.removeRowById(DataBaseHandler.ordersTableName, getID());
    }

    public void receiveMessage(Message message) {
        messages.add(message);
    }

    public void createServiceOrder(String text, int carId) {
        if (getCarByID(carId).isBooked())
            Printer.printCentered("На машину есть активный заказ, дождитесь его завершения и повторите попытку!");
        else {
            Order newOrder = new Order(OrderTypes.SERVICE, this, text, carData.get(carId));
            DataBaseHandler.add(newOrder);
            orderList.add(newOrder);
            carData.get(carId).setBooked(true);
        }
    }

    public void createPurchaseOrder(String text, int carId) {
        if (DataBaseHandler.getCarData().get(carId).isBooked())
            Printer.printCentered("Машина уже забронирована, попробуйте позже");
        else {
            Order newOrder = new Order(OrderTypes.PURCHASE, this, text, DataBaseHandler.getCarData().get(carId));
            DataBaseHandler.add(newOrder);
            orderList.add(newOrder);
            DataBaseHandler.getCarData().get(carId).setBooked(true);
        }
    }

    public Car getCarByID(int id) throws NoSuchElementException {
        return carData.get(id);
    }

    public void addCar(Car car) {
        carData.put(car.getID(), car);
    }

    public void buyCar(Car car) {
        carData.put(car.getID(), car);
        receiveMessage(new Message(null, "Поздравляем с приобретением автомобиля!"));
    }

    public void removeCar(Integer id) {
        if (!getCarByID(id).isBooked()) carData.remove(id);
        else Printer.printCentered("На машину есть активный заказ, дождитесь его завершения и повторите попытку!");
    }

    public void checkOrderToArchive() {
        for (Order order : orderList) {
            if (order.getStatus().equals(StatusesOfOrder.COMPLETED) ||
                    order.getStatus().equals(StatusesOfOrder.AGREED) ||
                    order.getStatus().equals(StatusesOfOrder.DISMISSED)) {

                try {
                    order.setStatus(null, StatusesOfOrder.ARCHIVED, true);
                    order.getCar().setBooked(false);
                } catch (InvalidCommandException ignored) {
                }

            }
        }
        orderList.removeIf(order -> order.getStatus().equals(StatusesOfOrder.ARCHIVED));
    }
}
