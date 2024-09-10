package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException() {
        Controller.logger.log(Levels.ERR, LogActions.ERROR.getText() + " Пользотель с таким именем уже существует");
    }
}
