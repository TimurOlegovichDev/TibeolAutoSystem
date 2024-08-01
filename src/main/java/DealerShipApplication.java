import Controller.Controller;
import ui.StringSRC.Messages;

public class DealerShipApplication {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Messages.START);
        Thread.sleep(1000);
        Controller.getController().run();
    }
}
