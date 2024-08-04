package Controller;

import Model.Entities.Users.AccessLevels;
import Model.Entities.Users.Client;
import Model.Entities.Users.Manager;
import Model.Entities.Users.User;
import Model.Exceptions.UserExc.*;
import Model.UserManagement.AuthenticationManager;
import Model.UserManagement.Encryptor;
import Model.UserManagement.RegistrationManager;

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

    boolean timeDelayOn = true;

    public void run() {

        for(Scenes scene = Scenes.GREETING; checkSceneCycle(scene);){

            timeDelay(300);
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
            timeDelay(200);
            Printer.print("Вы вошли в аккаунт под ID " + currentUser.getUserParameters().getID() + " и именем " + currentUser.getUserParameters().getName() + " ваша роль: " + currentUser.getAccessLevel().getValue());
            return Scenes.ACTIONS;
        } catch (RegistrationInterruptException e) {
            Printer.print(Messages.ERROR.getMessage());
        } catch (UserAlreadyExistsException e) {
            Printer.print(Messages.USER_ALREADY_EXISTS.getMessage());
        }
        return Scenes.CHOOSING_ROLE;
    }

    private Scenes authorizationHandler(){
        try {
            authorization();
            timeDelay(200);
            Printer.print("Вы вошли в аккаунт под ID " + currentUser.getUserParameters().getID() + " и именем " + currentUser.getUserParameters().getName() + " ваша роль: " + currentUser.getAccessLevel().getValue());
            userNotification();
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

    private void userNotification(){
        if(currentUser instanceof Client && !((Client) currentUser).getMessages().isEmpty())
           Printer.notify(((Client) currentUser).getMessages().size());
    }

    private void registration() throws RegistrationInterruptException, UserAlreadyExistsException {
        currentUser = RegistrationManager.registration(
                Menu.chooseRole(),
                Menu.getUserName(),
                Menu.getUserPassword()
        );
        if(currentUser instanceof Client || currentUser instanceof Manager)
            currentUser.setPhoneNumber(Menu.getUserPhoneNumber());
    }

    private void authorization() throws Exception, InvalidPasswordException {
        Printer.printCentered("Добро пожаловать!");
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
        if(!timeDelayOn) return;
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
                case ADD_USER_CAR -> ActionHandler.addUserCar((Client) currentUser);
                case REMOVE_USER_CAR -> ActionHandler.removeUserCar((Client) currentUser);
                case GO_TO_SHOWROOM -> ActionHandler.goToShowRoom(currentUser);
                case VIEW_MESSAGES -> ActionHandler.readMessages((Client) currentUser);
                case SETUP_MY_PROFILE -> nextScene = ActionHandler.setUpUserParameters(currentUser);
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
                case DELETE_ACCOUNT -> nextScene = ActionHandler.removeAccount(currentUser);
                default -> throw new NotEnoughRightsException();
            }
            return nextScene;
        }

        private Scenes managerActionHandler(ManagerCommands action) throws NotEnoughRightsException {
            Scenes nextScene = Scenes.ACTIONS;
            switch (action) {
                case ORDERS -> ActionHandler.gotoOrdersPage((Manager) currentUser);
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
                case GO_TO_SHOWROOM -> ActionHandler.goToShowRoom(currentUser);
                case SETUP_MY_PROFILE -> nextScene = ActionHandler.setUpUserParameters(currentUser);
                case DELETE_ACCOUNT -> nextScene = ActionHandler.removeAccount(currentUser);
            }
            return nextScene;
        }

        private Scenes adminActionHandler(AdminCommands action) throws NotEnoughRightsException {
            Scenes nextScene = Scenes.ACTIONS;
            switch (action) {
                case USER_LIST ->  ActionHandler.viewUsers();
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
                case SHUT_DOWN -> nextScene = Scenes.SHUT_DOWN;
            }
            return nextScene;
        }
    }

}
