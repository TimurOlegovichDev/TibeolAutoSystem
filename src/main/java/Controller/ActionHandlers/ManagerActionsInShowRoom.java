package Controller.ActionHandlers;

import Controller.Controller;
import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.Car;
import Model.Entities.Users.Manager;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.out.Printer;

import java.util.NoSuchElementException;

public class ManagerActionsInShowRoom {

    static void gotoShowRoom(Manager manager){
        Printer.printCentered("Go to the car dealership page");
        distribute();
    }

    protected static void distribute() {
        switch (Menu.managerChoosingActionInShowRoom()) {
            case VIEW_ALL_CARS -> Printer.print(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
            case ADD_CAR -> addCar((Manager) Controller.getController().getCurrentUser());
            case REMOVE_CAR -> removeCar();
            case SEARCH_CAR -> ShowRoomMainActions.getFilterList();
            case BACK -> {
                Printer.printCentered("Return to previous page");
                return;
            }
        }
        distribute();
    }

    private static void addCar(Manager manager) {
        try {
            Car car = Menu.getCar(manager);
            DataBaseHandler.addDealerCar(car);
            Controller.logger.log(Levels.INFO, LogActions.NEW_CAR_IN_DEALER.getText());
        } catch (DeliberateInterruptException e) {
            Printer.print(Messages.RETURN.getMessage());
        }
    }

    private static void removeCar() {
        try {
            Printer.print(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
            Printer.print("Введите ID автомобиля подлежащего удалению (для отмены введите любое слово): ");
            int id = Menu.tryGetNumberFromUser();
            if (!Menu.areYouSure("Вы точно хотите удалить? (Да/Нет) ")) return;
            DataBaseHandler.removeRowById(DataBaseHandler.dealerCarTableName, id);
            Controller.logger.log(Levels.INFO, LogActions.CAR_DELETED.getText());
        } catch (NoSuchElementException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }
}
