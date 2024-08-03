package Model.Entities.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
public abstract class User {

    private final UserParameters userParameters;
    @Setter
    private PhoneNumber phoneNumber;
    @Setter
    private AccessLevels accessLevel;
    User(String name, byte[] password){
        this.userParameters = new UserParameters(name, password);
    }

    public void setUserParameters(String name,
                                  byte[] password){

        userParameters.setName(name);
        userParameters.setPassword(password);

    }

    public void exit(){
        //todo
    }

    public void delete(){
        //todo
    }

    @Override
    public String toString() {
        return "| ID: " + userParameters.getID() +
                " | Имя: " + userParameters.getName() +
                " | Контактный номер: " + getPhoneNumber() +
                " | Роль: " + getAccessLevel().toString() + " | ";
    }
}
