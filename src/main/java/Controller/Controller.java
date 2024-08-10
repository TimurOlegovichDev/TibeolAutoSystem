package Controller;

import Model.DataBase.DataBaseHandler;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.*;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import Model.LoggerUtil.Logger;
import Model.UserManagement.AuthenticationManager;
import Model.UserManagement.Encryptor;
import Model.UserManagement.RegistrationManager;
import ui.Menu;
import ui.messageSrc.commands.*;
import ui.out.Printer;
import ui.messageSrc.Messages;

/**
 * Главный класс, который отвечает за переключение и контроль сцен.
 * @see Scenes
 * Контроллер взаимодействует с классом Menu для отображения меню и подсказок пользователю.
 */

public class Controller extends Thread {

    public static final Logger logger = new Logger();

    /**
     * Реализация синглтона, для предотвращения множества различных объектов контроллера
     */

    private static volatile Controller instance;

    public static synchronized Controller getController() {
        return (instance == null) ? (instance = new Controller()) : instance;
    }

    private Controller() {
    }

    private final ActionController actionController = new ActionController();

    volatile User currentUser;

    /**
     * Переключатель задержки, она нужна для создания "анимации" и более привычного обращения с программой
     */

    boolean timeDelayOn = false;

    /**
     * Основной переключатель, который с помощью сцен переключается между действиями
     *
     *
     */

    public void run() {

        for(Scenes scene = Scenes.GREETING; checkSceneCycle(scene); ){
            timeDelay(3000);
            switch (scene) {

                case GREETING -> scene = greeting();

                case CHOOSING_ROLE -> scene = chooseRegistrationOrAuth();

                case REGISTRATION -> scene = registrationHandler();

                case AUTHORIZATION -> scene = authorizationHandler();

                case ACTIONS -> scene = actionController.distributeActions();

                case EXIT_FROM_ACCOUNT -> scene = logOut();

                case SHUT_DOWN -> scene = Scenes.ACTIONS;
            }
        }
    }

    private boolean checkSceneCycle(Scenes scene){
        return !(scene.equals(Scenes.SHUT_DOWN) && Menu.areYouSure(Messages.SHUT_DOWN_WARNING.getMessage()));
    }

    public Scenes greeting(){
        Menu.greeting();
        return Scenes.GREETING.nextStep();
    }

    public Scenes chooseRegistrationOrAuth(){
        Printer.print(Messages.ACTIONS_TO_ENTER.getMessage());
        return (Menu.chooseRegistrationOrAuth().equals("Войти")) ? Scenes.AUTHORIZATION : Scenes.REGISTRATION;
    }

    public Scenes registrationHandler(){
        try {
            currentUser = registration();
            timeDelay(200);
            Printer.print("Вы создали аккаунт с ID " + currentUser.getID() + " и именем " + currentUser.getName() + " Ваша роль: " + currentUser.getAccessLevel().getValue());
            logger.log(LogActions.USER_REGISTERED.getText() + currentUser.toString(), Levels.INFO);
            return Scenes.ACTIONS;
        } catch (RegistrationInterruptException e) {
            Printer.print(Messages.ERROR.getMessage());
        } catch (UserAlreadyExistsException e) {
            Printer.print(Messages.USER_ALREADY_EXISTS.getMessage());
        }
        return Scenes.CHOOSING_ROLE;
    }

    public Scenes authorizationHandler(){
        try {
            authorization();
            timeDelay(2000);
            Printer.print("Вы вошли в аккаунт под ID " + currentUser.getID() + " и именем " + currentUser.getName() + " Ваша роль: " + currentUser.getAccessLevel().getValue());
            userNotification();
            logger.log(LogActions.USER_AUTHORIZED.getText() + currentUser.toString(), Levels.INFO);
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
//        if(currentUser instanceof Client && !((Client) currentUser).getMessages().isEmpty())
//           Printer.notify(((Client) currentUser).getMessages().size());
    }

    private User registration() throws RegistrationInterruptException, UserAlreadyExistsException {
        User newUser = RegistrationManager.registration(
                Menu.chooseRole(),
                Menu.getUserName(),
                Menu.getUserPassword()
        );
        if(newUser instanceof Client || newUser instanceof Manager)
            newUser.setPhoneNumber(Menu.getUserPhoneNumber());
        return newUser;
    }

    private void authorization() throws Exception, InvalidPasswordException {
        Printer.printCentered("Добро пожаловать!");
        currentUser = AuthenticationManager.authentication(
                Menu.getUserName(),
                Encryptor.encrypt(Menu.getUserPassword())
        );
    }

    public Scenes logOut(){
        logger.log(LogActions.USER_EXIT.getText() + currentUser.toString(), Levels.INFO);
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
                case SETUP_MY_PROFILE -> nextScene = ActionHandler.setUpUserParameters(currentUser.getID());
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
                case SETUP_MY_PROFILE -> nextScene = ActionHandler.setUpUserParameters(currentUser.getID());
                case DELETE_ACCOUNT -> nextScene = ActionHandler.removeAccount(currentUser);
            }
            return nextScene;
        }

        private Scenes adminActionHandler(AdminCommands action) throws NotEnoughRightsException {
            Scenes nextScene = Scenes.ACTIONS;
            switch (action) {
                case GO_TO_USER_LIST ->  ActionHandler.gotoUserListPage((Administrator) currentUser);
                case GET_LOG_LIST -> ActionHandler.getLogList();
                case SAVE_LOG_LIST -> ActionHandler.saveLogList();
                case DELETE_ACCOUNT -> nextScene = ActionHandler.removeAccount(currentUser);
                case SETUP_MY_PROFILE -> nextScene = ActionHandler.setUpUserParameters(currentUser.getID());
                case EXIT_FROM_ACCOUNT -> nextScene = Scenes.EXIT_FROM_ACCOUNT;
                case SHUT_DOWN -> nextScene = Scenes.SHUT_DOWN;
            }
            return nextScene;
        }
    }

}
