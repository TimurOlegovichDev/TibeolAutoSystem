package Controller;

import Controller.ActionHandlers.AdminHandlers.AdminMainActionHandler;
import Controller.ActionHandlers.ClientHandler.ClientMainActionHandler;
import Controller.ActionHandlers.ManagerHandler.ManagerMainActionHandler;
import Model.Entities.Users.*;
import Model.Exceptions.UserExc.*;
import Model.LoggerUtil.Levels;
import Model.LoggerUtil.LogActions;
import Model.LoggerUtil.Logger;
import Model.UserManagement.AuthenticationManager;
import Model.UserManagement.Encryptor;
import Model.UserManagement.RegistrationManager;
import lombok.Getter;
import ui.Menu;
import ui.out.Printer;
import ui.messageSrc.Messages;

/**
 * Главный класс, который отвечает за переключение и контроль сцен.
 *
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

    @Getter
    volatile User currentUser;

    /**
     * Основной переключатель, который с помощью сцен переключается между действиями
     * @see Scenes
     */

    public void run() {
        Controller.logger.log(Levels.INFO, "Starting system!");
        for (; ; ) {
            switch (Scenes.currentScene) {

                case GREETING -> greeting();

                case CHOOSING_ROLE -> chooseRegistrationOrAuth();

                case ACTIONS -> actionController.distributeActionsByRoots();

                case EXIT_FROM_ACCOUNT -> logOut();

                case SHUT_DOWN -> {
                    return;
                }
            }
        }
    }

    public void greeting() {
        Menu.greeting();
        Scenes.currentScene.nextStep();
    }

    public void chooseRegistrationOrAuth() {
        Printer.print(Messages.ACTIONS_TO_ENTER.getMessage());
        signHandler();
    }

    public void signHandler() {
        try {
            currentUser = (Menu.chooseRegistrationOrAuth().equals("Log in")) ? authorization() : registration();
            Printer.print("Successfully! Your ID " + currentUser.getID() + "\t Name: " + currentUser.getName() + "\t Role: " + currentUser.getAccessLevel().getValue());
            logger.log(Levels.INFO, LogActions.USER_REGISTERED.getText() + currentUser.toString());
            Scenes.currentScene = Scenes.ACTIONS;
            return;
        } catch (Exception e) {
            Printer.print("Invalid input data, try again!");
        }
        Scenes.currentScene = Scenes.CHOOSING_ROLE;
    }

    private User registration() throws RegistrationInterruptException, UserAlreadyExistsException {
        User user = RegistrationManager.registration(
                Menu.chooseRole(),
                Menu.getUserName(),
                Menu.getUserPassword()
        );
        if (user instanceof Client || user instanceof Manager)
            user.setPhoneNumber(Menu.getUserPhoneNumber());
        return user;
    }

    private User authorization() throws Exception {
        return AuthenticationManager.authentication(
                Menu.getUserName(),
                Encryptor.encrypt(Menu.getUserPassword())
        );
    }

    public void logOut() {
        logger.log(Levels.INFO, LogActions.USER_EXIT.getText() + currentUser.toString());
        currentUser = null;
        Scenes.currentScene = Scenes.CHOOSING_ROLE;
    }

    /**
     * Основной контроллер, который переключает действия в зависимости от уровня доступа
     */

    private class ActionController {

        private void distributeActionsByRoots() {
            Scenes.currentScene = Scenes.ACTIONS;
            switch (currentUser.getAccessLevel()) {
                case CLIENT -> ClientMainActionHandler.distribute(Menu.clientChoosingAction());
                case MANAGER -> ManagerMainActionHandler.distribute(Menu.managerChoosingAction());
                case ADMINISTRATOR -> AdminMainActionHandler.distribute(Menu.adminChoosingAction());
            }
        }

    }
}
