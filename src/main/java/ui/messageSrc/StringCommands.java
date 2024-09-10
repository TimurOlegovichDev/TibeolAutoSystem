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
            "Administrator",
            "Manager",
            "Client"
    }),

    REG_OR_AUTHORIZE(new String[]{
            "Register",
            "Log in",
    }
    ),

    CLIENT_COMMANDS(ClientCommands.getStringArray()),

    MANAGER_COMMANDS(ManagerCommands.getStringArray()),

    ADMIN_COMMANDS(AdminCommands.getStringArray());

    private final String[] commands;

    StringCommands(String[] commands) {
        this.commands = commands;
    }

}
