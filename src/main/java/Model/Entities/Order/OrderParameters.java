package Model.Entities.Order;

import lombok.Getter;

/**
 * Основные поля заказа, данные енум используется для удобства вывода возможных действий пользователю и валидации введенных данных
 */

@Getter
public enum OrderParameters {

    OWNER_NAME("Имя"),
    STATUS("Статус заказа"),
    DATE("Дата"),
    AUTO("Название марки автомобиля");


    private final String value;
    OrderParameters(String value) {
        this.value = value;
    }
}
