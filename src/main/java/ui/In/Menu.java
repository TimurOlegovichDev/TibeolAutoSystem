package ui.In;

import Model.Entities.Users.AccessLevels;
import Model.Exceptions.InvalidInputException;
import ui.Out.Printer;
import ui.StringSRC.Commands;
import ui.StringSRC.Messages;

import java.util.Scanner;

import static ui.In.Validator.validLevel;

public abstract class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void greeting(){
        Printer.print(Messages.GREETING.getMessage());
    }

    public static String chooseActionToStart() {
        while (true){
            try {
                return Validator.validCommand(scanner.nextLine(),Commands.REG_OR_AUTORIZE.getCommands());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AccessLevels chooseRole() {
        Printer.print(Messages.CHOOSE_ROLE.getMessage());
        while (true){
            try {
                return validLevel(Validator.validCommand(scanner.nextLine(), Commands.ROLES.getCommands()));
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    private static AccessLevels getAccessLvl(String level){
        return AccessLevels.valueOf(level);
    }

    public static String getUserName() {
        Printer.print(Messages.ENTER_NAME.getMessage());
        while(true){
            try{
                return Validator.validName(scanner.nextLine());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getUserPassword() {
        Printer.print(Messages.ENTER_PASSWORD.getMessage());
        while(true){
            try{
                return Validator.validPassword(scanner.nextLine());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
}
