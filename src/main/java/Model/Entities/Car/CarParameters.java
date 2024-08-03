package Model.Entities.Car;

import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.User;

public enum CarParameters {
    OWNER,
    BRAND,
    MODEL,
    COLOR,
    YEAR,
    MILEAGE,
    PRICE;

    public static CarParameters[] getParameters(User user){
        if(user.getAccessLevel().equals(AccessLevels.CLIENT))
            return new CarParameters[]{OWNER,BRAND,MODEL,COLOR};
        else
            return CarParameters.values();
    }
}
