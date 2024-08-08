package Model.DataBase;

import Model.DataBase.dbconfig.DataBaseConfiguration;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.NoSuchUserException;
import liquibase.structure.core.PrimaryKey;
import ui.out.Printer;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Предоставляет статические методы для работы с данными в базе данных.
 * Он сделан для удобства, чтобы не требовалось обращаться к другим абстрактным классам, который работают с базами данных
 * @see OrderDataBase
 * @see UserDataBase
 * @see DealerCarData
 */

public abstract class DataBaseHandler {

    private static final String carTableName = "dealer-car";

    public static User add(User user){
        executeUpdate("INSERT INTO");
        UserDataBase.add(user);
        return user;
    }

    public static void remove(User user){
        UserDataBase.remove(user.getUserParameters().getID());
    }

    public static Map<Integer, User> getUserData() {
        return UserDataBase.getUserData();
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

    private static void executeUpdate(String query){
        try (Connection connection = DriverManager.getConnection(DataBaseConfiguration.URL,
                DataBaseConfiguration.USER_NAME,
                DataBaseConfiguration.PASSWORD
        )) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            Printer.print("SQL ERROR EXCEPTION!");
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
