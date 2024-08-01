package Model.DataBase;

import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Users.User;

public abstract class DataManager {
    public static User add(User user){
        UserData.add(user);
        return user;
    }
    public static void addOrder(Order order){
        OrderData.add(order);
    }
    public static void addCar(Car car){
        DealerCarData.add(car);
    }
}
