package Model.Entities.Users;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

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

    public static AccessLevels getAccessLevelByValue(@Nullable String value) {
        if(value == null) return null;
        for (AccessLevels level : AccessLevels.values()) {
            if (level.getValue().equals(value)) {
                return level;
            }
        }
        return null;
    }

}
