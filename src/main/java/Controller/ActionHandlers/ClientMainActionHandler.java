package Controller.ActionHandlers;

import Controller.Controller;
import Model.DataBase.DataBaseHandler;
import Model.DataBase.DataFields.ClientCarDataFields;
import Model.DataBase.DataFields.OrderDataFields;
import Model.Entities.Car.Car;
import Model.Entities.Users.Client;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidInputException;
import Model.LoggerUtil.Levels;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.ClientCommands;
import ui.out.Printer;
import Controller.Scenes;
import Model.Entities.Users.User;
import java.util.NoSuchElementException;

public abstract class ClientMainActionHandler {

    public static void distribute(ClientCommands action) {
        User currentUser = Controller.getController().getCurrentUser();
        switch (action) {
            case VIEW_ORDERS -> viewUserOrders((Client) currentUser);
            case VIEW_USER_CARS -> viewUserCars((Client) currentUser);
            case ADD_USER_CAR -> addUserCar((Client) currentUser);
            case REMOVE_USER_CAR -> removeUserCar((Client) currentUser);
            case GO_TO_SHOWROOM -> ClientActionsInShowRoomHandler.gotoShowRoom((Client) currentUser);
            case SETUP_MY_PROFILE -> BaseActionsHandler.setUpUserParameters(currentUser.getID());
            case EXIT_FROM_ACCOUNT -> Controller.currentScene = Scenes.EXIT_FROM_ACCOUNT;
            case DELETE_ACCOUNT -> BaseActionsHandler.removeAccount(currentUser);
        }
    }

    static void viewUserOrders(Client client) {
        Printer.print(DataBaseHandler.getTableByFieldAndValue(
                DataBaseHandler.ordersTableName,
                client.getID(),
                OrderDataFields.CLIENT_ID
        ));
        DataBaseHandler.checkOrderAndArchive();
        Controller.logger.log(Levels.INFO, "Пользователь просматривает свои заказы");
    }

    static void viewUserCars(Client client) {
        Printer.print(DataBaseHandler.getTableByFieldAndValue(
                DataBaseHandler.clientsCarTableName,
                client.getID(),
                ClientCarDataFields.CLIENT_ID
        ));
        Controller.logger.log(Levels.INFO, "Пользователь просматривает свои автомобили");
    }

    static void addUserCar(Client client) {
        try {
            Car car = Menu.getCar(client);
            client.addCar(car);
            Controller.logger.log(Levels.INFO, "Пользователь добавил автомобиль -> " + car.getBrand());
        } catch (DeliberateInterruptException ignored) {
            Printer.print("Операция отменена");
        }
    }

    static void removeUserCar(Client client) {
        try {
            viewUserCars(client);
            int id = Menu.tryGetNumberFromUser();
            if (!DataBaseHandler.getColumnByField(DataBaseHandler.clientsCarTableName, ClientCarDataFields.ID, "WHERE client_id = " + client.getID()).contains(String.valueOf(id)))
                throw new NoSuchCarException();
            if (!Menu.areYouSure("Вы точно хотите удалить автомобиль? (Да/Нет) "))
                return;
            client.removeCar(id);
            Controller.logger.log(Levels.INFO, "Пользователь удалил автомобиль с id = " + id);
        } catch (NoSuchElementException | NoSuchCarException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (InvalidInputException e) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        } catch (Exception e) {
            Printer.print(Messages.ERROR.getMessage() + e.getMessage());
        }
    }
}