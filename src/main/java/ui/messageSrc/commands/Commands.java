package ui.messageSrc.commands;

import lombok.Getter;

@Getter
public enum Commands implements Commandable{

    SETUP_MY_PROFILE("Изменить личные данные"),
    EXIT_FROM_ACCOUNT("Выйти из аккаунта");

    private final String command;
    Commands(String command) {
        this.command = command;
    }
}
