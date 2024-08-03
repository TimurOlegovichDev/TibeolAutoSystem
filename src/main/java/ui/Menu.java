package ui;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.Car;
import Model.Entities.Car.CarParameters;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.DeliberateInterruptExeption;
import Model.Exceptions.UserExc.InvalidInputException;
import Model.UserManagement.Encryptor;
import ui.in.Validator;
import ui.messageSrc.commands.AdminCommands;
import ui.messageSrc.commands.ClientCommands;
import ui.messageSrc.commands.ManagerCommands;
import ui.out.Printer;
import ui.messageSrc.StringCommands;
import ui.messageSrc.Messages;

import java.util.Map;
import java.util.Scanner;

import static ui.in.Validator.validLevel;

public abstract class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void greeting() {
        Printer.print(Messages.GREETING.getMessage());
    }

    public static String chooseRegistrationOrAuth() {
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), StringCommands.REG_OR_AUTORIZE.getCommands());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AccessLevels chooseRole() {
        Printer.printAsIs(Messages.CHOOSE_ROLE.getMessage());
        while (true) {
            try {
                return validLevel(Validator.validCommand(scanner.nextLine(), StringCommands.ROLES.getCommands()));
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getUserName() {
        Printer.print(Messages.ENTER_NAME.getMessage());
        while (true) {
            try {
                return Validator.validName(scanner.nextLine());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static byte[] getUserPassword() {
        Printer.print(Messages.ENTER_PASSWORD.getMessage());
        while (true) {
            try {
                return Encryptor.encrypt(Validator.validPassword(scanner.nextLine()).getBytes());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            } catch (Exception e) {
                Printer.print(Messages.ERROR.getMessage());
            }
        }
    }

    public static ClientCommands clientChoosingAction() {
        Printer.printCommands(StringCommands.CLIENT_COMMANDS.getCommands());
        while (true) {
            try {
                return Validator.validClientAction(scanner.nextLine(), ClientCommands.values());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ClientCommands.CommandsInShowRoom clientChoosingActionInShowRoom() {
        Printer.printCommands(ClientCommands.CommandsInShowRoom.getStringArray());
        while (true) {
            try {
                return Validator.validClientInShowRoomAction(scanner.nextLine(), ClientCommands.CommandsInShowRoom.values());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ManagerCommands.CommandsInShowRoom managerChoosingActionInShowRoom() {
        Printer.printCommands(ManagerCommands.CommandsInShowRoom.getStringArray());
        while (true) {
            try {
                return Validator.validManagerInShowRoomAction(scanner.nextLine(), ManagerCommands.CommandsInShowRoom.values());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ManagerCommands managerChoosingAction() {
        Printer.printCommands(StringCommands.MANAGER_COMMANDS.getCommands());
        while (true) {
            try {
                return Validator.validManagerAction(scanner.nextLine(), ManagerCommands.values());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AdminCommands adminChoosingAction() {
        Printer.printCommands(StringCommands.ADMIN_COMMANDS.getCommands());
        while (true) {
            try {
                return Validator.validAdminAction(scanner.nextLine(), AdminCommands.values());
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static boolean areYouSure(String message) {
        Printer.print(message);
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), "ДА", "НЕТ").equals("ДА");
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static Car getCar(User user) throws DeliberateInterruptExeption {
        if(user.getAccessLevel().equals(AccessLevels.CLIENT))
            return createUserCar((Client) user);
        else
            return createDealerCar();
    }

    private static Car createUserCar(Client client) throws DeliberateInterruptExeption {
        return new Car(
                client,
                getBrand(),
                getModel(),
                getColor()
        );
    }

    private static Car createDealerCar() throws DeliberateInterruptExeption {
        return new Car(
                getBrand(),
                getModel(),
                getColor(),
                getYearOfProduction(),
                getPrice(),
                getMileAge()
        );
    }

    private static String getBrand() throws DeliberateInterruptExeption {
        Printer.print("Введите название марки:");
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 15);
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static String getModel() throws DeliberateInterruptExeption {
        Printer.print("Введите модель:");
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 15);
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static String getColor() throws DeliberateInterruptExeption {
        Printer.print("Введите цвет:");
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 15);
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static Integer getYearOfProduction() throws DeliberateInterruptExeption {
        Printer.print("Введите год производства:");
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), CarParameters.YEAR);
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static Integer getPrice() throws DeliberateInterruptExeption {
        Printer.print("Введите цену:");
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), CarParameters.PRICE);
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static Integer getMileAge() throws DeliberateInterruptExeption {
        Printer.print("Введите пробег:");
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), CarParameters.MILEAGE);
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static Integer getNumberGreaterZero(int maxValue) throws DeliberateInterruptExeption {
        Printer.print("Введите номер параметра, который вы хотите изменить: ");
        while (true) {
            try {
                int value =  Validator.validNumber(scanner.nextLine());
                if(value < maxValue && value > 0) return value;
            } catch (InvalidInputException e) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static PhoneNumber getUserPhoneNumber() {
        return null;
    }

    public static int tryGetNumberFromUser() throws InvalidInputException {
        return Validator.isNumber(scanner.nextLine());
    }
}
