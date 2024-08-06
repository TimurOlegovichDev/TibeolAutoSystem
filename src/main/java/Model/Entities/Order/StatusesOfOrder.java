package Model.Entities.Order;


import lombok.Getter;

/**
 * Все возможные статусы заявлений, ВАЖНО, заказы о покупке могут быть либо ОДОБРЕНЫ, либо ОТКЛОНЕНЫ, заказы об обслуживании могут либо ВЫПОЛНЯТЬСЯ, либо ЗАВЕРШЕНО
 */
@Getter
public enum StatusesOfOrder {
    NEW("Не просмотрено"),
    CHECKED("Просмотрено"),
    EXECUTING("Выполняется"),
    COMPLETED("Завершено"),
    DISMISSED("Отклонено"),
    AGREED("Одобрено"),
    ARCHIVED("Архивировано");


    private final String command;

    StatusesOfOrder(String command) {
        this.command = command;
    }

    public static String[] getStringArray(OrderTypes orderTypes){
        if(orderTypes.equals(OrderTypes.PURCHASE))
            return new String[]{AGREED.getCommand(), DISMISSED.getCommand()};
        return new String[]{EXECUTING.getCommand(), COMPLETED.getCommand()};
    }

    @Override
    public String toString() {
        return command;
    }
}
