package Controller.ActionHandlers.ManagerHandler;

import Controller.Controller;
import Model.DataBaseHandler;
import Model.DataFields.DealerCarDataFields;
import Model.DataFields.OrderDataFields;
import Model.Entities.Car.Car;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Entities.Users.Manager;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidCommandException;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.out.Printer;

import java.util.List;
import java.util.NoSuchElementException;

public class ManagerActionsHandlerInOrders {

    static void gotoOrderPage(){
        Printer.printCentered("Go to the ordes page");
        distribute();
    }

    protected static void distribute() {
        switch (Menu.managerChoosingActionInOrderList()) {
            case VIEW_ACTIVE_ORDERS -> Printer.print(DataBaseHandler.getActiveOrders());
            case VIEW_ARCHIVED_ORDERS -> Printer.print(DataBaseHandler.getArchivedOrders());
            case SET_STATUS -> setNewStatusOrder((Manager) Controller.getController().getCurrentUser());
            case BACK -> {
                Printer.printCentered("Go to previous page");
                return;
            }
        }
        distribute();
    }


    private static void setNewStatusOrder(Manager manager) {
        while (true) {
            try {
                Printer.print(DataBaseHandler.getActiveOrders());
                Printer.print("Введите номер заказа из этого списка или введите не число, чтобы вернуться");
                int id = Menu.tryGetNumberFromUser();
                List<String> validId = DataBaseHandler.getColumnByField(DataBaseHandler.ordersTableName, OrderDataFields.ID, " WHERE status != 'Архивировано'");
                if (!validId.contains(String.valueOf(id)))
                    throw new NoSuchElementException();
                List<String> orderRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.ordersTableName, id);
                if (!Menu.areYouSure("Вы выбрали заказ на " + orderRow.get(OrderDataFields.TYPE.getIndex()) +
                        " автомобиля верно? (Да/Нет)")) continue;
                chooseNewStatus(manager, OrderTypes.getTypeFromString(orderRow.get(OrderDataFields.TYPE.getIndex())), id);
                return;
            } catch (NoSuchElementException e) {
                Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            } catch (Exception e) {
                Printer.print(Messages.ERROR.getMessage() + e.getMessage());
                return;
            }
        }
    }

    private static void chooseNewStatus(Manager manager, OrderTypes type, int orderId) throws DeliberateInterruptException, InvalidCommandException {
        switch (Menu.getNewOrderStatus(type)) {
            case "Выполняется" -> setOrderStatus(StatusesOfOrder.EXECUTING, orderId);

            case "Завершено" -> setOrderStatus(StatusesOfOrder.COMPLETED, orderId);

            case "Отклонено" -> dismissOrder(manager, orderId);

            case "Одобрено" -> agreedOrder(manager, orderId);
        }
    }

    private static void setOrderStatus(StatusesOfOrder status, int orderId) {
        DataBaseHandler.setParameterById(
                OrderDataFields.STATUS.getValue(),
                DataBaseHandler.ordersTableName,
                status.getCommand(),
                orderId
        );
        Controller.logger.log(Levels.INFO, LogActions.ORDER_STATUS_CHANGED.getText() + status);
    }

    private static void dismissOrder(Manager manager, int orderId) {
        try {
            Printer.print("Отклоняемый заказ под номером: " + orderId);
            if (!Menu.areYouSure("Вы точно хотите отклонить? (Да/Нет) ")) return;
            List<String> orderRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.ordersTableName, orderId);
            DataBaseHandler.setParameterById(
                    OrderDataFields.STATUS.getValue(),
                    DataBaseHandler.ordersTableName,
                    StatusesOfOrder.DISMISSED.getCommand(),
                    orderId
            );
            DataBaseHandler.setParameterById(
                    DealerCarDataFields.BOOKED.getValue(),
                    DataBaseHandler.dealerCarTableName,
                    "false",
                    Integer.parseInt(orderRow.get(OrderDataFields.CLIENT_CAR_ID.getIndex()))
            );
            DataBaseHandler.checkOrderAndArchive();
            Controller.logger.log(Levels.INFO, LogActions.ORDER_STATUS_CHANGED.getText() + "отклонено");
        } catch (NoSuchElementException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }

    private static void agreedOrder(Manager manager, int orderId) {
        try {
            Printer.print("Одобряется на покупку заказ под номером: " + orderId);
            if (!Menu.areYouSure("Вы точно хотите одобрить? (Да/Нет) ")) return;
            setOrderStatus(StatusesOfOrder.AGREED, orderId);
            List<String> orderRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.ordersTableName, orderId);
            int carId = Integer.parseInt(orderRow.get(OrderDataFields.CLIENT_CAR_ID.getIndex()));
            int userId = Integer.parseInt(orderRow.get(OrderDataFields.CLIENT_ID.getIndex()));
            List<String> carRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.dealerCarTableName, carId);
            DataBaseHandler.addClientCar(
                    new Car(
                            null,
                            carRow.get(DealerCarDataFields.BRAND.getIndex()),
                            carRow.get(DealerCarDataFields.MODEL.getIndex()),
                            carRow.get(DealerCarDataFields.COLOR.getIndex())
                    ),
                    userId
            );
            DataBaseHandler.removeRowById(
                    DataBaseHandler.dealerCarTableName,
                    carId
            );
            DataBaseHandler.checkOrderAndArchive();
            Controller.logger.log(Levels.INFO, LogActions.ORDER_STATUS_CHANGED.getText() + "одобрено");
        } catch (NoSuchElementException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
        } catch (Exception ignored) {
            Printer.print(Messages.INVALID_COMMAND.getMessage());
        }
    }
}
