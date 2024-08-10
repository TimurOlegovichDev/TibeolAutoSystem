package Model.Entities.Users;

import Model.DataBase.DealerCarDataFields;
import Model.DataBase.DataBaseHandler;
import Model.DataBase.OrderDataFields;
import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Data;
import lombok.Getter;
import ui.out.Printer;

import java.sql.SQLException;
import java.util.*;

/**
 * Класс, обладающий обширными возможностями, такие как получение сообщений от менеджеров, оформление различных заказов.
 */

@Getter
public final class Client extends User {

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
        DataBaseHandler.removeRowById(DataBaseHandler.usersTableName, getID());
        DataBaseHandler.removeRowById(DataBaseHandler.clientMessagesTableName, getID());
        DataBaseHandler.removeRowById(DataBaseHandler.ordersTableName, getID());
    }

    public void receiveMessage(Message message) {
        //messages.add(message);
        //todo
    }

    public void createServiceOrder(String text, int carId) {
        Order newOrder = new Order(OrderTypes.SERVICE, this, text, carId);
        DataBaseHandler.add(newOrder);
    }

    private boolean carIsBook(int carId) {
        try {
            List<String> list = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.dealerCarTableName, carId);
            return Boolean.parseBoolean(list.get(DealerCarDataFields.BOOKED.getIndex()));
        } catch (SQLException | NoSuchElementException e) {
            Printer.print("Ошибочный запрос к базе данных");
        }
        return false;
    }

    public void createPurchaseOrder(String text, int carId) {
        if (carIsBook(carId))
            Printer.printCentered("Автомобиль уже заказан, выберите другой");
        else {
            Order newOrder = new Order(OrderTypes.PURCHASE, this, text, carId);
            DataBaseHandler.add(newOrder);
            DataBaseHandler.setParameterById(
                    DealerCarDataFields.BOOKED.toString(),
                    DataBaseHandler.dealerCarTableName,
                    "true",
                    carId
            );
        }
    }

    public void addCar(Car car) {
        DataBaseHandler.addClientCar(car);
    }

    public void buyCar(Car car) {
        DataBaseHandler.addClientCar(car);
        DataBaseHandler.removeRowById(
                DataBaseHandler.dealerCarTableName,
                car.getID()
        );
        receiveMessage(new Message(null, "Поздравляем с приобретением автомобиля!"));
    }

    public void removeCar(int id) {
        if(
                DataBaseHandler
                        .getActiveOrders()
                        .get(OrderDataFields.CLIENT_CAR_ID.getIndex())
                        .contains(id)
        ){
            DataBaseHandler.removeRowById(DataBaseHandler.clientsCarTableName, id);
        }
        else Printer.printCentered("На машину есть активный заказ, дождитесь его завершения и повторите попытку!");
    }
}
