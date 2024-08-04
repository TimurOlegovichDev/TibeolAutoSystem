package Model.LoggerUtil;

import lombok.Getter;

@Getter
public enum LogActions {

    USER_REGISTERED("Зарегистрировался новый пользователь: "),
    USER_AUTHORIZED("Данный пользователь вошел в систему: "),
    NEW_SERVICE_ORDER("Клиент создал заказ на: "),
    NEW_PURCHASE_ORDER("Клиент создал заказ на: " ),
    CLIENT_ADD_CAR("Клиент зарегистрировал автомобиль: "),
    NEW_CAR_IN_DEALER("Добавлен новый автомобиль в автосалон: "),
    CAR_DELETED("Автомобиль удален из базы данных: "),
    USER_EXIT("Пользователь вышел из системы: "),
    ORDER_STATUS_CHANGED("Изменен статус заказа: "),
    USER_SETUP_PROFILE("Пользователь изменил личные данные: "),
    USER_DELETE_ACCOUNT("Аккаунт пользователя удален: ");

    private final String text;

    LogActions(String text) {
        this.text = text;
    }
}
