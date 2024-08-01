package ui.StringSRC;

import lombok.Getter;

@Getter
public enum Commands {

    ROLES(new String[]{"Администратор", "Менеджер", "Клиент"}),
    REG_OR_AUTORIZE(new String[]{"Войти", "Зарегистрироваться"});


    private final String[] commands;

    Commands(String[] commands) {
        this.commands = commands;
    }

}
