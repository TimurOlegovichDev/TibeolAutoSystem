package Model.Entities.Users;

import Model.DataBase.DataManager;
import Model.DataBase.DealerCarData;
import Model.DataBase.OrderData;
import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class Client extends User{

    @Getter
    private List<Order> orderList = new ArrayList<>();
    @Getter
    private List<Car> carList = new ArrayList<>();
    @Getter
    private Queue<Message> messages = new ArrayDeque<>();

    public Client(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.CLIENT);
    }

    public void readMessage(){
        //todo
    }

    public void receiveMessage(Message message){
        messages.add(message);
    }

    public void createServiceOrder(String text, int carId){
        DataManager.add(new Order(OrderTypes.SERVICE, this, text, DataManager.getCarData().get(carId)));
    }

    public void createPurchaseOrder(String text, int carId){
        DataManager.add(new Order(OrderTypes.PURCHASE, this, text, DataManager.getCarData().get(carId)));
    }
}
