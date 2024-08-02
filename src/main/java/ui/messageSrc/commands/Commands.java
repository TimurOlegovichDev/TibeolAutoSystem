package ui.messageSrc.commands;

import lombok.Getter;

@Getter
public enum Commands implements Commandable{

    RETURN("Вернуться"),
    EXIT_FROM_ACCOUNT("Выйти из аккаунта");

    private final String command;
    Commands(String command) {
        this.command = command;
    }
}
