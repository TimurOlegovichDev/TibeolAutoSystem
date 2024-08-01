package Model.Entities.Order;



public enum StatusesOfOrder {
    NEW,
    WAITING,
    EXECUTING,
    COMPLETED,
    ARCHIVED;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
