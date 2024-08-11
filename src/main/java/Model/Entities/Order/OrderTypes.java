package Model.Entities.Order;

import Model.Entities.Car.CarParameters;

public enum OrderTypes {

    PURCHASE("Покупку"),
    SERVICE("Обслуживание");

    private final String title;

    OrderTypes(String title) {
        this.title = title;
    }

    public static OrderTypes getTypeFromString(String s) {
        if(s == null) return null;
        for (OrderTypes parameter : OrderTypes.values()) {
            if (parameter.title.equalsIgnoreCase(s)) {
                return parameter;
            }
        }
        return null;

    }

    @Override
    public String toString() {
        return title;
    }
}
