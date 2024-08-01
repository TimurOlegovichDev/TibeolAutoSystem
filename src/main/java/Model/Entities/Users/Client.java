package Model.Entities.Users;

import Model.Entities.Message;
import Model.Entities.Order.Order;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class Client extends User{

    private List<Order> orderList = new ArrayList<>();
    private Queue<Message> messages = new ArrayDeque<>();

    Client(String name, String password){
        super(name, password);
        setAccessLevel(AccessLevels.CLIENT);
    }

    public void readMessage(){
        //todo
    }

    public void receiveMessage(Message message){
        messages.add(message);
    }
}
