package Controller;

import ui.Messages.StringMessages;

public class Controller {

    private static volatile Controller instance;

    public static synchronized Controller getController(){
        return (instance == null) ? (instance=new Controller()) : instance;
    }
    private Controller(){}

    public void run(){
        System.out.println(StringMessages.GREETING);
    }

}
