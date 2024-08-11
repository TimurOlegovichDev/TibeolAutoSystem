package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class InvalidInputException extends Exception{
    public InvalidInputException() {
        Controller.logger.log(Levels.WARN, LogActions.WARN.getText() + " Ошибочный ввод пользователя!");
    }
}
