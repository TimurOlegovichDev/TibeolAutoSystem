package ui.messageSrc;

import Model.DataBase.dbconfig.DataBaseConfiguration;
import lombok.Getter;
import java.sql.*;

/**
 * Перечисление крупных сообщений, сделано для удобства и лаконичности кода
 */

@Getter
public enum Messages {

    GREETING(getFromDB("GREETING")),
    ACTIONS_TO_ENTER(getFromDB("ACTIONS_TO_ENTER")),
    ENTER_MESSAGE(getFromDB("ENTER_MESSAGE")),
    ENTER_TEXT_TO_ORDER(getFromDB("ENTER_TEXT_TO_ORDER")),
    RETURN(getFromDB("RETURN")),
    NO_SUCH_ELEMENT(getFromDB("NO_SUCH_ELEMENT")),
    CHOOSE_U_ACTION(getFromDB("CHOOSE_U_ACTION")),
    ENTER_NAME(getFromDB("ENTER_NAME")),
    ERROR(getFromDB("ERROR")),
    USER_ALREADY_EXISTS(getFromDB("USER_ALREADY_EXISTS")),
    EXIT(getFromDB("EXIT")),
    ENTER_PASSWORD(getFromDB("ENTER_PASSWORD")),
    ENTER_PHONE_NUMBER(getFromDB("ENTER_PHONE_NUMBER")),
    NEW_MESSAGE(getFromDB("NEW_MESSAGE")),
    CHOOSE_ROLE(getFromDB("CHOOSE_ROLE")),
    CAR_REGISTER(getFromDB("CAR_REGISTER")),
    SHUT_DOWN_WARNING(getFromDB("SHUT_DOWN_WARNING")),
    LOG_OUT_WARNING(getFromDB("LOG_OUT_WARNING")),
    DELETE_ACCOUNT_WARNING(getFromDB("DELETE_ACCOUNT_WARNING")),
    INVALID_COMMAND(getFromDB("INVALID_COMMAND")),
    INVALID_PASS(getFromDB("INVALID_PASS")),
    NO_SUCH_USER(getFromDB("NO_SUCH_USER")),
    START(getFromDB("START")),
    EXAMPLE(getFromDB("EXAMPLE")),
    END(getFromDB("END"));

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    /**
     * Так как все сообщения хранятся в базе данных, то в конструкторе получаем нужное сообщение
     */

    public static String getFromDB(String id){
        try (Connection connection = DriverManager.getConnection(DataBaseConfiguration.URL,DataBaseConfiguration.USER_NAME,DataBaseConfiguration.PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT message FROM service_schema.messages WHERE id = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            String message = null;
            if (resultSet.next())
                message = resultSet.getString("message");
            return message;
        } catch (SQLException e) {
            return "Здесь должно быть сообщение, но SQL нас подвела, вот так: " + e.getMessage();
        }
    }
}
