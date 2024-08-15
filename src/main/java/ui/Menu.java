package ui;

import Model.Entities.Car.*;
import Model.Entities.Order.*;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.*;
import Model.UserManagement.Encryptor;
import org.jetbrains.annotations.NotNull;
import ui.in.PhoneNumberValidator;
import ui.in.Validator;
import ui.messageSrc.commands.*;
import ui.out.Printer;
import ui.messageSrc.*;
import java.util.Scanner;
import static ui.in.Validator.validLevel;

/**
 * Главный класс взаимодействия пользователя с программой, именно с помощью интерфейса меню происходит отправка ввода в логику программы и отправка вывода для пользователя.
 * В нем содежится большое количество методов для взаимодействия с пользователем и получения от него информации
 */

public abstract class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void greeting() {
        Printer.print(Messages.GREETING.getMessage());
    }

    public static String chooseRegistrationOrAuth() {
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), StringCommands.REG_OR_AUTHORIZE.getCommands());
            } catch (InvalidInputException | DeliberateInterruptException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AccessLevels chooseRole() {
        Printer.printAsIs(Messages.CHOOSE_ROLE.getMessage());
        while (true) {
            try {
                return validLevel(Validator.validCommand(scanner.nextLine(), StringCommands.ROLES.getCommands()));
            } catch (InvalidInputException | DeliberateInterruptException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getUserName() {
        Printer.print(Messages.ENTER_NAME.getMessage());
        while (true) {
            try {
                return Validator.validName(scanner.nextLine());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
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
                Printer.printCentered(Messages.ERROR.getMessage());
            }
        }
    }

    public static ClientCommands clientChoosingAction() {
        Printer.printCommands(StringCommands.CLIENT_COMMANDS.getCommands());
        while (true) {
            try {
                return Validator.validClientAction(scanner.nextLine(), ClientCommands.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ClientCommands.CommandsInShowRoom clientChoosingActionInShowRoom() {
        Printer.printCommands(ClientCommands.CommandsInShowRoom.getStringArray());
        while (true) {
            try {
                return Validator.validClientInShowRoomAction(scanner.nextLine(), ClientCommands.CommandsInShowRoom.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ManagerCommands.CommandsInShowRoom managerChoosingActionInShowRoom() {
        Printer.printCommands(ManagerCommands.CommandsInShowRoom.getStringArray());
        while (true) {
            try {
                return Validator.validManagerInShowRoomAction(scanner.nextLine(), ManagerCommands.CommandsInShowRoom.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ManagerCommands.CommandsInOrderList managerChoosingActionInOrderList() {
        Printer.printCommands(ManagerCommands.CommandsInOrderList.getStringArray());
        while (true) {
            try {
                return Validator.validManagerInOrderListAction(scanner.nextLine(), ManagerCommands.CommandsInOrderList.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AdminCommands.CommandsInUserList adminChoosingActionInUserList() {
        Printer.printCommands(AdminCommands.CommandsInUserList.getStringArray());
        while (true) {
            try {
                return Validator.validAdminCommandInUserList(scanner.nextLine(), AdminCommands.CommandsInUserList.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static ManagerCommands managerChoosingAction() {
        Printer.printCommands(StringCommands.MANAGER_COMMANDS.getCommands());
        while (true) {
            try {
                return Validator.validManagerAction(scanner.nextLine(), ManagerCommands.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static AdminCommands adminChoosingAction() {
        Printer.printCommands(StringCommands.ADMIN_COMMANDS.getCommands());
        while (true) {
            try {
                return Validator.validAdminAction(scanner.nextLine(), AdminCommands.values());
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static boolean areYouSure(String message) {
        Printer.print(message);
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), "yes", "no").equals("yes");
            } catch (InvalidInputException | DeliberateInterruptException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static Car getCar(User user) throws DeliberateInterruptException {
        Printer.print("Переход на страницу добавление автомобиля, для отмены, можете ввести команду \"Назад\" в любой момент.");
        if(user.getAccessLevel().equals(AccessLevels.CLIENT))
            return createUserCar((Client) user);
        else
            return createDealerCar();
    }

    private static Car createUserCar(Client client) throws DeliberateInterruptException {
        return new Car(
                client,
                getBrand(),
                getModel(),
                getColor()
        );
    }

    private static Car createDealerCar() throws DeliberateInterruptException {
        return new Car(
                getBrand(),
                getModel(),
                getColor(),
                getYearOfProduction(),
                getMileAge(),
                getText("Enter car description (up to 1000 characters): "),
                getPrice()
        );
    }

    private static String getBrand() throws DeliberateInterruptException {
        return getText("Enter brand of the car");
    }

    private static String getModel() throws DeliberateInterruptException {
        return getText("Enter model of the car");
    }

    private static String getColor() throws DeliberateInterruptException{
        return getText("Enter color of the car");
    }

    private static Integer getYearOfProduction() throws DeliberateInterruptException {
        Printer.print("Введите год производства:");
        return getCarParam(CarParameters.YEAR);
    }

    private static Integer getPrice() throws DeliberateInterruptException {
        Printer.print("Price:");
        return getCarParam(CarParameters.PRICE);
    }

    private static Integer getMileAge() throws DeliberateInterruptException {
        Printer.print("Mileage:");
        return getCarParam(CarParameters.MILEAGE);
    }

    private static Integer getCarParam(CarParameters parameter) throws DeliberateInterruptException {
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), parameter);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }


    public static String getText(String message) throws DeliberateInterruptException {
        Printer.print(message);
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 1000);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getInput() throws DeliberateInterruptException {
        String input = scanner.nextLine();
        if ("back".equalsIgnoreCase(input))
            throw new DeliberateInterruptException();
        return input;
    }

    public static String getNewOrderStatus(OrderTypes type) throws DeliberateInterruptException{
        Printer.printCommandsWithCustomQuestion(StatusesOfOrder.getStringArray(type), "What order status do you want to set?");
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), StatusesOfOrder.getStringArray(type));
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getUserPhoneNumber() {
        Printer.print("Enter your contact phone number (e.g. 89998887766 without extra symbols: ");
        String number;
        do {
            number = scanner.nextLine();
            if (!PhoneNumberValidator.isValidPhoneNumber(number))
                Printer.printCentered("It seems you made a mistake while entering the number, try again: ");
            else return number;
        } while (true);
    }

    public static int tryGetNumberFromUser() throws InvalidInputException {
        return Validator.isNumber(scanner.nextLine());
    }

    public static String getSetParameterName(String[] commands) throws DeliberateInterruptException {
        Printer.printCommandsWithCustomQuestion(commands, "Which parameter do you want to change? (Enter \"Back\" to cancel the action)");
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), commands);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getPath() throws DeliberateInterruptException {
        Printer.print("Enter the path to the directory where you want to create a file or its name, but in this case, the file will be saved in the project folder (to cancel the operation, enter \"Back\"");
        String input = scanner.nextLine();
        if("back".startsWith(input.toLowerCase())) throw new DeliberateInterruptException();
        return input;
    }


}
