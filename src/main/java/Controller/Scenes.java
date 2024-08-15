package Controller;

import lombok.Getter;

/**
 *  Представляет собой перечисление (enum) в Java, которое определяет различные сцены или состояния в приложении. Каждая сцена имеет уникальный номер
 */


public enum Scenes {

    GREETING(1),
    CHOOSING_ROLE(2),
    REGISTRATION(3),
    AUTHORIZATION(4),
    ACTIONS(5),
    EXIT_FROM_ACCOUNT(6),
    SHUT_DOWN(7);

    @Getter
    private final int number;
    @Getter
    public static Scenes currentScene = GREETING;
    Scenes(int number){
        this.number = number;
    }

    /**
     * Переключение сцены на один шаг вперед.
     * Так как нумерация списка начинается с 0, а нумерация сцен с 1,
     * то единицу прибавлять не нужно для следующего шага,
     * достаточно взять элемент из списка по значению сцены
     */

    void nextStep(){
        if(!currentScene.equals(SHUT_DOWN))
            currentScene = Scenes.values()[currentScene.getNumber()];
    }
}
