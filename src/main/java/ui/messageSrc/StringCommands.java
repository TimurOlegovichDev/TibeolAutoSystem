package ui.messageSrc;

import lombok.Getter;

@Getter
public enum StringCommands {

    ROLES(new String[]{
            "Администратор",
            "Менеджер",
            "Клиент"
    }),
    REG_OR_AUTORIZE(new String[]{
            "Войти",
            "Зарегистрироваться"}
    ),
    CLIENT_COMMANDS(new String[]{
            "Мои заказы",
            "Мои автомобили",
            "Автосалон",
            "Обслуживание",
            "Добавить машину",
            "Выйти из аккаунта",}),
    MANAGER_COMMANDS(new String[]{
            "Активные заказы",
            "Архив заказов",
            "Автосалон",
            "",
            "",
            "",
            "",}),
    ADMIN_COMMANDS(new String[]{
            "",
            "",
            "",
            "",
            "",
            "",
            "",}
    );


    private final String[] commands;

    StringCommands(String[] commands) {
        this.commands = commands;
    }

}
