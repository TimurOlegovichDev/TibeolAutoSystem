package ui.messageSrc;


import lombok.Getter;

/**
 * Перечисление крупных сообщений, сделано для удобства и лаконичности кода
 */
@Getter
public enum Messages {

    GREETING("\"Добро пожаловать в систему Автосалона \"TibeolAuto\", " +
            "для дальнейшего пользования программой, вводите начало названия выбранного действия.\n"),

    ACTIONS_TO_ENTER("""
            Что желаете выполнить?:
            - Зарегистрироваться
            - Войти\n"""),

    CHOOSE_U_ACTION("Выберите дальнейшее действие: "),

    ENTER_NAME("введите имя пользователя (от 8 до 30 символов):\n"),

    ERROR("Извините, произошла непредвиденная ошибка, возврат к предыдущему действию\n"),

    EXIT("Выполняется выход из системы\n"),

    ENTER_PASSWORD("Введите пароль (от 8 до 50 символов):\n"),

    ENTER_PHONE_NUMBER("Укажите номер телефона, начиная с \"8\", если не хотите, то можете выбрать команду \"Пропустить\":\n"),

    CHOOSE_ROLE("""
            Укажите ваш статус:
            - Администратор
            - Менеджер
            - Клиент\n"""),
    SUCCESSFUL_AUTHORIZATION(""),

    INVALID_COMMAND("Ошибочный ввод, проверьте корректность данных и повторите попытку\n"),

    INVALID_PASS("Неверный пароль, попробуйте войти снова\n"),

    NO_SUCH_USER("Данный пользователь не найден в системе, проверьте вводные данные или зарегистрируйтесь под новым именем\n"),

    START("Система запущена\n"),

    END("Выключение\n");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
