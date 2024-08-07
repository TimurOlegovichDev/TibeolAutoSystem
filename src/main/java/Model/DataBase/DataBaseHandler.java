package Model.DataBase;

import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.NoSuchUserException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Предоставляет статические методы для работы с данными в базе данных.
 * Он сделан для удобства, чтобы не требовалось обращаться к другим абстрактным классам, который работают с базами данных
 * @see OrderDataBase
 * @see UserDataBase
 * @see DealerCarData
 */

public abstract class DataBaseHandler {

    public static User add(User user){
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
}
