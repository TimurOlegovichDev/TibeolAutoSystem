package Model.DataBase;

import Model.Entities.Car.Car;
import java.util.HashMap;
import java.util.Map;

public abstract class DealerCarData{

    private static final Map<Integer, Car> carData = new HashMap<>();

    static Map<Integer, Car> getCarData(){
        return new HashMap<>(carData);
    }

    protected static void add(Car car){
        carData.put(car.getID(), car);
    }

    protected static void remove(Car car){
        carData.remove(car.getID(), car);
    }
}
