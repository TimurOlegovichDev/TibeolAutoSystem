package Model.DataBase;


import Model.Entities.Order.Order;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class OrderDataBase {

    private static final List<Order> orders = new ArrayList<>();

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
