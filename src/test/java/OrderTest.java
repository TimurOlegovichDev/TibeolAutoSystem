import Model.Entities.Car.Car;
import Model.Entities.Message;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Order.StatusesOfOrder;
import Model.Entities.Users.Client;
import Model.Entities.Users.Manager;
import Model.Exceptions.UserExc.InvalidCommandException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    @DisplayName("Проверка основных функций, по типу получения владельца заказа, получение авто, указанного в заказе")
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
    @DisplayName("Проверка изменения статуса")
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
    @DisplayName("Проверка на блокировку измения статуса заказа")
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
    @DisplayName("Тестирование уведомления пользователя")
    public void testNotifyClient() {
        OrderTypes orderType = OrderTypes.SERVICE;
        Client owner = new Client("testClient", null);
        String orderText = "testOrderText";
        Car car = new Car(owner, "testBrand", "testModel", "dsadsadasd");
        Order order = new Order(orderType, owner, orderText, car);
        Manager manager = new Manager("testManager", null);
        Message message = new Message(manager, "testMessage");
        order.notifyClient(message, owner);
        assertEquals(owner.getMessages().size(), 1);
    }
}