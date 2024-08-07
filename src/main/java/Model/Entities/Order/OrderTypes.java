package Model.Entities.Order;

public enum OrderTypes {

    PURCHASE("Покупку"),
    SERVICE("Обслуживание");

    private final String title;

    OrderTypes(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
