package ui;

import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.PhoneNumber;
import Model.Exceptions.InvalidInputException;
import Model.UserManagement.Encryptor;
import ui.in.Validator;
import ui.out.Printer;
import ui.messageSrc.StringCommands;
import ui.messageSrc.Messages;

import java.util.Scanner;

import static ui.in.Validator.validLevel;

public abstract class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void greeting(){
        Printer.print(Messages.GREETING.getMessage());
    }

    public static String chooseRegistrationOrAuth() {
        while (true){
            try {
                return Validator.validCommand(scanner.nextLine(), StringCommands.REG_OR_AUTORIZE.getCommands());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AccessLevels chooseRole() {
        Printer.print(Messages.CHOOSE_ROLE.getMessage());
        while (true){
            try {
                return validLevel(Validator.validCommand(scanner.nextLine(), StringCommands.ROLES.getCommands()));
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

    public static byte[] getUserPassword() {
        Printer.print(Messages.ENTER_PASSWORD.getMessage());
        while(true){
            try{
                return Encryptor.encrypt(Validator.validPassword(scanner.nextLine()).getBytes());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            } catch (Exception e) {
                Printer.print(Messages.ERROR.getMessage());
            }
        }
    }

    public static String clientChoosingAction(){
        Printer.printCommands(StringCommands.CLIENT_COMMANDS.getCommands());
        while(true){
            try{
                return Validator.validCommand(scanner.nextLine(), StringCommands.CLIENT_COMMANDS.getCommands());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String managerChoosingAction(){
        Printer.printCommands(StringCommands.MANAGER_COMMANDS.getCommands());
        while(true){
            try{
                return Validator.validCommand(scanner.nextLine(), StringCommands.MANAGER_COMMANDS.getCommands());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String adminChoosingAction(){
        Printer.printCommands(StringCommands.ADMIN_COMMANDS.getCommands());
        while(true){
            try{
                return Validator.validCommand(scanner.nextLine(), StringCommands.ADMIN_COMMANDS.getCommands());
            } catch (InvalidInputException e){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static PhoneNumber getUserPhoneNumber(){
        return null;
    }
}
