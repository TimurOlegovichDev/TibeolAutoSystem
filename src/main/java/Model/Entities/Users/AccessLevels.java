package Model.Entities.Users;

import lombok.Getter;

/**
 * Перечисление уровней доступа, он определяет, может ли пользователь выполнить ту или иную функцию
 */

@Getter
public enum AccessLevels {

    ADMINISTRATOR("Администратор"),
    MANAGER("Менеджер"),
    CLIENT("Клиент");

    private final String value;

    AccessLevels(String value) {
        this.value = value;
    }

}
