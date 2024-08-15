package Controller.ActionHandlers.AdminHandlers;

import Controller.ActionHandlers.BaseActionsHandler;
import Controller.Controller;
import Model.DataBaseHandler;
import Model.DataFields.UsersDataFields;
import Model.Entities.Users.Administrator;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidInputException;
import Model.Exceptions.UserExc.NoSuchUserException;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.in.Validator;
import ui.messageSrc.Messages;
import ui.out.Printer;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static Model.DataBaseHandler.executeQuery;

public class AdminActionsHandlerInUsersList {

    public static void gotoUserListPage() {
        Printer.printCentered("Welcome to the Admin List");
        distribute();
    }

    private static void distribute() {
        switch (Menu.adminChoosingActionInUserList()) {
            case USER_LIST -> Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
            case GET_FILTER_LIST -> getFilterList();
            case SET_USER_PARAM -> setUserParameters((Administrator) Controller.getController().getCurrentUser());
            case DELETE_USER -> deleteUser((Administrator) Controller.getController().getCurrentUser());
            case BACK -> {
                Printer.printCentered("Return to previous page");
                return;
            }
        }
        distribute();
    }

    private static void getFilterList() {
        while(true) {
            String command = "";
            try {
                command = Validator.validCommand(Menu.getText(
                                """
                                Enter parameter to sort list:            \s
                                - Name
                                - Status"""),
                        "Name", "Status");
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
            case "Name" -> {
                String filter = Menu.getText("Enter name to sort: ");
                Printer.print(executeQuery(
                        "SELECT * FROM " + DataBaseHandler.usersTableName +
                                " WHERE name LIKE '" + filter + "%'"
                ));
            }
            case "Status" -> {
                String filter = Menu.getText("Enter status to sort: ");
                Printer.print(executeQuery(
                        "SELECT * FROM " + DataBaseHandler.usersTableName +
                                " WHERE role LIKE '" + filter + "%'"
                ));
            }
        }
    }

    private static void setUserParameters(Administrator administrator) {
        try {
            int id = getAnotherUserId(administrator.getID());
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
            int id = getAnotherUserId(administrator.getID());
            DataBaseHandler.removeRowById(DataBaseHandler.usersTableName, id);
            Printer.printCentered("Account deleted successfully");
            Controller.logger.log(Levels.INFO, LogActions.USER_DELETE_ACCOUNT.getText() + id);
        } catch (NoSuchElementException | NoSuchUserException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }

    private static int getAnotherUserId(int currentId) throws InvalidInputException, SQLException, NoSuchUserException, DeliberateInterruptException {
        Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
        Printer.print("Enter user ID whose parameters you want to change (to cancel, enter any word): ");
        int id = Menu.tryGetNumberFromUser();
        if (id == currentId) {
            Printer.printCentered("You cannot select your profile!");
            throw new InvalidInputException();
        }
        if (!Menu.areYouSure("You have selected a user -> " +
                DataBaseHandler.getUserParamById(id).get(UsersDataFields.NAME.getIndex()) +
                "? (Yes/No) "
        ))
            throw new DeliberateInterruptException();
        return id;
    }
}

