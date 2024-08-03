package ui.messageSrc.commands;


import lombok.Getter;

@Getter
public enum ManagerCommands {
    GO_TO_SHOWROOM("Перейти в автосалон"),
    VIEW_ARCHIVED_ORDERS("Активные заказы"),
    VIEW_ACTIVE_ORDERS("Заказы в архиве"),
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
    public enum CommandsInActiveOrderList {

        SET_STATUS("Изменить статус заказа"),
        DISMISS("Отклонить заказ"),
        SEARCH_ORDERS("Поиск заказов"),
        BACK("Назад");

        private final String command;
        CommandsInActiveOrderList(String command) {
            this.command = command;
        }

        public static String[] getStringArray(){
            String[] strings = new String[CommandsInActiveOrderList.values().length];
            int index = 0;
            for(CommandsInActiveOrderList command : CommandsInActiveOrderList.values())
                strings[index++] = command.getCommand();
            return strings;
        }
    }
}
