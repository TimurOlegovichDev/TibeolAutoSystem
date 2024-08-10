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

    public static @NotNull String getDismissMessage(){
        Printer.print(Messages.ENTER_MESSAGE.getMessage());
        String text = scanner.nextLine();
        if(text == null || text.trim().isEmpty())
            return "Текст сообщения отсутствует";
        return text;
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
                return Validator.validCommand(scanner.nextLine(), "да", "нет").equals("да");
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
                getText("Введите описание автомобиля (до 1000 символов): "),
                getPrice()
        );
    }

    private static String getBrand() throws DeliberateInterruptException {
        Printer.print("Введите маркy автомобиля:");
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 15);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static String getModel() throws DeliberateInterruptException {
        Printer.print("Введите модель:");
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 15);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static String getColor() throws DeliberateInterruptException{
        Printer.print("Введите цвет:");
        while (true) {
            try {
                return Validator.validLength(scanner.nextLine(), 15);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static Integer getYearOfProduction() throws DeliberateInterruptException {
        Printer.print("Введите год производства:");
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), CarParameters.YEAR);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static Integer getPrice() throws DeliberateInterruptException {
        Printer.print("Введите цену:");
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), CarParameters.PRICE);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
    private static Integer getMileAge() throws DeliberateInterruptException {
        Printer.print("Введите пробег:");
        while (true) {
            try {
                return Validator.validIntCarParameter(scanner.nextLine(), CarParameters.MILEAGE);
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
        return scanner.nextLine();
    }

    public static Integer getNumberGreaterZero(int maxValue) throws DeliberateInterruptException {
        Printer.print("Введите число: ");
        while (true) {
            try {
                int value =  Validator.validNumber(scanner.nextLine());
                if(value <= maxValue && value > 0) return value;
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getNewOrderStatus(OrderTypes status) throws DeliberateInterruptException{
        Printer.printCommandsWithCustomQuestion(StatusesOfOrder.getStringArray(status), "Какой статус заказа вы желаете установить?");
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), StatusesOfOrder.getStringArray(status));
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getUserPhoneNumber() {
        Printer.print("Введите ваш контактный номер телефона (аналогично 89998887766 без сторонних символов: ");
        String number;
        do {
            number = scanner.nextLine();
            if(!PhoneNumberValidator.isValidPhoneNumber(number))
                Printer.printCentered("Похоже, что вы совершили ошибку при вводе номера, попробуйте снова: ");
            else return number;
        } while (true);
    }

    public static int tryGetNumberFromUser() throws InvalidInputException {
        return Validator.isNumber(scanner.nextLine());
    }

    public static String getSetParameterName(String[] commands) throws DeliberateInterruptException {
        Printer.printCommandsWithCustomQuestion(commands, "Какой параметр вы хотите изменить? (Введите \"Назад\" для отмены действия)");
        while (true) {
            try {
                return Validator.validCommand(scanner.nextLine(), commands);
            } catch (InvalidInputException e) {
                Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    public static String getPath() throws DeliberateInterruptException {
        Printer.print("Введите путь к директории, где нужно создать файл и в конце пути укажите его название, в случае, если вы не укажете путь, файл будет сохранен в папке проекта (для отмены операции, введите \"Назад\"");
        String input = scanner.nextLine();
        if("назад".startsWith(input.toLowerCase())) throw new DeliberateInterruptException();
        return input;
    }


}
