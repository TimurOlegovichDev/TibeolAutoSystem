package Controller.ActionHandlers.ClientHandler;

import Controller.ActionHandlers.ShowRoomMainActions;
import Controller.Controller;
import Model.DataBaseHandler;
import Model.DataFields.ClientCarDataFields;
import Model.DataFields.DealerCarDataFields;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Users.Client;
import Model.Exceptions.UserExc.DeliberateInterruptException;
import Model.Exceptions.UserExc.InvalidInputException;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import ui.Menu;
import ui.messageSrc.Messages;
import ui.out.Printer;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientActionsInShowRoomHandler {

    static void gotoShowRoom(Client client){
        Printer.printCentered("Go to the car dealership page");
        distribute();
    }

    public static void distribute() {
        switch (Menu.clientChoosingActionInShowRoom()) {
            case VIEW_ALL_CARS ->
                    Printer.printDealerCars(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
            case CREATE_PURCHASE_ORDER -> createPurchaseOrder((Client) Controller.getController().getCurrentUser());
            case CREATE_SERVICE_ORDER -> createServiceOrder((Client) Controller.getController().getCurrentUser());
            case SEARCH_CAR -> ShowRoomMainActions.getFilterList();
            case BACK -> {
                Printer.printCentered("Возврат на предыдущую страницу");
                return;
            }
        }
        distribute();
    }


    private static void createServiceOrder(Client client) {
        try {
            ClientMainActionHandler.viewUserCars(client);
            List<String> listValidId = DataBaseHandler.getColumnByField(DataBaseHandler.clientsCarTableName, ClientCarDataFields.ID, " WHERE client_id = " + client.getID());
            Printer.print("Для заказа на обслуживание, введите ID вашего авто из списка, если нужного автомобиля нет, воспользуйтесь действием \"Добавить автомобиль\" (для отмены введите любое слово): ");
            int id = Menu.tryGetNumberFromUser();
            if (!listValidId.contains(String.valueOf(id)))
                throw new NoSuchElementException();
            List<String> carRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.clientsCarTableName, id);
            if (!Menu.areYouSure("Вы выбрали автомобиль " + carRow.get(ClientCarDataFields.BRAND.getIndex()) + " " + carRow.get(ClientCarDataFields.MODEL.getIndex()) + "?"))
                createServiceOrder(client);
            client.createServiceOrder(Menu.getText("Сообщите, по какой причине вы хотите обслужить авто: "), Integer.parseInt(carRow.get(ClientCarDataFields.ID.getIndex())));
            Printer.print("Заказ на обслуживание успешно создан и передан в автосалон");
            Controller.logger.log(Levels.INFO, LogActions.NEW_SERVICE_ORDER.getText() + OrderTypes.SERVICE);
        } catch (InvalidInputException | DeliberateInterruptException e) {
            Printer.print(Messages.RETURN.getMessage());
        } catch (NoSuchElementException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            createServiceOrder(client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createPurchaseOrder(Client client) {
        try {
            Printer.print(DataBaseHandler.getData(DataBaseHandler.dealerCarTableName));
            Printer.print("Введите ID интересующего вас автомобиля (для отмены введите любое слово): ");
            int id = Menu.tryGetNumberFromUser();
            List<String> carRow = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.dealerCarTableName, id);
            if (!Menu.areYouSure(
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
            Controller.logger.log(Levels.INFO, LogActions.NEW_SERVICE_ORDER.getText() + OrderTypes.PURCHASE);
        } catch (InvalidInputException e) {
            Printer.print(Messages.RETURN.getMessage());
        } catch (NoSuchElementException e) {
            Printer.print(Messages.NO_SUCH_ELEMENT.getMessage());
            createPurchaseOrder(client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
