package ui.messageSrc.commands;

import lombok.Getter;

/**
 * Набор всех команд пользователя данной роли, вложенный класс используется для разделения между страницами выбора действий
 */

@Getter
public enum ClientCommands {

    VIEW_ORDERS("View My Orders"),
    VIEW_USER_CARS("My Cars"),
    ADD_USER_CAR("Add Car"),
    REMOVE_USER_CAR("Remove Car"),
    GO_TO_SHOWROOM("Showroom"),
    SETUP_MY_PROFILE("Edit Profile"),
    EXIT_FROM_ACCOUNT("Log Out"),
    DELETE_ACCOUNT("Delete Account");

    private final String command;

    ClientCommands(String command) {
        this.command = command;
    }

    public static String[] getStringArray(){
        String[] strings = new String[ClientCommands.values().length];
        int index = 0;
        for(ClientCommands command : ClientCommands.values())
            strings[index++] = command.getCommand();
        return strings;
    }

    @Getter
    public enum CommandsInShowRoom {

        VIEW_ALL_CARS("Cars for Sale"),
        CREATE_PURCHASE_ORDER("Buy a Car"),
        CREATE_SERVICE_ORDER("Service a Car"),
        SEARCH_CAR("Search for a Car"),
        BACK("Back");

        private final String command;

        CommandsInShowRoom(String command) {
            this.command = command;
        }

        public static String[] getStringArray(){
            String[] strings = new String[ClientCommands.CommandsInShowRoom.values().length];
            int index = 0;
            for(CommandsInShowRoom command : CommandsInShowRoom.values())
                strings[index++] = command.getCommand();
            return strings;
        }

    }
}
