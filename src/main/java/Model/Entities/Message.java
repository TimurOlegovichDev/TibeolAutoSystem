package Model.Entities;

import Model.Entities.Users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Message {

    private User sender;
    private String message;

    private Message(){}
}
