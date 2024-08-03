package ui.in;

import Model.Entities.Car.Car;
import Model.Entities.Car.CarParameters;
import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import Model.Exceptions.UserExc.DeliberateInterruptExeption;
import Model.Exceptions.UserExc.InvalidInputException;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.AdminCommands;
import ui.messageSrc.commands.ClientCommands;
import ui.messageSrc.commands.ManagerCommands;
import ui.out.Printer;

import java.time.Instant;
import java.time.Year;

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

    public static Integer validNumber(String input) throws InvalidInputException, DeliberateInterruptExeption {
        if("назад".startsWith(input.toLowerCase())) throw new DeliberateInterruptExeption();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e){
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
        throw new InvalidInputException();
    }

    public static Integer isNumber(String input) throws InvalidInputException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e){
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
        throw new InvalidInputException();
    }

    public static Integer validIntCarParameter(String input, CarParameters carParameter) throws InvalidInputException, DeliberateInterruptExeption {
        if("назад".startsWith(input.toLowerCase())) throw new DeliberateInterruptExeption();
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

    public static String validLength(String input, int validLen) throws InvalidInputException {
        if(input == null || input.length() > validLen) throw new InvalidInputException();
        return input;
    }

    public static String validCommand(String input, String ... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
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

    public static ManagerCommands.CommandsInShowRoom validManagerInShowRoomAction(String input, ManagerCommands.CommandsInShowRoom ... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        for( ManagerCommands.CommandsInShowRoom validCommands : commands)
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
            case "Клиент" -> {
                return AccessLevels.CLIENT;
            }
            case "Менеджер" -> {
                return AccessLevels.MANAGER;
            }
            case "Администратор" -> {
                return AccessLevels.ADMINISTRATOR;
            }
        }
        throw new InvalidInputException();
    }
}
