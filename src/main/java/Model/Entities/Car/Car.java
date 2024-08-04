package Model.Entities.Car;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Users.Client;
import Model.Entities.Users.Id;
import Model.Entities.Users.User;
import Model.Exceptions.CarExc.InvalidContractException;
import Model.Exceptions.CarExc.NoSuchCarException;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


@Setter
@Getter
public class Car {

    @Nullable
    private User owner;

    @Getter
    private final int ID = Id.getUniqueId(DataBaseHandler.getCarData());

    private String model;
    private String brand;

    @Nullable
    private Integer yearOfProduction;

    private String color;

    @Nullable
    private Integer price;

    @Nullable
    private Integer mileAge;

    @Nullable
    private String description;

    private boolean booked = false;

    public Car(@NotNull Client client,
               @NotNull String model,
               @NotNull String brand,
               @NotNull String color)
    {
        this.owner = client;
        this.model = model;
        this.brand = brand;
        this.color = color;
    }

    public Car(
               @NotNull String brand,
               @NotNull String model,
               @NotNull String color,
               @NotNull Integer yearOfProduction,
               @NotNull Integer mileAge,
               @NotNull String description,
               @NotNull Integer price)
    {
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.mileAge = mileAge;
        this.description = description;
    }

    private Car(){}

    @Override
    public String toString() {
        return "| ID: " + ID +
                " | Производитель: " + brand +
                " | Модель: " + model + " | ";
    }

    public String getForm() {
        return "| ID: " + ID +
                " | Производитель: " + brand +
                " | Модель: " + model +
                " | Цвет: " + brand +
                " | Пробег: " + brand +
                " | Модель: " + model +
                "\n Описание: " + description;
    }

    public void purchase(Client newOwner) throws NoSuchCarException, InvalidContractException {
        if(!DataBaseHandler.getCarData().containsKey(getID())) throw new NoSuchCarException();
        if(Objects.equals(owner, newOwner)) throw new InvalidContractException();
        owner = newOwner;
        DataBaseHandler.remove(this);
    }
}
