package Controller.ActionHandlers;

import Model.DataBaseHandler;
import Model.Entities.Car.CarParameters;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidInputException;
import ui.Menu;
import ui.in.Validator;
import ui.messageSrc.Messages;
import ui.out.Printer;

import java.sql.SQLException;
import java.util.Objects;

import static Model.DataBaseHandler.executeQuery;

public abstract class ShowRoomMainActions {

    public static void getFilterList() {
        while(true) {
            String command = "";
            try {
                Printer.printCommandsWithCustomQuestion(new String[] {"Brand", "Model", "Color"}, "Enter the parameter by which the sorting will be done");
                command = Validator.validCommand(Menu.getInput(), "Brand", "Model", "Color");
                filterList(command);
                return;
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            } catch (DeliberateInterruptException e) {
                Printer.printCentered(Messages.RETURN.getMessage());
                return;
            } catch (Exception e) {
                Printer.printCentered(Messages.ERROR.getMessage());
            }
        }
    }

    private static void filterList(String command) throws DeliberateInterruptException, NullPointerException, SQLException {
        switch (Objects.requireNonNull(CarParameters.getCarParameterFromString(command))) {
            case BRAND -> {
                String filter = Menu.getText("Введите интересующую марку: ");
                Printer.print(executeQuery( "SELECT * FROM " + DataBaseHandler.dealerCarTableName +
                        " WHERE brand LIKE '" + filter + "%'"));
            }
            case MODEL -> {
                String filter = Menu.getText("Введите интересующую модель: ");
                Printer.print(executeQuery( "SELECT * FROM " + DataBaseHandler.dealerCarTableName +
                        " WHERE model LIKE '" + filter + "%'"));
            }
            case COLOR -> {
                String filter = Menu.getText("Введите интересующий цвет: ");
                Printer.print(executeQuery( "SELECT * FROM " + DataBaseHandler.dealerCarTableName +
                        " WHERE color LIKE '" + filter + "%'"));
            }
        }
    }
}