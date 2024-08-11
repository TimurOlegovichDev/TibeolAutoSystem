import Model.DataBase.DataBaseHandler;
import Model.DataBase.DataFields.UsersDataFields;
import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import Model.Entities.Order.OrderTypes;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import Model.Exceptions.UserExc.NoSuchUserException;
import Model.LoggerUtil.Levels;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class DatabaseTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest");

    @Test
    @DisplayName("Проверка подключения к базе данных")
    void testDatabaseConnection() throws SQLException {
        BasicConfigurator.configure();

        String url = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT 1")) {
                resultSet.next();
                int result = resultSet.getInt(1);
                Assertions.assertEquals(1, result);
            }
        }
    }

    @Test
    @DisplayName("Проверка создания таблиц бд")
    void testDataBaseMigration() {
        BasicConfigurator.configure();

        String url = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        Assertions.assertDoesNotThrow(() -> migration(url, username, password));
    }

    public static void migration(String url, String username, String password) throws Exception {
        Connection connection = DriverManager.getConnection(url, username, password);
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(connection)
                );
        Liquibase liquibase =
                new Liquibase(
                        "db/changelog/v.1.0/changelog.xml",
                        new ClassLoaderResourceAccessor(),
                        database
                );
        liquibase.update();
        connection.close();
    }

    @Test
    void testGetData() {
        List<List<String>> data = DataBaseHandler.getData(DataBaseHandler.usersTableName);
        Assertions.assertNotNull(data);
        Assertions.assertFalse(data.isEmpty());
    }

    @Test
    void testAddUser() {
        Client user = new Client("John Doe", null);
        User addedUser = DataBaseHandler.add(user);
        Assertions.assertNotNull(addedUser);
        Assertions.assertEquals(user.getName(), addedUser.getName());
    }

    @Test
    void testGetTableByFieldAndValue() {
        Client user = new Client("John Doe", null);
        DataBaseHandler.add(user);
        List<List<String>> data = DataBaseHandler.getTableByFieldAndValue(DataBaseHandler.usersTableName, user.getID(), UsersDataFields.ID);
        Assertions.assertNotNull(data);
        Assertions.assertFalse(data.isEmpty());
    }

    @Test
    void testGetCurrentUserParametersList() throws NoSuchUserException {
        Client user = new Client("John Doe", null);
        List<String> parameters = DataBaseHandler.getCurrentUserParametersList(user);
        Assertions.assertNotNull(parameters);
        Assertions.assertEquals(user.getName(), parameters.get(UsersDataFields.NAME.getIndex()));
    }

    @Test
    void testGetColumnByField() throws SQLException {
        List<String> column = DataBaseHandler.getColumnByField(DataBaseHandler.usersTableName, UsersDataFields.ID);
        Assertions.assertNotNull(column);
        Assertions.assertFalse(column.isEmpty());
    }

    @Test
    void testSetParameterById() throws SQLException {
        Client user = new Client("John Doe", null);
        DataBaseHandler.add(user);
        DataBaseHandler.setParameterById("name", DataBaseHandler.usersTableName, "New Name", user.getID());
        List<String> parameters = DataBaseHandler.getRowByIdFromTable(DataBaseHandler.usersTableName, user.getID());
        Assertions.assertNotNull(parameters);
        Assertions.assertEquals("New Name", parameters.get(UsersDataFields.NAME.getIndex()));
    }

    @Test
    void testRemoveRowById() {
        DataBaseHandler.removeRowById("users", 1);
        List<List<String>> data = DataBaseHandler.getData("users");
        Assertions.assertNotNull(data);
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    void testAddOrder() {
        Order order = new Order(OrderTypes.SERVICE, new Client("John Doe", null),  "Description", 1);
        Order addedOrder = DataBaseHandler.add(order);
        Assertions.assertNotNull(addedOrder);
        Assertions.assertEquals(order.getOrderType(), addedOrder.getOrderType());
    }

    @Test
    void testAddDealerCar() {
        Car car = new Car("Toyota", "Camry", "Black", 2015, 20000, "Description", 50000);
        DataBaseHandler.addDealerCar(car);
        List<List<String>> cars = DataBaseHandler.getData(DataBaseHandler.dealerCarTableName);
        Assertions.assertNotNull(cars);
        Assertions.assertFalse(cars.isEmpty());
    }

    @Test
    void testAddClientCar() {
        Car car = new Car("Toyota", "Camry", "Black", 2015, 20000, "Description", 50000);
        DataBaseHandler.addClientCar(car, 1);
        List<List<String>> cars = DataBaseHandler.getData(DataBaseHandler.clientsCarTableName);
        Assertions.assertNotNull(cars);
        Assertions.assertFalse(cars.isEmpty());
    }

    @Test
    void testLogMessage() {
        DataBaseHandler.logMessage(Levels.INFO, "Test message", "2022-01-01 12:00:00");
        List<List<String>> logs = DataBaseHandler.getData(DataBaseHandler.logsTableName);
        Assertions.assertNotNull(logs);
        Assertions.assertFalse(logs.isEmpty());
    }
}