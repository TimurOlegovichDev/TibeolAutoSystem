package Model.DataBase.DataFields;

import lombok.Getter;

/**
 * Поля таблицы автомобилей клиентов, данное перечисление помогает при создании запросов к БД >
 * {@link #index} - index индекс колонны |
 * {@link #value} - хранит название колонны
 */

@Getter
public enum ClientCarDataFields implements DataFieldImp {

    ID("id",0),
    CLIENT_ID("client_id",2),
    BRAND("brand",3),
    MODEL("model",4),
    COLOR("color",5);

    private final String value;

    private final int index;

    ClientCarDataFields(String value, int index) {
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return value;
    }
}
