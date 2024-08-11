package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class NotEnoughRightsException extends Exception{
    public NotEnoughRightsException() {
        Controller.logger.log(Levels.WARN, LogActions.WARN.getText() + " Пользователю недостаточно прав для использования данных функций!");
    }
}
