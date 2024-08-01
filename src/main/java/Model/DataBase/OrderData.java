package Model.DataBase;


import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
public abstract class OrderData{

    private static final List<Order> orders = new ArrayList<>();

//    public static List<Order> getSortedByName(){
//         List<Order> sortedList = new ArrayList<>();
//         Collections.copy(sortedList, orders);
//         sortedList.sort(Comparator.comparing(o -> o.getOwner().getUserData().getName()));
//         return sortedList;
//    }

    public static void add(Order o){
        orders.add(o);
    }
}
