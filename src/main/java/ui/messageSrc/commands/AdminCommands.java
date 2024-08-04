package ui.messageSrc.commands;

import lombok.Getter;


/**
 * Набор всех команд пользователя данной роли, вложенный класс используется для разделения между страницами выбора действий
 */

@Getter
public enum AdminCommands {


    GO_TO_USER_LIST("К списку пользователей"),
    GET_LOG_LIST("Показать события"),
    SAVE_LOG_LIST("Сохранить события в файл"),
    SETUP_MY_PROFILE("Изменить личные данные"),
    EXIT_FROM_ACCOUNT("Выйти из аккаунта"),
    SHUT_DOWN("Завершить работу системы"),
    DELETE_ACCOUNT("Удалить аккаунт");

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


        USER_LIST("Список пользователей"),
        GET_FILTER_LIST("Фильтровать список"),
        DELETE_USER("Удалить пользователя"),
        SET_USER_PARAM("Изменить параметры пользователя"),
        BACK("Назад");

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
