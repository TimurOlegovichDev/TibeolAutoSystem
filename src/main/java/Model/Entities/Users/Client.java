package Model.Entities.Users;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Exceptions.UserExc.InvalidCommandException;
import lombok.Data;
import lombok.Getter;
import ui.out.Printer;

import java.io.Console;
import java.util.*;

@Getter
public final class Client extends User{

    private List<Order> orderList = new ArrayList<>();
    private final Map<Integer, Car> carData = new HashMap<>();
    private List<Message> messages = new ArrayList<>();

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

    public void receiveMessage(Message message){
        messages.add(message);
    }

    public void createServiceOrder(String text, int carId){
        Order newOrder = new Order(OrderTypes.SERVICE, this, text, carData.get(carId));
        DataBaseHandler.add(newOrder);
        orderList.add(newOrder);
    }

    public void createPurchaseOrder(String text, int carId){
        Order newOrder = new Order(OrderTypes.PURCHASE, this, text, DataBaseHandler.getCarData().get(carId));
        DataBaseHandler.add(newOrder);
        orderList.add(newOrder);
    }

    public Car getCarByID(int id) throws NoSuchElementException{
        return carData.get(id);
    }

    public void addCar(Car car){
        carData.put(car.getID(), car);
    }

    public void buyCar(Car car){
        carData.put(car.getID(), car);
        receiveMessage(new Message(null, "Поздравляем с приобретением автомобиля!"));
    }

    public void removeCar(Integer id) {
        carData.remove(id);
    }

    public void checkOrderToArchive(){
        for (Order order : orderList){
            if(order.getStatus().equals(StatusesOfOrder.COMPLETED) ||
                    order.getStatus().equals(StatusesOfOrder.AGREED) ||
                    order.getStatus().equals(StatusesOfOrder.DISMISSED)) {

                try {
                    order.setStatus(null, StatusesOfOrder.ARCHIVED, true);
                    orderList.remove(order);
                } catch (InvalidCommandException ignored) {}

            }
        }
    }
}
