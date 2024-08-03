package Controller;

import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.Client;
import Model.Entities.Users.User;
import Model.Exceptions.InvalidPasswordException;
import Model.Exceptions.NoSuchUserException;
import Model.Exceptions.NotEnoughRightsException;
import Model.Exceptions.RegistrationInterruptException;
import Model.UserManagement.AuthenticationManager;
import Model.UserManagement.Encryptor;
import Model.UserManagement.RegistrationManager;

import lombok.Getter;
import lombok.Setter;
import ui.Menu;
import ui.messageSrc.commands.*;
import ui.out.Printer;
import ui.messageSrc.Messages;

public class Controller extends Thread {

    private static volatile Controller instance;

    public static synchronized Controller getController() {
        return (instance == null) ? (instance = new Controller()) : instance;
    }

    private Controller() {
    }

    private final ActionController actionController = new ActionController();

    volatile User currentUser;

    public void run() {

        for(Scenes scene = Scenes.GREETING; checkSceneCycle(scene);){

            switch (scene) {

                case GREETING -> scene = greeting();

                case CHOOSING_ROLE -> scene = chooseRegistrationOrAuth();

                case REGISTRATION -> scene = registrationHandler();

                case AUTHORIZATION -> scene = authorizationHandler();

                case ACTIONS -> scene = actionController.distributeActions();

                case EXIT_FROM_ACCOUNT -> scene = logOut();

                case SHUT_DOWN -> {}
            }
        }
    }

    private boolean checkSceneCycle(Scenes scene){
        return !(scene.equals(Scenes.SHUT_DOWN) && Menu.areYouSure(Messages.SHUT_DOWN_WARNING.getMessage()));
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
            Printer.print("Вы успешно зарегистрировались под ID " + currentUser.getUserParameters().getID() + " и именем " + currentUser.getUserParameters().getName());
            return Scenes.ACTIONS;
        } catch (RegistrationInterruptException e) {
            Printer.print(Messages.ERROR.getMessage());
            return Scenes.CHOOSING_ROLE;
        }
    }

    private Scenes authorizationHandler(){
        try {
            authorization();
            Printer.print("Вы вошли в аккаунт под ID " + currentUser.getUserParameters().getID() + " и именем " + currentUser.getUserParameters().getName());
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

    private Scenes logOut(){
        currentUser = null;
        return Scenes.CHOOSING_ROLE;
    }

    private void timeDelay(int ms){
        try{
            Thread.sleep(ms);
        } catch (InterruptedException ignored){
            System.err.println("Задержка прервана");
        }
    }


    private class ActionController {

        private Scenes distributeActions(){
            Scenes nextScene = Scenes.ACTIONS;
            try {
                switch (currentUser.getAccessLevel()) {
                    case CLIENT -> nextScene = clientActionHandler(Menu.clientChoosingAction());
                    case MANAGER -> nextScene = managerActionHandler(Menu.managerChoosingAction());
                    case ADMINISTRATOR -> nextScene = adminActionHandler(Menu.adminChoosingAction());
                }
            } catch (NotEnoughRightsException rightsException){
                Printer.print("Неверное действие, проверьте корректность введенных данных и попробуйте снова");
                distributeActions();
            }
            return nextScene;
        }

        private Scenes clientActionHandler(ClientCommands action) throws NotEnoughRightsException {
            if(!currentUser.getAccessLevel().equals(AccessLevels.CLIENT)) throw new NotEnoughRightsException();
            Scenes nextScene = Scenes.ACTIONS;
            switch (action) {
                case VIEW_ORDERS -> ActionHandler.viewUserOrders((Client) currentUser);
                case VIEW_USER_CARS -> ActionHandler.viewUserCars((Client) currentUser);
                case ADD_USER_CAR -> {}
                case GO_TO_SHOWROOM -> ActionHandler.goToShowRoom(currentUser);
                case SETUP_MY_PROFILE -> {}
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
                default -> throw new NotEnoughRightsException();
            }
            return nextScene;
        }

        private Scenes managerActionHandler(ManagerCommands action) throws NotEnoughRightsException {
            Scenes nextScene = Scenes.ACTIONS;
            switch (action) {
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
            }
            return nextScene;
        }

        private Scenes adminActionHandler(AdminCommands action) throws NotEnoughRightsException {
            Scenes nextScene = Scenes.ACTIONS;
            switch (action) {
                case USER_LIST ->  ActionHandler.viewUsers(currentUser);
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
                case SHUT_DOWN -> nextScene = Scenes.SHUT_DOWN;
            }
            return nextScene;
        }
    }

}
