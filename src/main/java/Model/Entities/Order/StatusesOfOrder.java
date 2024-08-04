package Model.Entities.Order;


import lombok.Getter;
import ui.messageSrc.commands.ClientCommands;

import java.util.Arrays;
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
