package Model.DataBase;

import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;

import java.util.List;
import java.util.Map;

public abstract class DataBaseHandler {

    public static User add(User user){
        UserDataBase.add(user);
        return user;
    }

    public static User remove(User user){
        UserDataBase.remove(user.getUserParameters().getID());
        return user;
    }

    public static Map<Integer, User> getUserData() {
        return UserDataBase.getUserData();
    }

    public static Order add(Order order){
        OrderDataBase.add(order);
        return order;
    }

    public static Order remove(Order order){
        OrderDataBase.remove(order);
        return order;
    }

    public static List<Order> getOrderData() {
        return OrderDataBase.getOrders();
    }


    public static Car add(Car car){
        DealerCarData.add(car);
        return car;
    }

    public static Car getCar(int id) throws NoSuchCarException {
        Car car = DealerCarData.getCarData().get(id);
        if(car == null) throw new NoSuchCarException();
        return car;
    }

    public static Car remove(Car car){
        DealerCarData.remove(car);
        return car;
    }

    public static Map<Integer, Car> getCarData(){
        return DealerCarData.getCarData();
    }

    public static void addCar(Car car){
        DealerCarData.add(car);
    }


}
