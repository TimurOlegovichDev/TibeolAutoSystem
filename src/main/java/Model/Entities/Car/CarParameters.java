package Model.Entities.Car;

import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.User;

/**
 * Данный енум позволяет легко валидировать поступающие команды от пользователей
 * @see Car
 */


public enum CarParameters {
    OWNER("Владелец"),
    BRAND("Марка"),
    MODEL("Модель"),
    COLOR("Цвет"),
    YEAR("Год"),
    MILEAGE("Пробег"),
    PRICE("Цена");

    private final String translation;

    CarParameters(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public static String[] getStringArray(User user) {
        CarParameters[] parameters = getParameters(user);
        String[] translations = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            translations[i] = parameters[i].getTranslation();
        }
        return translations;
    }

    public static CarParameters[] getParameters(User user) {
        if (user.getAccessLevel().equals(AccessLevels.CLIENT))
            return new CarParameters[]{OWNER, BRAND, MODEL, COLOR};
        else
            return CarParameters.values();
    }

    public static CarParameters getCarParameterFromString(String parameterString) {
        for (CarParameters parameter : CarParameters.values()) {
            if (parameter.getTranslation().equals(parameterString)) {
                return parameter;
            }
        }
        return null;
    }
}
