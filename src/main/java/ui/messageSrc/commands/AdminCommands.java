package ui.messageSrc.commands;

import lombok.Getter;

@Getter
public enum AdminCommands {
    USER_LIST("Список пользователей"),
    GET_SORTED_LIST("Отсортировать список"),
    SETUP_MY_PROFILE("Изменить личные данные"),
    EXIT_FROM_ACCOUNT("Выйти из аккаунта"),
    SHUT_DOWN("Завершить работу системы");

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

        SETUP_USER("Настроить пользователя"),
        DELETE_USER("Удалить пользователя"),
        BACK("Назад");

        private final String command;
        CommandsInUserList(String command) {
            this.command = command;
        }
        public static String[] getStringArray(){
            String[] strings = new String[ManagerCommands.values().length];
            int index = 0;
            for(ManagerCommands command : ManagerCommands.values())
                strings[index++] = command.getCommand();
            return strings;
        }
    }
}
