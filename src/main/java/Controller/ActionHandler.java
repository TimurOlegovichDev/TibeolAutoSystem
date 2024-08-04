package Controller;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.StatusesOfOrder;
import Model.Entities.Users.Client;
import Model.Entities.Users.Manager;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidCommandException;
import Model.Exceptions.UserExc.InvalidInputException;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.messageSrc.commands.ClientCommands;
import ui.messageSrc.commands.ManagerCommands;
import ui.out.Printer;

import java.util.List;
import java.util.NoSuchElementException;

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
        try {client.addCar(Menu.getCar(client));}
        catch (DeliberateInterruptException ignored){
            Printer.print("Операция отменена");
        }
    }

    static Scenes removeAccount(User user){
        if(Menu.areYouSure(Messages.DELETE_ACCOUNT_WARNING.getMessage())) {
            user.removeAccount();
            System.out.println("Аккаунт удален!");
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

    static void viewUsers(){
        Printer.print(DataBaseHandler.getUserData());
    }

    public static void readMessages(Client currentUser) {
        Printer.printCentered("Выполняется переход на страницу сообщений");
        Printer.print(currentUser.getMessages());
        currentUser.getMessages().clear();
        currentUser.checkOrderToArchive();
    }

    public static void gotoOrdersPage(Manager manager) {
        Printer.printCentered("Выполняется переход на страницу заказов");
        orderPageActionsHandler.managerActionHandler(manager, Menu.managerChoosingActionInOrderList());
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
                    case SEARCH_CAR -> {}
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
                case SEARCH_CAR -> {}
                case SETUP_CAR -> {}
                case BACK -> {
                    Printer.printCentered("Возврат на предыдущую страницу");
                    return;
                }
            }
            chooseAction(manager);
        }


        private static void addCar(Manager manager){
            try {
                DataBaseHandler.add(Menu.getCar(manager));
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
            }  catch (NoSuchElementException | NoSuchCarException e){
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception ignored){
                Printer.print(Messages.INVALID_COMMAND.getMessage());
            }
        }
    }


    static class orderPageActionsHandler {

        protected static void managerActionHandler(Manager manager, ManagerCommands.CommandsInOrderList command)  {
            switch (command) {
                case VIEW_ACTIVE_ORDERS -> Printer.print(DataBaseHandler.getOrderStream()
                        .filter(order -> order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                        .toList());
                case VIEW_ARCHIVED_ORDERS -> Printer.print(DataBaseHandler.getOrderStream()
                        .filter(order -> !order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                        .toList());
                case SET_STATUS -> setNewStatusOrder(manager);
                //case DISMISS -> dismissOrder(manager);
                case SEARCH_ORDERS -> {}
                case BACK -> Printer.printCentered("Возврат на предыдущую страницу");

            }
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

}
