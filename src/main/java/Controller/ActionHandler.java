package Controller;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.Car;
import Model.Entities.Users.Client;
import Model.Entities.Users.Manager;
import Model.Entities.Users.User;
import Model.Exceptions.UserExc.DeliberateInterruptExeption;
import Model.Exceptions.UserExc.InvalidInputException;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.ClientCommands;
import ui.messageSrc.commands.ManagerCommands;
import ui.out.Printer;

public abstract class ActionHandler {

    static void viewUserOrders(Client client) {
        Printer.print(client.getOrderList());
    }

    static void viewUserCars(Client client) {
        Printer.print(client.getCarData());
    }

    static void addUserCar(Client client)  {
        try {client.addCar(Menu.getCar(client));}
        catch (DeliberateInterruptExeption ignored){
            Printer.print("Операция отменена");
        }
    }

    static Scenes removeAccount(User user){
        if(Menu.areYouSure(Messages.DELETE_ACCOUNT_WARNING.getMessage())) {
            user.removeAccount();
            return Scenes.ACTIONS;
        }
        return Scenes.CHOOSING_ROLE;
    }

    static Scenes setUpUserParameters(User user){
        try {
            switch (Menu.getNumberGreaterZero(2)) { // 3, Потому что пользователь может изменить только 3 параметра: имя, номер телефона
                case 1 -> user.setName(Menu.getUserName());
                case 2 -> user.setPhoneNumber(Menu.getUserPhoneNumber());
            }
        } catch (DeliberateInterruptExeption ignored){
            Printer.print(Messages.RETURN.getMessage());
        } catch (InvalidInputException e) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
        return Scenes.ACTIONS;
    }

    static void goToShowRoom(User user){
        Printer.print("Выполняется переход на страницу автосалона");
        ShowRoomActionsHandler.chooseAction(user);
    }

    static void viewUsers(){
        Printer.print(DataBaseHandler.getUserData());
    }

    static class ShowRoomActionsHandler {
        static void chooseAction(User user) {
            switch (user.getAccessLevel()){
                case CLIENT -> clientActionHandler((Client) user, Menu.clientChoosingActionInShowRoom());
                case MANAGER -> managerActionHandler((Manager) user, Menu.managerChoosingActionInShowRoom());
            }
        }
        private static void clientActionHandler(Client client, ClientCommands.CommandsInShowRoom command){
                switch (command) {
                    case VIEW_ALL_CARS -> Printer.print(DataBaseHandler.getCarData());
                    case CREATE_PURCHASE_ORDER -> {}
                    case CREATE_SERVICE_ORDER -> {}
                    case SEARCH_CAR -> {}
                    case BACK -> {
                        Printer.print("Возврат на предыдущую страницу");
                        return;
                    }
                }
                chooseAction(client);
        }

        private static void managerActionHandler(Manager manager, ManagerCommands.CommandsInShowRoom command)  {
            switch (command) {
                case VIEW_ALL_CARS -> Printer.print(DataBaseHandler.getCarData());
                case ADD_CAR -> addCar(manager);
                case REMOVE_CAR -> removeCar();
                case SEARCH_CAR -> {}
                case SETUP_CAR -> {}
                case BACK -> {
                    Printer.print("Возврат на предыдущую страницу");
                    return;
                }
            }
            chooseAction(manager);
        }

        private static void addCar(Manager manager){
            try {
                DataBaseHandler.add(Menu.getCar(manager));
            } catch (DeliberateInterruptExeption e){
                Printer.print(Messages.RETURN.getMessage());
            }
        }

        private static void removeCar(){
            try {
                int id = Menu.tryGetNumberFromUser();
                Car car = DataBaseHandler.getCar(id);
                DataBaseHandler.remove(car);
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
}
