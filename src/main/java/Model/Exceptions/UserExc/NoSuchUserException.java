package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class NoSuchUserException extends Exception{
    public NoSuchUserException() {
        Controller.logger.log(Levels.ERR, LogActions.ERROR.getText() + " Пользователь не найден в системе!");
    }
}
