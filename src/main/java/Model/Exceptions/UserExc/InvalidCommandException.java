package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class InvalidCommandException extends Exception{
    public InvalidCommandException() {
        Controller.logger.log(Levels.WARN, LogActions.WARN.getText() + " Ошибочный выбор действия пользователя!");
    }
}
