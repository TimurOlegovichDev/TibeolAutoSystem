package Model.Entities.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhoneNumber {
    private String phoneNumber;

    @Override
    public String toString(){
        return (phoneNumber == null) ? "Не указан" : phoneNumber;
    }
}
