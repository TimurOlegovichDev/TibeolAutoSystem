import Model.DataBase.DataBaseHandler;
import Model.DataBase.UserDataBase;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.NoSuchUserException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseHandlerTest {

    @Test
    public void testAddUser() {

        User user = new Client("testUser", null);

        User addedUser = DataBaseHandler.add(user);

        assertNotNull(addedUser);
        assertEquals(user, addedUser);
    }


    @Test
    public void testGetUserData() {

        Map<Integer, User> userData = DataBaseHandler.getUserData();

        assertNotNull(userData);
    }

    @Test
    public void testAddOrder() {
        Order order = new Order(OrderTypes.SERVICE, new Client("testClient", null), "testOrderText", new Car(new Client("sdadfada", null), "testBrand", "testModel", "sdasdas"));
        Order addedOrder = DataBaseHandler.add(order);
        assertNotNull(addedOrder);
        assertEquals(order, addedOrder);
    }


    @Test
    public void testGetOrderData() {

        List<Order> orderData = DataBaseHandler.getOrderData();

        assertNotNull(orderData);
    }

    @Test
    public void testGetOrderStream() {

        Stream<Order> orderStream = DataBaseHandler.getOrderStream();

        assertNotNull(orderStream);
    }

    @Test
    public void testAddCar() {

        Car car = new Car(new Client("sdadfada", null), "testBrand", "testModel", "sdasdas");

        Car addedCar = DataBaseHandler.add(car);

        assertNotNull(addedCar);
        assertEquals(car, addedCar);
    }

    @Test
    public void testGetCar() throws NoSuchCarException {

        Car car = new Car(new Client("sdadfada", null), "testBrand", "testModel", "sdasdas");
        DataBaseHandler.add(car);

        Car retrievedCar = DataBaseHandler.getCar(car.getID());

        assertNotNull(retrievedCar);
        assertEquals(car, retrievedCar);
    }


    @Test
    public void testGetCarData() {

        Map<Integer, Car> carData = DataBaseHandler.getCarData();
        assertNotNull(carData);
    }

    @Test
    public void testGetUserById() throws NoSuchUserException {
        User user = new Client("testUser", null);
        DataBaseHandler.add(user);

        User retrievedUser = DataBaseHandler.getUserById(user.getUserParameters().getID());

        assertNotNull(retrievedUser);
        assertEquals(user, retrievedUser);
    }
}