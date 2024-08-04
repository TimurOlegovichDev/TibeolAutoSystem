package Model.Entities;

import Model.Entities.Users.User;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Message {

    private User sender;
    private String message = "";
    private final String timeCreation = formatInstant(Instant.now());

    private static String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault(); // или другой часовой пояс, если нужно
        return formatter.format(instant.atZone(zoneId));
    }

    private Message(){}

    @Override
    public String toString(){
        return (sender == null) ?
                message + "(Отправлено " + timeCreation + ")"
                :
                ("Получено от " + sender.getAccessLevel().getValue() + "а " + sender.getUserParameters().getName()  + " " + message + "(Отправлено " + timeCreation + ") \n");
    }

    public Message(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
