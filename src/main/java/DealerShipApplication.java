import Controller.Controller;
import ui.messageSrc.Messages;
import ui.out.Printer;

/**
 * Главный класс для запуска программы. В нем мы создаем экземляр контроллера с помощью синглтона, чтобы предотвратить множество экземляров.
 */

public class DealerShipApplication {
    public static void main(String[] args) throws InterruptedException {
        Printer.print(Messages.START.getMessage());
        Controller.getController().start();
        Controller.getController().join();
        Printer.print(Messages.END.getMessage());
    }
}
