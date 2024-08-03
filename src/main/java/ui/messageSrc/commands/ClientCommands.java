package ui.messageSrc.commands;

import lombok.Getter;

@Getter
public enum ClientCommands {

    VIEW_ORDERS("Просмотр моих заказов"),
    VIEW_USER_CARS("Мои автомобили"),
    ADD_USER_CAR("Добавить автомобиль"),
    REMOVE_USER_CAR("Удалить автомобиль"),
    GO_TO_SHOWROOM("Автосалон"),
    SETUP_MY_PROFILE("Изменить личные данные"),
    EXIT_FROM_ACCOUNT("Выйти из аккаунта"),
    DELETE_ACCOUNT("Удалить аккаунт");
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

        VIEW_ALL_CARS("Автомобили в продаже"),
        CREATE_PURCHASE_ORDER("Купить авто"),
        CREATE_SERVICE_ORDER("Обслужить авто"),
        SEARCH_CAR("Поиск автомобиля"),
        BACK("Назад");

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
