package Model.DataBase;

import Model.DataBase.dbconfig.DataBaseConfiguration;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
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
 *
 * @see OrderDataBase
 * @see DealerCarData
 */

public abstract class DataBaseHandler {

    public static final String clientsCarTableName = "dealer_schema.client_cars";
    public static final String clientMessagesTableName = "dealer_schema.clientMessages";
    public static final String dealerCarTableName = "dealer_schema.dealerCars";
    public static final String logsTableName = "dealer_schema.logs";
    public static final String ordersTableName = "dealer_schema.orders";
    public static final String usersTableName = "dealer_schema.users";


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

    private static List<String> getCurrentUserParametersList(User user) throws SQLException, NoSuchUserException {
        List<List<String>> table = DataBaseHandler.getUsersData();
        if (table == null)
            throw new SQLException();
        for (List<String> list : table)
            if (list.contains(user.getName()))
                return list;
        throw new NoSuchUserException();
    }

    public static List<List<String>> getUsersData() {
        try {
            return executeQuery(
                    "SELECT * FROM " + usersTableName
            );
        } catch (SQLException e) {
            System.out.println("Error");
        }
        return null;
    }

    public static List<String> getUsersColumnByField(UsersDataFields field) throws SQLException {
        List<List<String>> lists = executeQuery("SELECT " + field.toString() + " FROM " + usersTableName);
        List<String> result = new ArrayList<>();
        for(List<String> list : lists)
            result.add(list.get(0));
        return result;
    }

    public static void setParameterById(String fieldName, String tableName, String newValue, int id){
        executeUpdate(
                "UPDATE " + tableName +
                        " SET " + fieldName + " = " + "'"+ newValue + "'" +
                        " WHERE id = " + id
        );
    }

    public static void removeRowById(String tableName, int id){
        executeUpdate(
                "DELETE FROM " + tableName + " " +
                        "WHERE id = " + id
        );
    }

    public static Order add(Order order) {
        OrderDataBase.add(order);
        return order;
    }

    public static void remove(Order order) {
        OrderDataBase.remove(order);
    }

    public static List<Order> getOrderData() {
        return OrderDataBase.getOrders();
    }

    public static Stream<Order> getOrderStream() {
        return OrderDataBase.getOrders().stream();
    }

    public static Car add(Car car) {
        DealerCarData.add(car);
        return car;
    }

    public static Car getCar(int id) throws NoSuchCarException, NoSuchElementException {
        Car car = DealerCarData.getCarData().get(id);
        if (car == null) throw new NoSuchCarException();
        return car;
    }

    public static void remove(Car car) {
        DealerCarData.remove(car);
    }

    public static Map<Integer, Car> getCarData() {
        return DealerCarData.getCarData();
    }

    public static void addCar(Car car) {
        DealerCarData.add(car);
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
            throw new SQLException("Error executing query: " + e.getMessage(), e);
        }
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
            System.out.println(e.getMessage());
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
}
