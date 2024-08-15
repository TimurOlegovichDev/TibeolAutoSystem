package Controller.ActionHandlers;

import Controller.Controller;
import Controller.Scenes;
import Model.DataBase.DataBaseHandler;
import Model.DataBase.DataFields.UsersDataFields;
import Model.Entities.Users.Administrator;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidInputException;
import Model.Exceptions.UserExc.NoSuchUserException;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.in.Validator;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.AdminCommands;
import ui.out.Printer;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static Model.DataBase.DataBaseHandler.executeQuery;

public class AdminActionsHandlerInUsersList {

    private Scenes adminUserListActionHandler(AdminCommands action) {
        Scenes nextScene = Scenes.ACTIONS;
        switch (action) {
//            case GO_TO_USER_LIST ->  BaseActionsHandler.gotoUserListPage((Administrator) currentUser);
//            case GET_LOG_LIST -> BaseActionsHandler.getLogList();
//            case SAVE_LOG_LIST -> BaseActionsHandler.saveLogList();
//            case DELETE_ACCOUNT -> nextScene = BaseActionsHandler.removeAccount(currentUser);
//            case SETUP_MY_PROFILE -> nextScene = BaseActionsHandler.setUpUserParameters(currentUser.getID());
            case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
            case SHUT_DOWN -> nextScene = Scenes.SHUT_DOWN;
        }
        return nextScene;
    }
    protected static void adminActionHandler(Administrator administrator, AdminCommands.CommandsInUserList command) {
        switch (command) {
            case USER_LIST -> Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
            case GET_FILTER_LIST -> getFilterList();
            case SET_USER_PARAM -> setUserParameters(administrator);
            case DELETE_USER -> deleteUser(administrator);
            case BACK -> {
                Printer.printCentered("Возврат на предыдущую страницу");
                return;
            }
        }
        adminActionHandler(administrator, Menu.adminChoosingActionInUserList());
    }

    private static void getFilterList() {
        while(true) {
            String command = "";
            try {
                command = Validator.validCommand(Menu.getText(
                                """
                                Введите параметр, по которому будет отсортирован список:            \s
                                - Имя
                                - Статус"""),
                        "Имя", "Статус", "Номер телефона");
                filterList(command);
                return;
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            } catch (Exception e) {
                Printer.printCentered(Messages.ERROR.getMessage());
            }
        }
    }

    private static void filterList(String command) throws DeliberateInterruptException, SQLException {
        switch (command) {
            case "Имя" -> {
                String filter = Menu.getText("Введите имя (или его начало) для сортировки: ");
                Printer.print(executeQuery(
                        "SELECT * FROM " + DataBaseHandler.usersTableName +
                                " WHERE name LIKE '" + filter + "%'"
                ));
            }
            case "Статус" -> {
                String filter = Menu.getText("Введите статус пользователя (или его начало) для сортировки: ");
                Printer.print(executeQuery(
                        "SELECT * FROM " + DataBaseHandler.usersTableName +
                                " WHERE role LIKE '" + filter + "%'"
                ));
            }
        }
    }

    private static void setUserParameters(Administrator administrator) {
        try {
            Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
            Printer.print("Введите ID пользователя, параметры которого желаете изменить (для отмены введите любое слово): ");
            int id = Menu.tryGetNumberFromUser();
            if (id == administrator.getID()) {
                Printer.printCentered("Нельзя выбрать свой профиль в этом действии!");
                return;
            } else if (!Menu.areYouSure("Вы выбрали пользователя -> " +
                    DataBaseHandler.getUserParamById(id).get(UsersDataFields.NAME.getIndex()) +
                    "? (Да/Нет) ")) return;
            BaseActionsHandler.setUpUserParameters(id);
            Controller.logger.log(Levels.INFO, LogActions.USER_SETUP_PROFILE.getText() + id);
        } catch (NoSuchElementException | NoSuchUserException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }

    private static void deleteUser(Administrator administrator) {
        try {
            Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
            Printer.print("Введите ID пользователя, которого желаете удалить (для отмены введите любое слово): ");
            int id = Menu.tryGetNumberFromUser();
            if (id == administrator.getID()) {
                Printer.printCentered("Нельзя выбрать свой профиль в этом действии!");
                return;
            }
            if (!Menu.areYouSure("Вы точно хотите удалить пользователя " +
                    DataBaseHandler.getUserParamById(id).get(UsersDataFields.NAME.getIndex()) +
                    "? (Да/Нет) "))
                return;
            DataBaseHandler.removeRowById(DataBaseHandler.usersTableName, id);
            Printer.printCentered("Аккаунт удален");
            Controller.logger.log(Levels.INFO, LogActions.USER_DELETE_ACCOUNT.getText() + id);
        } catch (NoSuchElementException | NoSuchUserException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }
}
