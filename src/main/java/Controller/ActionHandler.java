package Controller;

import Model.DataBase.DataManager;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import ui.Menu;
import ui.in.Validator;
import ui.messageSrc.commands.ClientCommands;
import ui.out.Printer;

public abstract class ActionHandler {

    static void viewUserOrders(Client client) {
        Printer.print(client.getOrderList());
    }

    static void viewUserCars(Client client) {
        Printer.print(client.getCarList());
    }

    static void goToShowRoom(User user){
        Printer.print("Выполняется переход на страницу автосалона");
        ShowRoomActionsHandler.chooseAction(user);
    }

    static void viewUsers(User user){
        Printer.print(DataManager.getUserData());
    }

    static class ShowRoomActionsHandler {
        static void chooseAction(User user){
            switch (user.getAccessLevel()){
                case CLIENT -> clientActionHandler((Client) user, Menu.clientChoosingActionInShowRoom());
            }
        }
        private static void clientActionHandler(Client client, ClientCommands.CommandsInShowRoom command){
                switch (command) {
                    case VIEW_ALL_CARS -> Printer.print(DataManager.getCarData());
                    case BACK -> {
                        Printer.print("Возврат на предыдущую страницу");
                        return;
                    }
                }
                chooseAction(client);
        }
    }
}
