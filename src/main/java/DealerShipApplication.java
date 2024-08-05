import Controller.Controller;
import ui.messageSrc.Messages;
import ui.out.Printer;

/**
 * ������� ����� ��� ������� ���������. � ��� �� ������� �������� ����������� � ������� ���������, ����� ������������� ��������� ����������.
 */

public class DealerShipApplication {
    public static void main(String[] args) throws InterruptedException {
        Printer.print(Messages.START.getMessage());
        Controller.getController().start();
        Controller.getController().join();
        Printer.print(Messages.END.getMessage());
    }
}
