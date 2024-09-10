package ui.in;

import Model.Entities.Car.CarParameters;
import Model.Entities.Users.AccessLevels;
import Model.Exceptions.UserExc.*;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.*;
import ui.out.Printer;
import java.io.File;
import java.time.Year;

/**
 * Самый главный класс для проверки данных, весь ввод пользователя проверяется с помощью этого класса. Он обладает множество перегруженных методов, для удобности пользования. Чаще всего, работает совмество с классом Menu
 * @see ui.Menu
 */

public abstract class Validator {

    public static String validName(String name) throws InvalidInputException {
        if (name == null || name.length() <= 8 || name.length() > 25)
            throw new InvalidInputException();
        return name;
    }

    public static String validPassword(String password) throws InvalidInputException {
        if (password == null || password.length() < 8 || password.length() > 50)
            throw new InvalidInputException();
        return password;
    }

    public static Integer validNumber(String input) throws InvalidInputException, DeliberateInterruptException {
        if("back".startsWith(input.toLowerCase())) throw new DeliberateInterruptException();
        return isNumber(input);
    }

    public static Integer isNumber(String input) throws InvalidInputException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e){
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
        throw new InvalidInputException();
    }

    public static Integer validIntCarParameter(String input, CarParameters carParameter) throws InvalidInputException, DeliberateInterruptException {
        if("back".startsWith(input.toLowerCase())) throw new DeliberateInterruptException();
        switch (carParameter){
            case MILEAGE, PRICE -> {
                try {
                    int value =  Integer.parseInt(input);
                    if(value >= 0) return value;
                } catch (NumberFormatException ignored){}
            }
            case YEAR -> {
                try {
                    int value =  Integer.parseInt(input);
                    if(value <= Year.now().getValue()) return value;
                } catch (NumberFormatException ignored){}
            }
        }
        throw new InvalidInputException();
    }

    public static String validLength(String input, int validLen) throws InvalidInputException, DeliberateInterruptException {
        if(input == null || input.length() > validLen) throw new InvalidInputException();
        if("back".startsWith(input.toLowerCase())) throw new DeliberateInterruptException();
        return input;
    }

    public static String validCommand(String input, String ... commands) throws InvalidInputException, DeliberateInterruptException {
        if(input == null) throw new InvalidInputException();
        if("back".startsWith(input.toLowerCase()) && input.length() <= 5) throw new DeliberateInterruptException();
        for(String validCommands : commands)
            if(validCommands.toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static ClientCommands validClientAction(String input, ClientCommands ... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for(ClientCommands validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static ClientCommands.CommandsInShowRoom validClientInShowRoomAction(String input, ClientCommands.CommandsInShowRoom ... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for( ClientCommands.CommandsInShowRoom validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static ManagerCommands validManagerAction(String input, ManagerCommands ... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for(ManagerCommands validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static ManagerCommands.CommandsInShowRoom validManagerInShowRoomAction(String input,
                                                                                  ManagerCommands.CommandsInShowRoom ... commands)
            throws InvalidInputException {

        if(input == null) throw new InvalidInputException();
        for( ManagerCommands.CommandsInShowRoom validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static ManagerCommands.CommandsInOrderList validManagerInOrderListAction(String input,
                                                                                    ManagerCommands.CommandsInOrderList[] commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for(ManagerCommands.CommandsInOrderList validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static AdminCommands.CommandsInUserList validAdminCommandInUserList(String input, AdminCommands.CommandsInUserList[] commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for(AdminCommands.CommandsInUserList validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static AdminCommands validAdminAction(String input, AdminCommands... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for(AdminCommands validCommands : commands)
            if(validCommands.getCommand().toLowerCase().startsWith(input.toLowerCase())) return validCommands;
        throw new InvalidInputException();
    }

    public static AccessLevels validLevel(String command) throws InvalidInputException {
        switch (command) {
            case "Client" -> {
                return AccessLevels.CLIENT;
            }
            case "Manager" -> {
                return AccessLevels.MANAGER;
            }
            case "Administrator" -> {
                return AccessLevels.ADMINISTRATOR;
            }
        }
        throw new InvalidInputException();
    }

    /**
     * Функция проверки валидности пути к файлу
     *
     * @param filePath путь к файлу
     * @return true, если путь к файлу валиден, false иначе
     */

    public static boolean isValidFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty())
            return false;
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }


}
