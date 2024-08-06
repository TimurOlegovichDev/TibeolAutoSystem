import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Entities.Users.Client;
import Model.Entities.Users.Manager;
import Model.Exceptions.UserExc.InvalidCommandException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void testConstructorSuccessful() {
        OrderTypes orderType = OrderTypes.SERVICE;
        Client owner = new Client("testClient", null);
        String orderText = "testOrderText";
        Car car = new Car(owner, "testBrand", "testModel", "dsadsadasd");

        Order order = new Order(orderType, owner, orderText, car);

        assertNotNull(order);
        assertEquals(orderType, order.getOrderType());
        assertEquals(owner, order.getOwner());
        assertEquals(orderText, order.getOrderText());
        assertEquals(car, order.getCar());
    }

    @Test
    public void testSetStatusSuccessful() throws InvalidCommandException {
        OrderTypes orderType = OrderTypes.SERVICE;
        Client owner = new Client("testClient", null);
        String orderText = "testOrderText";
        Car car = new Car(owner, "testBrand", "testModel", "dsadsadasd");
        Order order = new Order(orderType, owner, orderText, car);
        Manager manager = new Manager("testManager",null);
        StatusesOfOrder newStatus = StatusesOfOrder.EXECUTING;
        order.setStatus(manager, newStatus, false);
        assertEquals(newStatus, order.getStatus());
    }

    @Test
    public void testSetStatusAutomatic() throws InvalidCommandException {
        OrderTypes orderType = OrderTypes.SERVICE;
        Client owner = new Client("testClient",null);
        String orderText = "testOrderText";
        Car car = new Car(owner, "testBrand", "testModel", "dsadsadasd");
        Order order = new Order(orderType, owner, orderText, car);
        Manager manager = new Manager("testManager", null);
        StatusesOfOrder newStatus = StatusesOfOrder.ARCHIVED;
        order.setStatus(manager, newStatus, true);

        assertEquals(newStatus, order.getStatus());
    }

    @Test
    public void testSetStatusInvalidCommandException() {
        OrderTypes orderType = OrderTypes.SERVICE;
        Client owner = new Client("testClient", null);
        String orderText = "testOrderText";
        Car car = new Car(owner, "testBrand", "testModel", "dsadsadasd");
        Order order = new Order(orderType, owner, orderText, car);
        Manager manager = new Manager("testManager", null);
        StatusesOfOrder newStatus = StatusesOfOrder.NEW;
        assertThrows(InvalidCommandException.class, () -> order.setStatus(manager, newStatus, false));
    }

    @Test
    public void testNotifyClient() {
        OrderTypes orderType = OrderTypes.SERVICE;
        Client owner = new Client("testClient", null);
        String orderText = "testOrderText";
        Car car = new Car(owner, "testBrand", "testModel", "dsadsadasd");
        Order order = new Order(orderType, owner, orderText, car);
        Manager manager = new Manager("testManager", null);
        Message message = new Message(manager, "testMessage");
        order.notifyClient(message, owner);
    }
}