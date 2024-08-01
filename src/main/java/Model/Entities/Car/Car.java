package Model.Entities.Car;

import Model.DataBase.DealerCarData;
import Model.Entities.Users.Id;
import Model.Entities.Users.User;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;


@Getter
public class Car {
    @Nullable
    private User owner;
    @Nullable
    private String registrationNumber;

    private final int ID = Id.getUniqueId(DealerCarData.getCarData());

    @Setter
    @Getter
    private String model;
    @Setter
    @Getter
    private String brand;

    @Setter
    private Instant yearOfProduction;

}
