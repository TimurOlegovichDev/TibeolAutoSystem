package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import lombok.Data;
import lombok.Getter;
import ui.out.Printer;

import java.util.*;

@Getter
public final class Client extends User{

    private List<Order> orderList = new ArrayList<>();
    private final Map<Integer, Car> carData = new HashMap<>();
    private Queue<Message> messages = new ArrayDeque<>();

    public Client(String name, byte[] password){
        super(name, password);
        setAccessLevel(AccessLevels.CLIENT);
    }

    @Override
    public void removeAccount(){
        for(Order order : orderList){
            if(!order.getStatus().equals(StatusesOfOrder.ARCHIVED))
                DataBaseHandler.remove(order);
        }
        DataBaseHandler.remove(this);
    }



    public void readMessage(){
        Message message = messages.poll();
        if(message != null)
            Printer.print(message.toString());
    }

    public void receiveMessage(Message message){
        messages.add(message);
    }

    public void createServiceOrder(String text, int carId){
        DataBaseHandler.add(new Order(OrderTypes.SERVICE, this, text, DataBaseHandler.getCarData().get(carId)));
    }

    public void createPurchaseOrder(String text, int carId){
        DataBaseHandler.add(new Order(OrderTypes.PURCHASE, this, text, DataBaseHandler.getCarData().get(carId)));
    }
    public void addCar(Car car){
        carData.put(car.getID(), car);
        receiveMessage(new Message(null, "Поздравляем с приобретением автомобиля!!!"));
    }
    public void removeCar(Integer id) {
        carData.remove(id);
    }
}
