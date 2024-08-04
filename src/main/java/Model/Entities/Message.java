package Model.Entities;

import Model.Entities.Users.User;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Message {

    private User sender;
    private String message = "";
    private final String timeCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    private Message(){}

    @Override
    public String toString(){
        return (sender == null) ?
                message + "(Отправлено " + timeCreation + ")"
                :
                ("Получено от " + sender.getUserParameters().getName()  + " " + message + "(Отправлено " + timeCreation + ")");
    }

    public Message(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
