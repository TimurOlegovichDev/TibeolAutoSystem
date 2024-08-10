package Model.DataBase;

import Model.DataBase.dbconfig.DataBaseConfiguration;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.InvalidPasswordException;
import Model.Exceptions.UserExc.NoSuchUserException;
import org.jetbrains.annotations.Nullable;
import ui.out.Printer;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Предоставляет статические методы для работы с данными в базе данных.
 * Он сделан для удобства, чтобы не требовалось обращаться к другим абстрактным классам, который работают с базами данных
 */

public abstract class DataBaseHandler {

    public static final String clientsCarTableName = "dealer_schema.client_cars";
    public static final String clientMessagesTableName = "dealer_schema.client_messages";
    public static final String dealerCarTableName = "dealer_schema.dealer_cars";
    public static final String logsTableName = "dealer_schema.logs";
    public static final String ordersTableName = "dealer_schema.orders";
    public static final String usersTableName = "dealer_schema.users";

    public static List<List<String>> getData(String tableName) {
        try {
            return executeQuery(
                    "SELECT * FROM " + tableName
            );
        } catch (SQLException e) {
            System.out.println("Query canceled, got error" + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static User add(User user) {
        executeUpdate(
                "INSERT INTO " + usersTableName + " (name, password, phone_number, role) VALUES (?, ?, ?, ?)",
                user.getName(),
                new String(user.getPassword(), StandardCharsets.UTF_8),
                user.getPhoneNumber(),
                user.getAccessLevel().getValue()
        );
        try {
            user.setID(Integer.parseInt(
                    getCurrentUserParametersList(user)
                            .get(UsersDataFields.ID.getIndex())
            ));
        } catch (Exception e) {
            Printer.print("Произошла ошибка при изменении ID пользователя " + e.getMessage());
        }
        return user;
    }

    public static List<List<String>> МgetTableByUser(String tableName, int userId){
        try {
            return executeQuery(
                    "SELECT * FROM " + tableName +
                            " WHERE id = " + userId
            );
        } catch (SQLException e) {
            System.out.println("Query canceled, got error" + e.getMessage());
        }
        return new ArrayList<>();
    }

    private static List<String> getCurrentUserParametersList(User user) throws SQLException, NoSuchUserException {
        List<List<String>> table = DataBaseHandler.getData(usersTableName);
        for (List<String> list : table)
            if (list.contains(user.getName()))
                return list;
        throw new NoSuchUserException();
    }

    public static List<String> getColumnByField(String tableName, DataFieldImp field) throws SQLException {
        List<List<String>> lists = executeQuery("SELECT " + field.toString() + " FROM " + tableName);
        List<String> result = new ArrayList<>();
        for(List<String> list : lists)
            result.add(list.get(0));
        return result;
    }

    public static void setParameterById(String fieldName, String tableName, String newValue, int id){
        executeUpdate(
                "UPDATE " + tableName + " SET " + fieldName + " = ? WHERE id = ?",
                newValue, id
        );
    }

    public static void removeRowById(String tableName, int id){
        executeUpdate(
                "DELETE FROM " + tableName + " WHERE id = ?",
                id
        );
    }

    public static Order add(Order order) {
        executeUpdate(
                "INSERT INTO " + ordersTableName +
                        " (type, client_id, client_car_id, client_phone_number, description, status, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ",
                order.getOrderType(),
                order.getCarId(),
                order.getOwner().getPhoneNumber(),
                order.getOrderText(),
                order.getStatus(),
                order.getDateOfCreation()
        );
        return order;
    }

    public static List<List<String>> getActiveOrders() {
        try {
            return executeQuery(
                    "SELECT * FROM " + DataBaseHandler.ordersTableName +
                            " WHERE status != " + StatusesOfOrder.ARCHIVED.getCommand()
            );
        } catch (SQLException e) {
            System.out.println("Query canceled, got error" + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<List<String>> getArchivedOrders() {
        try {
            return executeQuery(
                    "SELECT * FROM " + DataBaseHandler.ordersTableName +
                            " WHERE status = " + StatusesOfOrder.ARCHIVED.getCommand()
            );
        } catch (SQLException e) {
            System.out.println("Query canceled, got error" + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static Car addDealerCar(Car car) {
        executeUpdate(
                "INSERT INTO " + dealerCarTableName +
                        " (brand, model, color, year, price, mile_age, description) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ",
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getYearOfProduction(),
                car.getPrice(),
                car.getMileAge(),
                car.getDescription()
        );
        return car;
    }

    public static void addClientCar(Car car) {
        executeUpdate(
                "INSERT INTO " + clientsCarTableName +
                        " (client, brand, model) " +
                        "VALUES (?, ?, ?) ",
                car.getOwner().getID(),
                car.getBrand(),
                car.getModel()
        );
    }

    public static List<String> getRowByIdFromTable(String tableName, int carId) throws NoSuchElementException, SQLException {
        List<List<String>> lists = executeQuery(
                "SELECT * FROM " + tableName +
                        " WHERE id = " + carId);
        if(lists.size() != 1)
            throw new NoSuchElementException();
        return lists.get(0);
    }

    private static List<List<String>> executeQuery(String query) throws SQLException {
        if (query == null || query.isEmpty())
            Printer.print("Ошибочный запрос к базе данных!");
        try (Connection connection = DriverManager.getConnection(
                DataBaseConfiguration.URL,
                DataBaseConfiguration.USER_NAME,
                DataBaseConfiguration.PASSWORD
        );
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            List<List<String>> result = new ArrayList<>();
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++)
                    row.add(resultSet.getString(i));
                result.add(row);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private static void executeUpdate(String query, Object... params) {
        try (Connection connection = DriverManager.getConnection(DataBaseConfiguration.URL,
                DataBaseConfiguration.USER_NAME,
                DataBaseConfiguration.PASSWORD
        )) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                preparedStatement.setObject(i + 1, params[i]);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " executeUpdateError");
        }
    }

    private static Object execute(String query) {
        try (Connection connection = DriverManager.getConnection(DataBaseConfiguration.URL,
                DataBaseConfiguration.USER_NAME,
                DataBaseConfiguration.PASSWORD
        )) {
            Statement statement = connection.createStatement();
            return statement.execute(query);
        } catch (SQLException e) {
            Printer.print("SQL ERROR EXCEPTION!");
        }
        return null;
    }

    public static List<String> getUserParamById(int id) throws SQLException, NoSuchUserException {
        List<List<String>> lists = executeQuery(
                "SELECT * FROM " + usersTableName +
                " WHERE id = " + id);
        if(lists.size() != 1)
            throw new NoSuchUserException();
        return lists.get(0);
    }

    public static void checkOrderAndArchive() {
        executeUpdate(
                "UPDATE " + ordersTableName + " SET ? = ? WHERE status IN (?, ?, ?)",
                OrderDataFields.STATUS,
                StatusesOfOrder.ARCHIVED,
                StatusesOfOrder.COMPLETED,
                StatusesOfOrder.AGREED,
                StatusesOfOrder.DISMISSED
        );
    }
}
