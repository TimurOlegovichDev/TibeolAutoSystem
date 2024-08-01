package Controller;

import Model.DataBase.UserData;
import Model.Entities.Users.User;
import Model.Exceptions.InvalidPasswordException;
import Model.Exceptions.NoSuchUserException;
import Model.Exceptions.RegistrationException;
import Model.UserManagement.AuthenticationManager;
import Model.UserManagement.Encryptor;
import Model.UserManagement.RegistrationManager;
import ui.In.Menu;
import ui.Out.Printer;
import ui.StringSRC.Messages;

public class Controller {

    private static volatile Controller instance;

    private volatile User currentUser;

    public static synchronized Controller getController() {
        return (instance == null) ? (instance = new Controller()) : instance;
    }

    private Controller() {
    }

    public void run() {
        while (true){
            Menu.greeting();
            enterToSystem();
            UserData.print();
            Printer.print("Вы вошли в аккаунт под ID " + currentUser.getUserParameters().getId() + " и именем " + currentUser.getUserParameters().getName());
        }
    }

    private void enterToSystem(){
        while(true){
            try {
                Printer.print(Messages.ACTIONS_TO_ENTER.getMessage());
                actionToEnter();
                break;
            } catch (Exception ignored){}
        }
    }

    private void actionToEnter() throws Exception {
        if (getActionToSign().equals("Войти")) {
            Printer.print("Добро пожаловать, чтобы войти в систему,");
            authorization();
        } else {
            Printer.print("Добро пожаловать, для успешной регистрации понадобится некоторые данные, для начала, ");
            registration();
        }
    }

    private void registration() {
        while (true) {
            try {
                currentUser = RegistrationManager.registration(
                        Menu.chooseRole(),
                        Menu.getUserName(),
                        Menu.getUserPassword()
                );
                return;
            } catch (RegistrationException e) {
                Printer.print(Messages.ERROR.getMessage());
            }
        }
    }

    private void authorization() throws Exception {
        try {
            currentUser = AuthenticationManager.authentication(
                    Menu.getUserName(),
                    Encryptor.encrypt(Menu.getUserPassword())
            );
            return;
        } catch (NoSuchUserException NSUE) {
            Printer.print(Messages.NO_SUCH_USER.getMessage());
        } catch (InvalidPasswordException IPE) {
            Printer.print(Messages.INVALID_PASS.getMessage());
        }
        throw new Exception();
    }

    private String getActionToSign() {
        return Menu.chooseActionToStart();
    }

    private void startChoosingRole() {
        Menu.chooseRole();
    }

}
