package Model.Entities.Users;

import Model.DataFields.DealerCarDataFields;
import Model.DataBaseHandler;
import Model.DataFields.OrderDataFields;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import lombok.Getter;
import ui.messageSrc.Messages;
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
        try {
            List<String> bookedCarId = DataBaseHandler.getColumnByField(DataBaseHandler.ordersTableName, OrderDataFields.CLIENT_CAR_ID, " WHERE type = 'Покупку' AND status != 'Архивировано'");
            for(String id : bookedCarId)
                DataBaseHandler.setParameterById(DealerCarDataFields.BOOKED.getValue(), DataBaseHandler.dealerCarTableName, "false", Integer.parseInt(id));
        } catch (SQLException e) {
            Printer.printCentered(Messages.ERROR.getMessage());
        }
        DataBaseHandler.removeRowByQuery(DataBaseHandler.ordersTableName, "WHERE client_id = " + getID());
        DataBaseHandler.removeRowByQuery(DataBaseHandler.clientsCarTableName, "WHERE client_id = " + getID());
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
        DataBaseHandler.addClientCar(car, getID());
    }

    public void removeCar(int id) throws SQLException {
        if(
                !DataBaseHandler
                        .getColumnByField(DataBaseHandler.ordersTableName, OrderDataFields.CLIENT_CAR_ID,"WHERE status != 'Архивировано'")
                        .contains(String.valueOf(id))
        ){
            DataBaseHandler.removeRowById(DataBaseHandler.clientsCarTableName, id);
            Printer.printCentered("Автомобиль успешно был удален!");
        }
        else Printer.printCentered("На машину есть активный заказ, дождитесь его завершения и повторите попытку!");
    }
}
