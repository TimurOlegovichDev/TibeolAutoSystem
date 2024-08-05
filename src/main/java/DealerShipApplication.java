import Controller.Controller;
import ui.messageSrc.Messages;
import ui.out.Printer;

/**
 * √лавный класс дл€ запуска программы. ¬ нем мы создаем экземл€р контроллера с помощью синглтона, чтобы предотвратить множество экземл€ров.
 */

public class DealerShipApplication {
    public static void main(String[] args) throws InterruptedException {
        Printer.print(Messages.START.getMessage());
        Controller.getController().start();
        Controller.getController().join();
        Printer.print(Messages.END.getMessage());
    }
}
