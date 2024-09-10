package Controller.ActionHandlers;

import Controller.Controller;
import Model.DataBaseHandler;
import Model.DataFields.UsersDataFields;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.*;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.out.Printer;
import Controller.Scenes;

/**
 * Является центральным хабом для обработки различных
 * действий и взаимодействий в системе управления автосалоном.
 * Он содержит множество статических методов, которые выполняют различные действия
 * по распределению действий между ролями.
 */

public abstract class BaseActionsHandler {

    public static void removeAccount(User user) {
        if (!Menu.areYouSure(Messages.DELETE_ACCOUNT_WARNING.getMessage())) return;
        user.removeAccount();
        Printer.print("Account deleted successfully.");
        Controller.logger.log(Levels.INFO, LogActions.USER_DELETE_ACCOUNT.getText() + user);
        Scenes.currentScene = Scenes.CHOOSING_ROLE;
    }

    public static void setUpUserParameters(int id) {
        try {
            switch (Menu.getSetParameterName(new String[]{"Имя", "Номер телефона"})) {
                case "Имя" -> setName(Menu.getUserName(), id);
                case "Номер телефона" -> setPhoneNumber(Menu.getUserPhoneNumber(), id);
            }
            Controller.logger.log(Levels.INFO, LogActions.USER_SETUP_PROFILE.getText() + id);
        } catch (DeliberateInterruptException ignored) {
            Printer.print(Messages.RETURN.getMessage());
        }
    }

    private static void setName(String userName, int id) {
        DataBaseHandler.setParameterById(
                UsersDataFields.NAME.getValue(),
                DataBaseHandler.usersTableName,
                userName,
                id
        );
    }

    private static void setPhoneNumber(String phoneNumber, int id) {
        String newPhoneNumber = String.format("%s (%s) %s-%s-%s",
                phoneNumber.charAt(0),
                phoneNumber.substring(1, 4),
                phoneNumber.substring(4, 7),
                phoneNumber.substring(7, 9),
                phoneNumber.substring(9));
        DataBaseHandler.setParameterById(
                UsersDataFields.PHONE_NUMBER.getValue(),
                DataBaseHandler.usersTableName,
                newPhoneNumber,
                id
        );
    }
}
