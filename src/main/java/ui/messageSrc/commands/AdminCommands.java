package ui.messageSrc.commands;

import lombok.Getter;

/**
 * Набор всех команд пользователя данной роли, вложенный класс используется для разделения между страницами выбора действий
 */

@Getter
public enum AdminCommands {

    GO_TO_USER_LIST("Go to User List"),
    GET_LOG_LIST("Show Events"),
    SAVE_LOG_LIST("Save Events to File"),
    SETUP_MY_PROFILE("Edit Personal Data"),
    EXIT_FROM_ACCOUNT("Log Out of Account"),
    SHUT_DOWN("Shut Down System"),
    DELETE_ACCOUNT("Delete Account");

    private final String command;

    AdminCommands(String command) {
        this.command = command;
    }

    public static String[] getStringArray(){
        String[] strings = new String[AdminCommands.values().length];
        int index = 0;
        for(AdminCommands command : AdminCommands.values())
            strings[index++] = command.getCommand();
        return strings;
    }

    @Getter
    public enum CommandsInUserList {

        USER_LIST("User List"),
        GET_FILTER_LIST("Filter List"),
        DELETE_USER("Delete User"),
        SET_USER_PARAM("Edit User Parameters"),
        BACK("Back");

        private final String command;
        CommandsInUserList(String command) {
            this.command = command;
        }
        public static String[] getStringArray(){
            String[] strings = new String[CommandsInUserList.values().length];
            int index = 0;
            for(CommandsInUserList command : CommandsInUserList.values())
                strings[index++] = command.getCommand();
            return strings;
        }
    }
}
