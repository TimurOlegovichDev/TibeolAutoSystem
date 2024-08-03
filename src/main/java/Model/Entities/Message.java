package Model.Entities;

import Model.Entities.Users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@AllArgsConstructor
public class Message {

    private User sender;
    private String message = "";

    private Message(){}

    @Override
    public String toString(){
        return (sender == null) ?
                message
                :
                ("Получено от " + sender.getUserParameters().getName()  + " " + message);
    }
}
