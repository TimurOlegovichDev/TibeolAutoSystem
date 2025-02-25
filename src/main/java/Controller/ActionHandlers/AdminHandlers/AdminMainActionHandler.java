package Controller.ActionHandlers.AdminHandlers;

import Controller.ActionHandlers.BaseActionsHandler;
import Controller.Controller;
import Controller.Scenes;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.AdminCommands;
import ui.out.Printer;

public abstract class AdminMainActionHandler {

    public static void distribute(AdminCommands action) {
        Scenes.currentScene = Scenes.ACTIONS;
        switch (action) {
            case GO_TO_USER_LIST -> AdminActionsHandlerInUsersList.gotoUserListPage();
            case GET_LOG_LIST -> getLogList();
            case SAVE_LOG_LIST -> saveLogList();
            case DELETE_ACCOUNT -> BaseActionsHandler.removeAccount(Controller.getController().getCurrentUser());
            case SETUP_MY_PROFILE -> BaseActionsHandler.setUpUserParameters(Controller.getController().getCurrentUser().getID());
            case EXIT_FROM_ACCOUNT -> Scenes.currentScene = Scenes.EXIT_FROM_ACCOUNT;
            case SHUT_DOWN -> Scenes.currentScene = Scenes.SHUT_DOWN;
        }
    }

    public static void getLogList() {
        Printer.printCentered("Activity logs list: ");
        Printer.print(Controller.logger.getDataList());
    }

    public static void saveLogList() {
        Printer.printCentered("Save log list!");
        try {
            Controller.logger.saveLogsToFile(Menu.getPath());
        } catch (DeliberateInterruptException e) {
            Printer.print(Messages.RETURN.getMessage());
        }
    }

}