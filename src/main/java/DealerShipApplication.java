import Controller.Controller;
import ui.Messages.StringMessages;

public class DealerShipApplication {
    public static void main(String[] args) {
        System.out.println(StringMessages.START);
        Controller.getController().run();
    }
}
