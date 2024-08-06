package ui.messageSrc;

import lombok.Getter;
import ui.messageSrc.commands.AdminCommands;
import ui.messageSrc.commands.ClientCommands;
import ui.messageSrc.commands.ManagerCommands;

/**
 * Класс, перечисляющий команды, используется в начале программы, для удобства
 */

@Getter
public enum StringCommands {

    ROLES(new String[]{
            "Администратор",
            "Менеджер",
            "Клиент"
    }),

    REG_OR_AUTORIZE(new String[]{
            
            "Войти",
            "Зарегистрироваться"}
    ),

    CLIENT_COMMANDS(ClientCommands.getStringArray()),

    MANAGER_COMMANDS(ManagerCommands.getStringArray()),

    ADMIN_COMMANDS(AdminCommands.getStringArray());


    private final String[] commands;

    StringCommands(String[] commands) {
        this.commands = commands;
    }

}
