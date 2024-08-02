package Controller;

import lombok.Getter;

public enum Scenes {

    GREETING(1),
    CHOOSING_ROLE(2),
    REGISTRATION(3),
    AUTHORIZATION(4),
    ACTIONS(5),
    EXIT_FROM_ACCOUNT(6),
    SHUT_DOWN(7);


    @Getter
    private int number;

    Scenes(int number){
        this.number = number;
    }

    public Scenes nextStep(){
        if(number==8) return Scenes.SHUT_DOWN;
        return Scenes.values()[number];
    }

}
