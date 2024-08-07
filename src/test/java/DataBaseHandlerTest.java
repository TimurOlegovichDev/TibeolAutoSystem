import Model.DataBase.DataBaseHandler;
import Model.DataBase.UserDataBase;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.NoSuchCarException;
import Model.Exceptions.UserExc.NoSuchUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.SoftAssertions;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class DataBaseHandlerTest {

    @Test
    @DisplayName("Проверка обработчика баз данных на добавление пользователя")
    public void testAddUser() {
        User user = new Client("testUser", null);
        int sizeDB = UserDataBase.getCredentials().size();
        User addedUser = DataBaseHandler.add(user);
        assertEquals(UserDataBase.getCredentials().size(), sizeDB+1);
    }

    @Test
    @DisplayName("Проверка взаимодействия базы данных с заказами")
    public void testAddOrder() {
        Order order = new Order(OrderTypes.SERVICE, new Client("testClient", null), "testOrderText", new Car(new Client("sdadfada", null), "testBrand", "testModel", "sdasdas"));
        Order addedOrder = DataBaseHandler.add(order);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(addedOrder).isNotNull();
        softly.assertThat(addedOrder).isEqualTo(order);
        softly.assertAll();
    }

    @Test
    @DisplayName("Проверка обработчика баз данных на добавление пользователя")
    public void testGetOrderData() {
        List<Order> orderData = DataBaseHandler.getOrderData();
        assertNotNull(orderData);
    }

    @Test
    @DisplayName("Проверка обработчикаБД на получение корректного стрима заказов")
    public void testGetOrderStream() {
        Stream<Order> orderStream = DataBaseHandler.getOrderStream();
        List<Order> orders = DataBaseHandler.getOrderData();
        assertEquals(orders, DataBaseHandler.getOrderStream().toList());
    }

    @Test
    @DisplayName("Проверка обработчика на добавление авто")
    public void testAddCar() {
        Car car = new Car(new Client("sdadfada", null), "testBrand", "testModel", "sdasdas");
        Car addedCar = DataBaseHandler.add(car);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(addedCar).isNotNull();
        softly.assertThat(addedCar).isEqualTo(car);
        softly.assertAll();
    }

    @Test
    @DisplayName("Проверка обработчика на получение авто")
    public void testGetCar() throws NoSuchCarException {
        Car car = new Car(new Client("sdadfada", null), "testBrand", "testModel", "sdasdas");
        DataBaseHandler.add(car);
        Car retrievedCar = DataBaseHandler.getCar(car.getID());
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(retrievedCar).isNotNull();
        softly.assertThat(retrievedCar).isEqualTo(car);
        softly.assertAll();
    }

    @Test
    @DisplayName("Проверка обработчика на получение пользователя из базы данных")
    public void testGetUserById() throws NoSuchUserException {
        User user = new Client("testUser", null);
        DataBaseHandler.add(user);
        User retrievedUser = DataBaseHandler.getUserById(user.getUserParameters().getID());
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(retrievedUser).isNotNull();
        softly.assertThat(retrievedUser).isEqualTo(user);
        softly.assertAll();
    }
}