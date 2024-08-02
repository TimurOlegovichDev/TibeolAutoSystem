package Controller;

import Model.DataBase.UserData;
import Model.Entities.Users.User;
import Model.Exceptions.InvalidPasswordException;
import Model.Exceptions.NoSuchUserException;
import Model.Exceptions.RegistrationInterruptException;
import Model.UserManagement.AuthenticationManager;
import Model.UserManagement.Encryptor;
import Model.UserManagement.RegistrationManager;
import ui.in.Menu;
import ui.out.Printer;
import ui.messageSrc.Messages;

public class Controller extends Thread {

    private static volatile Controller instance;

    private volatile User currentUser;

    public static synchronized Controller getController() {
        return (instance == null) ? (instance = new Controller()) : instance;
    }

    private Controller() {
    }

    public void run() {

        for(Scenes scene = Scenes.GREETING; scene.getNumber() < Scenes.SHUT_DOWN.getNumber();){
            timeDelay(500);
            switch (scene){

                case GREETING -> scene = greeting();

                case CHOOSING_ROLE -> scene = chooseRegistrationOrAuth();

                case REGISTRATION -> scene = registrationHandler();

                case AUTHORIZATION -> scene = authorizationHandler();

                case ACTIONS -> {
                    System.out.println("Здесь потом будет набор функций");
                    UserData.print();
                    Printer.print("Вы вошли в аккаунт под ID " + currentUser.getUserParameters().getID() + " и именем " + currentUser.getUserParameters().getName());
                }
                case EXIT_FROM_ACCOUNT -> {
                    continue;
                }
                case SHUT_DOWN -> {
                    continue;
                }
            }
            System.out.println(scene + " NUMBER OF SCENE");
        }
    }

    private Scenes greeting(){
        Menu.greeting();
        return Scenes.GREETING.nextStep();
    }

    private Scenes chooseRegistrationOrAuth(){
        Printer.print(Messages.ACTIONS_TO_ENTER.getMessage());
        return (Menu.chooseRegistrationOrAuth().equals("Войти")) ? Scenes.AUTHORIZATION : Scenes.REGISTRATION;
    }

    private Scenes registrationHandler(){
        try {
            registration();
            return Scenes.ACTIONS;
        } catch (RegistrationInterruptException e) {
            Printer.print(Messages.ERROR.getMessage());
            return Scenes.CHOOSING_ROLE;
        }
    }

    private Scenes authorizationHandler(){
        try {
            authorization();
            return Scenes.ACTIONS;
        } catch (InvalidPasswordException e){
            Printer.print(Messages.INVALID_PASS.getMessage());
        } catch (NoSuchUserException e) {
            Printer.print(Messages.NO_SUCH_USER.getMessage());
        } catch (Exception e) {
            Printer.print(Messages.ERROR.getMessage());
        }
        return Scenes.CHOOSING_ROLE;
    }


    private void registration() throws RegistrationInterruptException {
        Printer.print("Добро пожаловать, для успешной регистрации понадобится некоторые данные, для начала, ");
        currentUser = RegistrationManager.registration(
                Menu.chooseRole(),
                Menu.getUserName(),
                Menu.getUserPassword()
        );
    }

    private void authorization() throws Exception, InvalidPasswordException {
        Printer.print("Добро пожаловать, чтобы войти в систему,");
        currentUser = AuthenticationManager.authentication(
                Menu.getUserName(),
                Encryptor.encrypt(Menu.getUserPassword())
        );
    }

    private void timeDelay(int ms){
        try{
            Thread.sleep(ms);
        } catch (InterruptedException ignored){
            System.err.println("Задержка прервана");
        }
    }

}
