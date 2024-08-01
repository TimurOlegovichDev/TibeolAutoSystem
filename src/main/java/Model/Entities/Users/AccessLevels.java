package Model.Entities.Users;

import lombok.Getter;

/**
 * Перечисление уровней доступа, он определяет, может ли пользователь выполнить ту или иную функцию
 */

@Getter
public enum AccessLevels {

    ADMINISTRATOR(3),
    MANAGER(2),
    CLIENT(1);

    private final int level;

    AccessLevels(int level) {
        this.level = level;
    }

}
