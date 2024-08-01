package ui.In;

import Model.Entities.Users.AccessLevels;
import Model.Exceptions.InvalidInputException;

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

    public static String validCommand(String input, String ... commands) throws InvalidInputException {
        if(input == null) throw new InvalidInputException();
        input = input.split(" ")[0];
        for(String validCommands : commands)
            if(validCommands.toLowerCase().startsWith(input.toLowerCase())) return validCommands;
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
