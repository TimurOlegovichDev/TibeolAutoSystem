import Model.DataBase.DataBaseHandler;
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
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class DataBaseHandlerTest {

    @Test
    @DisplayName("Проверка обработчика баз данных на добавление пользователя")
    public void testAddUser() {

    }

    @Test
    @DisplayName("Проверка взаимодействия базы данных с заказами")
    public void testAddOrder() {

    }

    @Test
    @DisplayName("Проверка обработчика баз данных на добавление пользователя")
    public void testGetOrderData() {

    }

    @Test
    @DisplayName("Проверка обработчикаБД на получение корректного стрима заказов")
    public void testGetOrderStream() {

    }

    @Test
    @DisplayName("Проверка обработчика на добавление авто")
    public void testAddCar() {

    }

    @Test
    @DisplayName("Проверка обработчика на получение авто")
    public void testGetCar() throws NoSuchCarException {

    }

    @Test
    @DisplayName("Проверка обработчика на получение пользователя из базы данных")
    public void testGetUserById() throws NoSuchUserException {

    }
}