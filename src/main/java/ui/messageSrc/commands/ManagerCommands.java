package ui.messageSrc.commands;


import lombok.Getter;


/**
 * Набор всех команд пользователя данной роли, вложенный класс используется для разделения между страницами выбора действий
 */

@Getter
public enum ManagerCommands {
    GO_TO_SHOWROOM("Перейти в автосалон"),
    ORDERS("К заказам"),
    SETUP_MY_PROFILE("Изменить личные данные"),
    EXIT_FROM_ACCOUNT("Выйти из аккаунта"),
    DELETE_ACCOUNT("Удалить аккаунт");

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

        VIEW_ALL_CARS("Автомобили в продаже"),
        ADD_CAR("Добавить автомобиль"),
        REMOVE_CAR("Удалить автомобиль"),
        SEARCH_CAR("Поиск автомобиля"),
        SETUP_CAR("Настроить автомобиль"),
        BACK("Назад");

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

        VIEW_ARCHIVED_ORDERS("Активные заказы"),
        VIEW_ACTIVE_ORDERS("Заказы в архиве"),
        SET_STATUS("Изменить статус заказа"),
        BACK("Назад");

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
