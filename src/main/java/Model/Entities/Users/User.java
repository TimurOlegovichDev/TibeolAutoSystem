package Model.Entities.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
public abstract class User {

    private final UserParameters userData;
    @Setter
    private AccessLevels accessLevel;
    User(String name, String password){
        this.userData = new UserParameters(name, password);
    }

    public void setUserData(String name,
                            String password){
        userData.setName(name);
        userData.setPassword(password);
    }

    public void exit(){
        //todo
    }

    public void delete(){
        //todo
    }
}
