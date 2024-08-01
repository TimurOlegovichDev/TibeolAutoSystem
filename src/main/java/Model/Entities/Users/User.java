package Model.Entities.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
public abstract class User {
    private UserData userData;
    private AccessLevels accessLevel;
    User(String name, String password){
        this.userData = new UserData(name, password);
    }

}
