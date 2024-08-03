package Model.DataBase;


import Model.Entities.Order.Order;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class OrderDataBase {

    private static final List<Order> orders = new ArrayList<>();

//    public static List<Order> getSortedByName(){
//         List<Order> sortedList = new ArrayList<>();
//         Collections.copy(sortedList, orders);
//         sortedList.sort(Comparator.comparing(o -> o.getOwner().getUserData().getName()));
//         return sortedList;
//    }

    protected static void add(Order o){
        orders.add(o);
    }

    protected static void remove(Order o){
        orders.remove(o);
    }

    protected static List<Order> getOrders(){
        return new ArrayList<>(orders);
    }
}
