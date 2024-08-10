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

    }

    @Test
    @DisplayName("Проверка изменения статуса")
    public void testSetStatusSuccessful() throws InvalidCommandException {

    }

    @Test
    @DisplayName("Проверка на блокировку измения статуса заказа")
    public void testSetStatusInvalidCommandException() {

    }

    @Test
    @DisplayName("Тестирование уведомления пользователя")
    public void testNotifyClient() {

    }
}