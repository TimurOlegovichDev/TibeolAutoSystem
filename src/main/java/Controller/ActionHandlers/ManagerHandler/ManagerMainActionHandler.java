package Controller.ActionHandlers.ManagerHandler;

import Controller.ActionHandlers.BaseActionsHandler;
import Controller.Controller;
import Model.Entities.Users.Manager;
import Model.Entities.Users.User;
import ui.messageSrc.commands.ManagerCommands;
import Controller.Scenes;

public abstract class ManagerMainActionHandler {

    public static void distribute(ManagerCommands action) {
        User currentUser = Controller.getController().getCurrentUser();
        switch (action) {
            case ORDERS -> ManagerActionsHandlerInOrders.gotoOrderPage();
            case EXIT_FROM_ACCOUNT -> Scenes.currentScene = Scenes.EXIT_FROM_ACCOUNT;
            case GO_TO_SHOWROOM -> ManagerActionsInShowRoom.gotoShowRoom((Manager) currentUser);
            case SETUP_MY_PROFILE -> BaseActionsHandler.setUpUserParameters(currentUser.getID());
            case DELETE_ACCOUNT -> BaseActionsHandler.removeAccount(currentUser);
        }
    }

}