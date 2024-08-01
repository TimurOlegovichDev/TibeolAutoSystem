package Model.Entities.Users;

/**
 * Перечисление уровней доступа, он определяет, может ли пользователь выполнить ту или иную функцию
 */

public enum AccessLevels {

    ADMINISTRATOR(3),
    MANAGER(2),
    CLIENT(1);

    private final int level;

    AccessLevels(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
