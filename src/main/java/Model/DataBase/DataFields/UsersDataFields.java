package Model.DataBase.DataFields;

import lombok.Getter;

/**
 * Поля таблицы пользователей, данное перечисление помогает при создании запросов к БД >
 * {@link #index} - index индекс колонны |
 * {@link #value} - хранит название колонны
 */

@Getter
public enum UsersDataFields implements DataFieldImp {
    ID("id",0),
    NAME("name",1),
    PASSWORD("password",2),
    PHONE_NUMBER("phone_number",3),
    ROLE("role",4);

    private final String value;

    private final int index;

    UsersDataFields(String value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return value;
    }
}
