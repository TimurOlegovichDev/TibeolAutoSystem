package Model.Exceptions.UserExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;

public class DeliberateInterruptException extends Exception{
    public DeliberateInterruptException() {
        Controller.logger.log(Levels.INFO,  "Пользователь прервал операцию");
    }
}
