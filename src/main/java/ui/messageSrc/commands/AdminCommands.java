package ui.messageSrc.commands;

import lombok.Getter;

@Getter
public enum AdminCommands implements Commandable{
    USER_LIST("Список пользователей"),
    GET_SORTED_LIST("Отсортировать список"),
    SHUT_DOWN("Завершить работу системы");

    private final String command;
    AdminCommands(String command) {
        this.command = command;
    }


    @Getter
    enum CommandsInUserList {

        SETUP_USER("Настроить пользователя"),
        DELETE_USER("Удалить пользователя"),
        BACK("Назад");

        private final String command;
        CommandsInUserList(String command) {
            this.command = command;
        }
    }
}
