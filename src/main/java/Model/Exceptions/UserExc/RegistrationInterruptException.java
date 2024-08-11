package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class RegistrationInterruptException extends Exception{
    public RegistrationInterruptException() {
        Controller.logger.log(Levels.ERR, LogActions.ERROR.getText() + " Программная ошибка при регистрации");
    }
}
