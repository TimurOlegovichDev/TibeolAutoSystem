package Model.Entities.Order;

public enum OrderTypes {
    PURCHASE("Покупка автомобиля"),
    SERVICE("Обслуживание автомобиля");

    private final String title;

    OrderTypes(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
