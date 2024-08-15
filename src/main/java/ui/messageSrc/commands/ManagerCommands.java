package ui.messageSrc.commands;


import lombok.Getter;


/**
 * Набор всех команд пользователя данной роли, вложенный класс используется для разделения между страницами выбора действий
 */

@Getter
public enum ManagerCommands {

    GO_TO_SHOWROOM("Dealer page"),
    ORDERS("Orders page"),
    SETUP_MY_PROFILE("Setup my profile"),
    EXIT_FROM_ACCOUNT("Exit from account"),
    DELETE_ACCOUNT("Delete account"),;

    private final String command;

    ManagerCommands(String command) {
        this.command = command;
    }

    public static String[] getStringArray(){
        String[] strings = new String[ManagerCommands.values().length];
        int index = 0;
        for(ManagerCommands command : ManagerCommands.values())
            strings[index++] = command.getCommand();
        return strings;
    }

    @Getter
    public enum CommandsInShowRoom {

        VIEW_ALL_CARS("Cars for Sale"),
        ADD_CAR("Add Car"),
        REMOVE_CAR("Remove Car"),
        SEARCH_CAR("Search Car"),
        SETUP_CAR("Configure Car"),
        BACK("Back");

        private final String command;

        CommandsInShowRoom(String command) {
            this.command = command;
        }

        public static String[] getStringArray(){
            String[] strings = new String[ManagerCommands.CommandsInShowRoom.values().length];
            int index = 0;
            for(CommandsInShowRoom command : CommandsInShowRoom.values())
                strings[index++] = command.getCommand();
            return strings;
        }

    }

    @Getter
    public enum CommandsInOrderList {

        VIEW_ARCHIVED_ORDERS("Archived Orders"),
        VIEW_ACTIVE_ORDERS("Active Orders"),
        SET_STATUS("Change Order Status"),
        BACK("Back");

        private final String command;

        CommandsInOrderList(String command) {
            this.command = command;
        }

        public static String[] getStringArray(){
            String[] strings = new String[CommandsInOrderList.values().length];
            int index = 0;
            for(CommandsInOrderList command : CommandsInOrderList.values())
                strings[index++] = command.getCommand();
            return strings;
        }
    }
}
