package Model.Entities.Car;

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

    private final UUID ID = UUID.randomUUID();

    @Setter
    @Getter
    private String model;
    @Setter
    @Getter
    private String brand;

    @Setter
    private Instant yearOfProduction;

}
