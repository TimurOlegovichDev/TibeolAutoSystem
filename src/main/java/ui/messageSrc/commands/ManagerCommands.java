package ui.messageSrc.commands;


import lombok.Getter;

@Getter
public enum ManagerCommands implements Commandable {
    VIEW_ACTIVE_ORDERS("Просмотр активных заказов"),
    VIEW_ARCHIVED_ORDERS("Мои автомобили"),
    GO_TO_SHOWROOM("Услуги автосалона");

    private final String command;
    ManagerCommands(String command) {
        this.command = command;
    }

    @Getter
    enum CommandsInShowRoom {

        VIEW_ALL_CARS("Автомобили в продаже"),
        CREATE_PURCHASE_ORDER("Купить авто"),
        CREATE_SERVICE_ORDER("Обслужить авто"),
        SEARCH_CAR("Поиск автомобиля"),
        BACK("Назад");

        private final String command;
        CommandsInShowRoom(String command) {
            this.command = command;
        }
    }


    @Getter
    enum CommandsInActiveOrderList {

        SET_STATUS("Изменить статус заказа"),
        DISMISS("Отклонить заказ"),
        SEARCH_ORDERS("Поиск заказов"),
        BACK("Назад");

        private final String command;
        CommandsInActiveOrderList(String command) {
            this.command = command;
        }
    }
}
