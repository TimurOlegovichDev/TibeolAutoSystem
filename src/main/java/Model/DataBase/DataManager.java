package Model.DataBase;

import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Users.User;

import java.util.Map;

public abstract class DataManager {

    public static User add(User user){
        UserData.add(user);
        return user;
    }

    public static Map<Integer, User> getUserData() {
        return UserData.getUserData();
    }

    public static Order add(Order order){
        OrderData.add(order);
        return order;
    }

    public static Car add(Car car){
        DealerCarData.add(car);
        return car;
    }

    public static Map<Integer, Car> getCarData(){
        return DealerCarData.getCarData();
    }

    public static void addCar(Car car){
        DealerCarData.add(car);
    }


}
