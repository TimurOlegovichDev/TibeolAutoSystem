package Model.DataBase;

import Model.Entities.Car.Car;

import java.util.HashMap;
import java.util.Map;

public abstract class DealerCarData{

    private static Map<Integer, Car> carData = new HashMap<>();

    protected static Map<Integer, Car> getCarData(){
        return new HashMap<>(carData);
    }

    protected static void add(Car car){
        carData.put(car.getID(), car);
    }
}
