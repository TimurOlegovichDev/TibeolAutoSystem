package Controller;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.*;
import Model.Entities.Message;
import Model.Entities.Order.*;
import Model.Entities.Users.*;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.*;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.in.Validator;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.*;
import ui.out.Printer;

import java.sql.SQLException;
import java.time.Year;
import java.util.*;

/**
 * Является центральным хабом для обработки различных
 * действий и взаимодействий в системе управления автосалоном.
 * Он содержит множество статических методов, которые выполняют различные действия
 * по распределению действий между ролями.
 *  @see  ShowRoomActionsHandler
 *  @see OrderPageActionsHandler
 *  @see UserListPageHandler
 *
 * Данные классы отвечает за переход между страницами и различными действиями
 */

public abstract class ActionHandler {

    static void viewUserOrders(Client client) {
        Printer.print(client.getOrderList());
    }

    static void viewUserCars(Client client) {
        Printer.print(client.getCarData());
    }

    public static void removeUserCar(Client client){
        try {
            Printer.print(client.getCarData());
            int id = Menu.tryGetNumberFromUser();
            if(!client.getCarData().containsKey(id)) throw new NoSuchCarException();
            if(!Menu.areYouSure("Вы точно хотите удалить? (Да/Нет) ")) return;
            client.removeCar(id);
        }  catch (NoSuchElementException | NoSuchCarException e){
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored){
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }

    static void addUserCar(Client client)  {
        try {
            Car car = Menu.getCar(client);
            client.addCar(car);
            Controller.logger.log(LogActions.CLIENT_ADD_CAR.getText() + car, Levels.INFO);
        }
        catch (DeliberateInterruptException ignored){
            Printer.print("Операция отменена");
        }
    }

    static Scenes removeAccount(User user){
        if(Menu.areYouSure(Messages.DELETE_ACCOUNT_WARNING.getMessage())) {
            user.removeAccount();
            System.out.println("Аккаунт удален!");
            Controller.logger.log(LogActions.USER_DELETE_ACCOUNT.getText() + user, Levels.INFO);
            return Scenes.CHOOSING_ROLE;
        }
        else return Scenes.ACTIONS;
    }

    static Scenes setUpUserParameters(User user){
        try {
            switch (Menu.getSetParameterName(new String[]{"Имя", "Номер телефона"})) {
                case "Имя" -> user.setName(Menu.getUserName());
                case "Номер телефона" -> user.setPhoneNumber(Menu.getUserPhoneNumber());
            }
            Controller.logger.log(LogActions.USER_SETUP_PROFILE.getText() + user, Levels.INFO);
        } catch (DeliberateInterruptException ignored){
            Printer.print(Messages.RETURN.getMessage());
        } catch (InvalidInputException e) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
        return Scenes.ACTIONS;
    }

    static void goToShowRoom(User user){
        Printer.printCentered("Выполняется переход на страницу автосалона");
        ShowRoomActionsHandler.chooseAction(user);
    }

    public static void readMessages(Client currentUser) {
        Printer.printCentered("Выполняется переход на страницу сообщений");
        Printer.print(currentUser.getMessages());
        currentUser.getMessages().clear();
        currentUser.checkOrderToArchive();
    }

    public static void gotoOrdersPage(Manager manager) {
        Printer.printCentered("Выполняется переход на страницу заказов");
        OrderPageActionsHandler.managerActionHandler(manager, Menu.managerChoosingActionInOrderList());
    }

    public static void gotoUserListPage(Administrator administrator) {
        Printer.printCentered("Выполняется переход на страницу списка пользователей");
        UserListPageHandler.adminActionHandler(administrator, Menu.adminChoosingActionInUserList());
    }

    public static void getLogList() {
        Printer.printCentered("Список событий программы:");
        Printer.print(Controller.logger.getLogs());
    }

    public static void saveLogList() {
        Printer.printCentered("Сохранение событий программы: ");
        try {
            Controller.logger.saveLogsToFile(Menu.getPath());
        } catch (DeliberateInterruptException e) {
            Printer.print(Messages.RETURN.getMessage());
        }
    }


    static class ShowRoomActionsHandler {

        static void chooseAction(User user) {
            switch (user.getAccessLevel()){
                case CLIENT -> clientActionHandler((Client) user, Menu.clientChoosingActionInShowRoom());
                case MANAGER -> managerActionHandler((Manager) user, Menu.managerChoosingActionInShowRoom());
            }
        }



        protected static void clientActionHandler(Client client, ClientCommands.CommandsInShowRoom command){
                switch (command) {
                    case VIEW_ALL_CARS -> Printer.printDealerCars(DataBaseHandler.getCarData());
                    case CREATE_PURCHASE_ORDER -> createPurchaseOrder(client);
                    case CREATE_SERVICE_ORDER -> createServiceOrder(client);
                    case SEARCH_CAR -> Printer.printDealerCars(getFilterList(client));
                    case BACK -> {
                        Printer.printCentered("Возврат на предыдущую страницу");
                        return;
                    }
                }
                chooseAction(client);
        }

        private static void createServiceOrder(Client client){
            try {
                Printer.print(client.getCarData());
                Printer.print("Для заказа на обслуживание, введите ID вашего авто из списка, если нужного автомобиля нет, воспользуйтесь действием \"Добавить автомобиль\" (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                Car car = client.getCarByID(id);
                if(car == null) throw new NoSuchCarException();
                if(!Menu.areYouSure("Вы выбрали автомобиль " + car.getBrand() + " " + car.getModel() + "?"))
                    createServiceOrder(client);
                client.createServiceOrder(Menu.getText("Сообщите, по какой причине вы хотите обслужить авто: ") + car, car.getID());
                Printer.print("Заказ на обслуживание успешно создан и передан в автосалон");
                Controller.logger.log(LogActions.NEW_SERVICE_ORDER.getText() + OrderTypes.SERVICE + " " + car, Levels.INFO);
            } catch (InvalidInputException | DeliberateInterruptException e) {
                Printer.print(Messages.RETURN.getMessage());
            } catch (NoSuchCarException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
                createServiceOrder(client);
            }
        }

        private static void createPurchaseOrder(Client client){
            try {
                Printer.print(DataBaseHandler.getCarData());
                Printer.print("Введите ID интересующего вас автомобиля (для отмены введите любое слово): ");
                int idNewCar = Menu.tryGetNumberFromUser();
                Car car = DataBaseHandler.getCar(idNewCar);
                if(!Menu.areYouSure("Вы хотите сделать заказ на автомобиль " + car.getBrand() + " " + car.getModel() + " стоимостью " + car.getPrice() + "?"))
                    createPurchaseOrder(client);
                client.createPurchaseOrder("Желаю приобрести автомобиль " + car, idNewCar);
                Printer.print("Заказ на покупку успешно создан и передан в автосалон");
                Controller.logger.log(LogActions.NEW_PURCHASE_ORDER.getText() + OrderTypes.PURCHASE + " " + car, Levels.INFO);
            } catch (InvalidInputException e) {
                Printer.print(Messages.RETURN.getMessage());
            } catch (NoSuchCarException | NoSuchElementException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
                createPurchaseOrder(client);
            }
        }

        protected static void managerActionHandler(Manager manager, ManagerCommands.CommandsInShowRoom command)  {
            switch (command) {
                case VIEW_ALL_CARS -> Printer.printDealerCars(DataBaseHandler.getCarData());
                case ADD_CAR -> addCar(manager);
                case REMOVE_CAR -> removeCar();
                case SEARCH_CAR -> Printer.printDealerCars(getFilterList(manager));
                case BACK -> {
                    Printer.printCentered("Возврат на предыдущую страницу");
                    return;
                }
            }
            chooseAction(manager);
        }

        private static List<Map.Entry<Integer, Car>> getFilterList(User user) {
            while(true) {
                String command = "";
                try {
                    Printer.printCommandsWithCustomQuestion(new String[] {"Марка", "Модель", "Цвет", "Год", "Пробег", "Цена"}, "Введите параметр, по которому будет сортировка");
                    command = Validator.validCommand(Menu.getInput(), "Марка", "Модель", "Цвет", "Год", "Пробег", "Цена");
                    return filterList(command);
                } catch (InvalidInputException e) {
                    Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
                } catch (DeliberateInterruptException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private static List<Map.Entry<Integer, Car>> filterList(String command) throws DeliberateInterruptException, NullPointerException {
            switch (Objects.requireNonNull(CarParameters.getCarParameterFromString(command))) {
                case BRAND -> {
                    String filter = Menu.getText("Введите интересующий брэнд: ");
                    return DataBaseHandler
                            .getCarData()
                            .entrySet()
                            .stream()
                            .filter(integerCarEntry -> integerCarEntry
                                    .getValue()
                                    .getBrand()
                                    .toLowerCase()
                                    .startsWith(filter.toLowerCase()))
                            .toList();
                }
                case MODEL -> {
                    String filter = Menu.getText("Введите интересующую марку: ");
                    return DataBaseHandler
                            .getCarData()
                            .entrySet()
                            .stream()
                            .filter(integerCarEntry -> integerCarEntry
                                    .getValue()
                                    .getModel()
                                    .toLowerCase()
                                    .startsWith(filter.toLowerCase()))
                            .toList();
                }
                case COLOR -> {
                    String filter = Menu.getText("Введите интересующий цвет: ");
                    return DataBaseHandler
                            .getCarData()
                            .entrySet()
                            .stream()
                            .filter(integerCarEntry -> integerCarEntry
                                    .getValue()
                                    .getColor()
                                    .toLowerCase()
                                    .startsWith(filter.toLowerCase()))
                            .toList();
                }
                case YEAR -> {
                    Printer.print("Введите минимальный год");
                    int filter = Menu.getNumberGreaterZero(Year.now().getValue());
                    return DataBaseHandler
                            .getCarData()
                            .entrySet()
                            .stream()
                            .filter(integerCarEntry -> integerCarEntry
                                    .getValue()
                                    .getYearOfProduction() >= filter)
                            .toList();
                }
                case MILEAGE -> {
                    Printer.print("Введите максимальный пробег:");
                    int filter = Menu.getNumberGreaterZero(Integer.MAX_VALUE);
                    return DataBaseHandler
                            .getCarData()
                            .entrySet()
                            .stream()
                            .filter(integerCarEntry -> integerCarEntry
                                    .getValue()
                                    .getMileAge() <= filter)
                            .toList();
                }
                case PRICE -> {
                    Printer.print("Введите максимальную цену:");
                    int filter = Menu.getNumberGreaterZero(Integer.MAX_VALUE);
                    return DataBaseHandler
                            .getCarData()
                            .entrySet()
                            .stream()
                            .filter(integerCarEntry -> integerCarEntry
                                    .getValue()
                                    .getPrice() <= filter)
                            .toList();
                }
            }
            return null;
        }

        private static void addCar(Manager manager){
            try {
                Car car = Menu.getCar(manager);
                DataBaseHandler.add(car);
                Controller.logger.log(LogActions.NEW_CAR_IN_DEALER.getText() + car, Levels.INFO);
            } catch (DeliberateInterruptException e){
                Printer.print(Messages.RETURN.getMessage());
            }
        }

        private static void removeCar(){
            try {
                Printer.print(DataBaseHandler.getCarData());
                Printer.print("Введите ID автомобиля подлежащего удалению (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                if(!DataBaseHandler.getCarData().containsKey(id)) throw new NoSuchCarException();
                Car car = DataBaseHandler.getCar(id);
                if(!Menu.areYouSure("Вы точно хотите удалить? (Да/Нет) ")) return;
                DataBaseHandler.remove(car);
                Controller.logger.log(LogActions.CAR_DELETED.getText() + car, Levels.INFO);
            }  catch (NoSuchElementException | NoSuchCarException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }


    static class OrderPageActionsHandler {

        protected static void managerActionHandler(Manager manager, ManagerCommands.CommandsInOrderList command)  {
            switch (command) {
                case VIEW_ACTIVE_ORDERS -> Printer.print(DataBaseHandler.getOrderStream()
                        .filter(order -> order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                        .toList());
                case VIEW_ARCHIVED_ORDERS -> Printer.print(DataBaseHandler.getOrderStream()
                        .filter(order -> !order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                        .toList());
                case SET_STATUS -> setNewStatusOrder(manager);
                case BACK -> {
                    Printer.printCentered("Возврат на предыдущую страницу");
                    return;
                }
            }
            managerActionHandler(manager, Menu.managerChoosingActionInOrderList());
        }


        private static void setNewStatusOrder(Manager manager){
            List<Order> list = DataBaseHandler.getOrderStream()
                    .filter(order -> !order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                    .toList();
            Printer.print(list, true);
            Printer.print("Введите порядковый номер заказа из этого списка, введите \"Назад\", чтобы вернуться");
            while (true) {
                try {
                    int index = Menu.getNumberGreaterZero(list.size());
                    Order currentOrder = list.get(index-1);
                    currentOrder.setStatus(manager, StatusesOfOrder.CHECKED, false);
                    if(!Menu.areYouSure("Вы выбрали заказ на " + currentOrder.getOrderType() + " от клиента "
                            + currentOrder.getOwner().getUserParameters().getName() +
                            " насчет автомобиля " + currentOrder.getCar().getBrand() + " " + currentOrder.getCar().getModel() +
                            " верно? (Да/Нет)")) continue;
                    chooseNewStatus(manager, currentOrder);
                    Controller.logger.log(LogActions.ORDER_STATUS_CHANGED.getText() + currentOrder, Levels.INFO);
                    return;
                } catch(NoSuchElementException e) {
                    Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
                } catch (DeliberateInterruptException e) {
                    Printer.print(Messages.RETURN.getMessage());
                    return;
                } catch (InvalidCommandException e) {
                    Printer.print(Messages.INVALID_COMMAND.getMessage());
                }
            }
        }

        private static void chooseNewStatus(Manager manager, Order order) throws DeliberateInterruptException, InvalidCommandException {
            switch (Menu.getNewOrderStatus(order)){
                case "Выполняется" -> order.setStatus(manager, StatusesOfOrder.EXECUTING, false);
                case "Завершено" -> {
                    order.setStatus(manager, StatusesOfOrder.COMPLETED, false);
                    order.getCar().setBooked(false);
                }
                case "Отклонено" -> dismissOrder(manager, order);
                case "Одобрено" -> {
                    DataBaseHandler.remove(order.getCar());
                    order.setStatus(manager, StatusesOfOrder.AGREED, false);
                    order.getOwner().buyCar(order.getCar());
                }
            }
        }

        private static void dismissOrder(Manager manager, Order order){
            try {
                Printer.print("Отклоняемый заказ: " + order.toString());
                if(!Menu.areYouSure("Вы точно хотите отклонить? (Да/Нет) ")) return;
                order.setStatus(manager, StatusesOfOrder.DISMISSED, false);
                order.getOwner().receiveMessage(new Message(manager, Menu.getDismissMessage()));
                order.getCar().setBooked(false);
            }  catch (NoSuchElementException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }

    static class UserListPageHandler {

        protected static void adminActionHandler(Administrator administrator, AdminCommands.CommandsInUserList command) {
            switch (command) {
                case USER_LIST -> Printer.print(DataBaseHandler.getUsers());
                case GET_FILTER_LIST -> {
                    try {
                        Printer.print(getFilterList());
                    } catch (DeliberateInterruptException e) {
                        Printer.print(Messages.RETURN.getMessage());
                    }
                }
                case SET_USER_PARAM -> setUserParameters(administrator);
                case DELETE_USER -> deleteUser(administrator);
                case BACK -> {
                    Printer.printCentered("Возврат на предыдущую страницу");
                    return;
                }
            }
            adminActionHandler(administrator, Menu.adminChoosingActionInUserList());
        }

        private static List<Map.Entry<Integer, User>> getFilterList() throws DeliberateInterruptException {
            while(true) {
                String command = "";
                try {
                    command = Validator.validCommand(Menu.getText(
                            """
                            Введите параметр, по которому будет отсортирован список:            \s
                            - Имя
                            - Статус
                            - Номер телефона"""),
                            "Имя", "Статус", "Номер телефона");
                    return filterList(command);
                } catch (InvalidInputException e) {
                    Printer.printCentered(Messages.INVALID_COMMAND.getMessage());
                }
            }
        }

        private static List<Map.Entry<Integer, User>> filterList(String command) throws DeliberateInterruptException {
            switch (command) {
                case "Имя" -> {
//                    String filter = Menu.getText("Введите имя (или его начало) для сортировки: ");
//                    return DataBaseHandler.getUserData().stream().filter(entry -> entry.getValue()
//                                    .getUserParameters()
//                                    .getName()
//                                    .toLowerCase()
//                                    .startsWith(filter))
//                            .toList();

                    //todo
                }
                case "Статус" -> {
//                    String filter = Menu.getText("Введите статус пользователя (или его начало) для сортировки: ");
//                    return DataBaseHandler.getUserData().entrySet().stream().filter(entry -> entry.getValue()
//                                    .getAccessLevel()
//                                    .getValue()
//                                    .startsWith(filter))
//                            .toList();
                    //todo
                }
                case "Номер телефона" -> {
//                    String filter = Menu.getText("Введите номер телефона (или его начало) для сортировки: ");
//                    return DataBaseHandler.getUserData().entrySet().stream().filter(entry -> entry.getValue()
//                                    .getPhoneNumber()
//                                    .startsWith(filter))
//                            .toList();
                    //todo
                }
            }
            return List.of();
        }

        private static void setUserParameters(Administrator administrator){
            try {
                Printer.print(DataBaseHandler.getUsers());
                Printer.print("Введите ID пользователя, параметры которого желаете изменить (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                User user = DataBaseHandler.getUserById(id);
                if(id == administrator.getUserParameters().getID()) {
                    Printer.printCentered("Нельзя выбрать свой профиль в этом действии!");
                    return;
                }
                else if(!Menu.areYouSure("Вы выбрали пользователя -> " + user + "? (Да/Нет) ")) return;
                ActionHandler.setUpUserParameters(user);
                Controller.logger.log(LogActions.USER_SETUP_PROFILE.getText() + user, Levels.INFO);
            }  catch (NoSuchElementException | NoSuchUserException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }

        private static void deleteUser(Administrator administrator){
            try {
                Printer.print(DataBaseHandler.getUsers());
                Printer.print("Введите ID пользователя, которого желаете удалить (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                User user = DataBaseHandler.getUserById(id);
                if(id == administrator.getUserParameters().getID()) {
                    Printer.printCentered("Нельзя выбрать свой профиль в этом действии!");
                    return;
                }
                if(!Menu.areYouSure("Вы точно хотите удалить пользователя? (Да/Нет) ")) return;
                user.removeAccount();
                Printer.printCentered("Аккаунт удален");
                Controller.logger.log(LogActions.USER_DELETE_ACCOUNT.getText() + user, Levels.INFO);
            }  catch (NoSuchElementException | NoSuchUserException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }

    }

}
