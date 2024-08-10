package Controller;

import Model.DataBase.*;
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

import javax.xml.crypto.Data;
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
        Printer.print(DataBaseHandler.getTableByFieldAndValue(
                DataBaseHandler.ordersTableName,
                client.getID(),
                OrderDataFields.CLIENT_ID
        ));
    }

    static void viewUserCars(Client client) {
        Printer.print(DataBaseHandler.getTableByFieldAndValue(
                DataBaseHandler.clientsCarTableName,
                client.getID(),
                ClientCarDataFields.CLIENT
        ));
    }

    public static void removeUserCar(Client client){
        try {
            viewUserCars(client);
            int id = Menu.tryGetNumberFromUser();
            if(!DataBaseHandler.getColumnByField(DataBaseHandler.clientsCarTableName, ClientCarDataFields.ID).contains(id))
                throw new NoSuchCarException();
            if(!Menu.areYouSure("Вы точно хотите удалить? (Да/Нет) "))
                return;
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
            Printer.print("Аккаунт удален!");
            Controller.logger.log(LogActions.USER_DELETE_ACCOUNT.getText() + user, Levels.INFO);
            return Scenes.CHOOSING_ROLE;
        }
        else return Scenes.ACTIONS;
    }

    static Scenes setUpUserParameters(int id){
        try {
            switch (Menu.getSetParameterName(new String[]{"Имя", "Номер телефона"})) {
                case "Имя" -> setName(Menu.getUserName(), id);
                case "Номер телефона" -> setPhoneNumber(Menu.getUserPhoneNumber(), id);
            }
            Controller.logger.log(LogActions.USER_SETUP_PROFILE.getText() + id, Levels.INFO);
        } catch (DeliberateInterruptException ignored){
            Printer.print(Messages.RETURN.getMessage());
        }
        return Scenes.ACTIONS;
    }

    private static void setName(String userName, int id) {
        DataBaseHandler.setParameterById(
                UsersDataFields.NAME.getValue(),
                DataBaseHandler.usersTableName,
                userName,
                id
        );
    }

    private static void setPhoneNumber(String phoneNumber, int id) {
        String newPhoneNumber = String.format("%s (%s) %s-%s-%s",
                phoneNumber.charAt(0),
                phoneNumber.substring(1, 4),
                phoneNumber.substring(4, 7),
                phoneNumber.substring(7, 9),
                phoneNumber.substring(9));
        DataBaseHandler.setParameterById(
                UsersDataFields.PHONE_NUMBER.getValue(),
                DataBaseHandler.usersTableName,
                newPhoneNumber,
                id
        );
    }

    static void goToShowRoom(User user){
        Printer.printCentered("Выполняется переход на страницу автосалона");
        ShowRoomActionsHandler.chooseAction(user);
    }

    public static void readMessages(Client currentUser) {
        Printer.printCentered("Выполняется переход на страницу сообщений");
        Printer.print(DataBaseHandler.getTableByFieldAndValue(DataBaseHandler.clientMessagesTableName, currentUser.getID(), MessagesDataFields.RECEIVER_ID));
        DataBaseHandler.checkOrderAndArchive();
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


    /**
     *  Обработчик команд на странице автосалона
     */

    private static class ShowRoomActionsHandler {

        static void chooseAction(User user) {
            switch (user.getAccessLevel()){
                case CLIENT -> clientActionHandler((Client) user, Menu.clientChoosingActionInShowRoom());
                case MANAGER -> managerActionHandler((Manager) user, Menu.managerChoosingActionInShowRoom());
            }
        }


        protected static void clientActionHandler(Client client, ClientCommands.CommandsInShowRoom command){
                switch (command) {
                    case VIEW_ALL_CARS -> Printer.printDealerCars(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
                    case CREATE_PURCHASE_ORDER -> createPurchaseOrder(client);
                    case CREATE_SERVICE_ORDER -> createServiceOrder(client);
                    case SEARCH_CAR -> Printer.print("ФУНКЦИЯ В РАЗРАБОТКЕ");
                    case BACK -> {
                        Printer.printCentered("Возврат на предыдущую страницу");
                        return;
                    }
                }
                chooseAction(client);
        }

        private static void createServiceOrder(Client client){
            try {
                Printer.print(DataBaseHandler.getData(DataBaseHandler.clientsCarTableName));
                Printer.print("Для заказа на обслуживание, введите ID вашего авто из списка, если нужного автомобиля нет, воспользуйтесь действием \"Добавить автомобиль\" (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                List<String> carRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.clientsCarTableName, id);
                if(!Menu.areYouSure("Вы выбрали автомобиль " + carRow.get(ClientCarDataFields.BRAND.getIndex()) + " " + carRow.get(ClientCarDataFields.MODEL.getIndex()) + "?"))
                    createServiceOrder(client);
                client.createServiceOrder(Menu.getText("Сообщите, по какой причине вы хотите обслужить авто: "), Integer.parseInt(carRow.get(ClientCarDataFields.ID.getIndex())));
                Printer.print("Заказ на обслуживание успешно создан и передан в автосалон");
                //Controller.logger.log(LogActions.NEW_SERVICE_ORDER.getText() + OrderTypes.SERVICE + " " + car, Levels.INFO);
            } catch (InvalidInputException | DeliberateInterruptException e) {
                Printer.print(Messages.RETURN.getMessage());
            } catch (NoSuchElementException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
                createServiceOrder(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private static void createPurchaseOrder(Client client){
            try {
                Printer.print(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
                Printer.print("Введите ID интересующего вас автомобиля (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                List<String> carRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.dealerCarTableName, id);
                if(!Menu.areYouSure(
                        "Вы хотите сделать заказ на автомобиль " +
                                carRow.get(DealerCarDataFields.BRAND.getIndex()) +
                                " " + carRow.get(DealerCarDataFields.BRAND.getIndex()) +
                                " стоимостью " +
                                carRow.get(DealerCarDataFields.PRICE.getIndex()) +
                                "?"
                )) {
                    createPurchaseOrder(client);
                }
                client.createPurchaseOrder("Желаю приобрести автомобиль с id -> " + carRow.get(DealerCarDataFields.ID.getIndex()), id);
                Printer.print("Заказ на покупку успешно создан и передан в автосалон");
                //Controller.logger.log(LogActions.NEW_PURCHASE_ORDER.getText() + OrderTypes.PURCHASE + " " + car, Levels.INFO);
            } catch (InvalidInputException e) {
                Printer.print(Messages.RETURN.getMessage());
            } catch (NoSuchElementException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
                createPurchaseOrder(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        protected static void managerActionHandler(Manager manager, ManagerCommands.CommandsInShowRoom command)  {
            switch (command) {
                case VIEW_ALL_CARS -> Printer.print(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
                case ADD_CAR -> addCar(manager);
                case REMOVE_CAR -> removeCar();
                case SEARCH_CAR -> Printer.print("ФУНКЦИЯ В РАЗРАБОТКЕ");
                case BACK -> {
                    Printer.printCentered("Возврат на предыдущую страницу");
                    return;
                }
            }
            chooseAction(manager);
        }

        private static void addCar(Manager manager){
            try {
                Car car = Menu.getCar(manager);
                DataBaseHandler.addDealerCar(car);
                Controller.logger.log(LogActions.NEW_CAR_IN_DEALER.getText() + car, Levels.INFO);
            } catch (DeliberateInterruptException e){
                Printer.print(Messages.RETURN.getMessage());
            }
        }

        private static void removeCar(){
            try {
                Printer.print(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
                Printer.print("Введите ID автомобиля подлежащего удалению (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                if(!Menu.areYouSure("Вы точно хотите удалить? (Да/Нет) ")) return;
                DataBaseHandler.removeRowById(DataBaseHandler.dealerCarTableName, id);
                //Controller.logger.log(LogActions.CAR_DELETED.getText() + car, Levels.INFO);
            }  catch (NoSuchElementException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }


    /**
     * Класс для обработки действий на странице заказов
     */

    static class OrderPageActionsHandler {

        protected static void managerActionHandler(Manager manager, ManagerCommands.CommandsInOrderList command)  {
            switch (command) {
                case VIEW_ACTIVE_ORDERS -> Printer.print(DataBaseHandler.getActiveOrders());
                case VIEW_ARCHIVED_ORDERS -> Printer.print(DataBaseHandler.getArchivedOrders());
                case SET_STATUS -> setNewStatusOrder(manager);
                case BACK -> {
                    Printer.printCentered("Возврат на предыдущую страницу");
                    return;
                }
            }
            managerActionHandler(manager, Menu.managerChoosingActionInOrderList());
        }


        private static void setNewStatusOrder(Manager manager){
            Printer.print(DataBaseHandler.getActiveOrders());
            Printer.print("Введите номер заказа из этого списка или введите \"Назад\", чтобы вернуться");
            while (true) {
                try {
                    int id = Menu.tryGetNumberFromUser();
                    List<String> orderRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.dealerCarTableName, id);
                    if(!Menu.areYouSure("Вы выбрали заказ на " + orderRow.get(OrderDataFields.TYPE.getIndex()) +
                            " автомобиля " +
                            DataBaseHandler.getRowByIdFromTable(
                                    DataBaseHandler.clientsCarTableName,
                                    Integer.parseInt(
                                            orderRow.get(OrderDataFields.CLIENT_CAR_ID.getIndex()))
                            ).get(ClientCarDataFields.BRAND.getIndex()) +
                            " " +
                            DataBaseHandler.getRowByIdFromTable(
                                    DataBaseHandler.clientsCarTableName,
                                    Integer.parseInt(
                                            orderRow.get(OrderDataFields.CLIENT_CAR_ID.getIndex()))
                            ).get(ClientCarDataFields.MODEL.getIndex()) +
                            " верно? (Да/Нет)")) continue;
                    //todo
                    chooseNewStatus(manager, OrderTypes.getTypeFromString(orderRow.get(OrderDataFields.STATUS.getIndex())), id);
                    //Controller.logger.log(LogActions.ORDER_STATUS_CHANGED.getText() + currentOrder, Levels.INFO);
                    return;
                } catch(NoSuchElementException e) {
                    Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
                } catch (DeliberateInterruptException e) {
                    Printer.print(Messages.RETURN.getMessage());
                    return;
                } catch (InvalidCommandException e) {
                    Printer.print(Messages.INVALID_COMMAND.getMessage());
                } catch (Exception e) {
                    Printer.print(Messages.ERROR.getMessage());
                }
            }
        }

        private static void chooseNewStatus(Manager manager, OrderTypes status, int orderId) throws DeliberateInterruptException, InvalidCommandException {
            switch (Menu.getNewOrderStatus(status)){
                case "Выполняется" -> DataBaseHandler.setParameterById(
                        OrderDataFields.STATUS.getValue(),
                        DataBaseHandler.ordersTableName,
                        StatusesOfOrder.EXECUTING.getCommand(),
                        orderId
                        );
                case "Завершено" ->
                    DataBaseHandler.setParameterById(
                            OrderDataFields.STATUS.getValue(),
                            DataBaseHandler.ordersTableName,
                            StatusesOfOrder.COMPLETED.getCommand(),
                            orderId
                    );

                case "Отклонено" -> dismissOrder(manager, orderId);

                case "Одобрено" ->
                    DataBaseHandler.setParameterById(
                            OrderDataFields.STATUS.getValue(),
                            DataBaseHandler.ordersTableName,
                            StatusesOfOrder.AGREED.getCommand(),
                            orderId
                    );
                    //todo что то с буком авто
                }
            }
        }

        private static void dismissOrder(Manager manager, int orderId){
            try {
                Printer.print("Отклоняемый заказ под номером: " + orderId);
                if(!Menu.areYouSure("Вы точно хотите отклонить? (Да/Нет) ")) return;
                DataBaseHandler.setParameterById(
                        OrderDataFields.STATUS.getValue(),
                        DataBaseHandler.ordersTableName,
                        StatusesOfOrder.DISMISSED.getCommand(),
                        orderId
                );
               //todo что то с буком авто
            }  catch (NoSuchElementException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }

    static class UserListPageHandler {

        protected static void adminActionHandler(Administrator administrator, AdminCommands.CommandsInUserList command) {
            switch (command) {
                case USER_LIST -> Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
                case GET_FILTER_LIST -> {
                    Printer.print("ФУНКЦИЯ В РАЗРАБОТКЕ");
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

        private static void setUserParameters(Administrator administrator) {
            try {
                Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
                Printer.print("Введите ID пользователя, параметры которого желаете изменить (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                if (id == administrator.getID()) {
                    Printer.printCentered("Нельзя выбрать свой профиль в этом действии!");
                    return;
                } else if (!Menu.areYouSure("Вы выбрали пользователя -> " +
                        DataBaseHandler.getUserParamById(id).get(UsersDataFields.NAME.getIndex()) +
                        "? (Да/Нет) ")) return;
                ActionHandler.setUpUserParameters(id);
                Controller.logger.log(LogActions.USER_SETUP_PROFILE.getText() + id, Levels.INFO);
            } catch (NoSuchElementException | NoSuchUserException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }

        private static void deleteUser(Administrator administrator) {
            try {
                Printer.print(DataBaseHandler.getData(DataBaseHandler.usersTableName));
                Printer.print("Введите ID пользователя, которого желаете удалить (для отмены введите любое слово): ");
                int id = Menu.tryGetNumberFromUser();
                if (id == administrator.getID()) {
                    Printer.printCentered("Нельзя выбрать свой профиль в этом действии!");
                    return;
                }
                if (!Menu.areYouSure("Вы точно хотите удалить пользователя " +
                        DataBaseHandler.getUserParamById(id).get(UsersDataFields.NAME.getIndex()) +
                        "? (Да/Нет) "))
                    return;
                DataBaseHandler.removeRowById(DataBaseHandler.usersTableName, id);
                Printer.printCentered("Аккаунт удален");
                Controller.logger.log(LogActions.USER_DELETE_ACCOUNT.getText() + id, Levels.INFO);
            } catch (NoSuchElementException | NoSuchUserException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored) {
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }
}
