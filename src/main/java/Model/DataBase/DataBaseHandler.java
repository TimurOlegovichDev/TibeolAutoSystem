package Model.DataBase;

import Model.DataBase.dbconfig.DataBaseConfiguration;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.NoSuchUserException;
import org.jetbrains.annotations.Nullable;
import ui.out.Printer;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Предоставляет статические методы для работы с данными в базе данных.
 * Он сделан для удобства, чтобы не требовалось обращаться к другим абстрактным классам, который работают с базами данных
 * @see OrderDataBase
 * @see UserDataBase
 * @see DealerCarData
 */

public abstract class DataBaseHandler {

    private static final String clientsCarTableName = "dealer_schema.client_cars";
    private static final String clientMessagesTableName = "dealer_schema.clientMessages";
    private static final String dealerCarTableName = "dealer_schema.dealerCars";
    private static final String logsTableName = "dealer_schema.logs";
    private static final String ordersTableName = "dealer_schema.orders";
    private static final String usersTableName = "dealer_schema.users";


    public static User add(User user){
        executeUpdate(
                "INSERT INTO " + usersTableName + " (name, password, phone_number, role) VALUES (?, ?, ?, ?)",
                user.getUserParameters().getName(),
                user.getUserParameters().getPassword(),
                user.getPhoneNumber(),
                user.getAccessLevel().getValue()
        );
        return user;
    }

    public static void remove(User user){
        executeUpdate(
                "DELETE FROM " + usersTableName + " " +
                        "WHERE id = " + user.getUserParameters().getID()
        );
    }

    public static List<List<Object>> getUsers()  {
        try {
            return convertResultSetTo2DList(executeQuery(
                    "SELECT id, name, phone_number, role " +
                            "FROM " + usersTableName
            ));
        } catch (SQLException e){
            System.out.println("Error");
        }
        return null;
    }

    public static Order add(Order order){
        OrderDataBase.add(order);
        return order;
    }

    public static void remove(Order order){
        OrderDataBase.remove(order);
    }

    public static List<Order> getOrderData() {
        return OrderDataBase.getOrders();
    }

    public static Stream<Order> getOrderStream() {
        return OrderDataBase.getOrders().stream();
    }

    public static Car add(Car car){
        DealerCarData.add(car);
        return car;
    }

    public static Car getCar(int id) throws NoSuchCarException, NoSuchElementException {
        Car car = DealerCarData.getCarData().get(id);
        if(car == null) throw new NoSuchCarException();
        return car;
    }

    public static void remove(Car car){
        DealerCarData.remove(car);
    }

    public static Map<Integer, Car> getCarData(){
        return DealerCarData.getCarData();
    }

    public static void addCar(Car car){
        DealerCarData.add(car);
    }

    public static User getUserById(int id) throws NoSuchUserException {
        if(!UserDataBase.getUserData().containsKey(id))
            throw new NoSuchUserException();
        return UserDataBase.getUserData().get(id);
    }

    public static List<List<Object>> convertResultSetTo2DList(@Nullable ResultSet resultSet) throws SQLException {
        if(resultSet == null) return null;
        List<List<Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
                row.add(resultSet.getObject(i));
            list.add(row);
        }
        return list;
    }


    private static ResultSet executeQuery(String query){
        try (Connection connection = DriverManager.getConnection(DataBaseConfiguration.URL,
                DataBaseConfiguration.USER_NAME,
                DataBaseConfiguration.PASSWORD
        )) {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            Printer.print("SQL ERROR EXCEPTION!");
        }
        return null;
    }

    private static void executeUpdate(String query, Object ... params) {
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

    private static Object execute(String query){
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
}
