package Model.Exceptions.CarExc;

import Controller.Controller;
import Model.LoggerUtil.Levels;

public class NoSuchCarException extends Exception{
    public NoSuchCarException() {
        Controller.logger.log(Levels.ERR, "Автомобиль не найден в базе данных!");
    }
}
