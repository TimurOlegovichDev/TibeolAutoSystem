package ui.out;

import lombok.Getter;
import ui.messageSrc.Messages;

import java.util.List;

public abstract class Printer {

    @Getter
    private static final String line = "-------------------------------------------------------------------\n";

    public static void print(String text){
        System.out.println(line + TextFormatter.formatText(text.split(" ")));
    }

    public static void print(List<?> list){

    }

    public static void print(String[] text){
        System.out.println(line + TextFormatter.formatText(text));
    }

    public static void printCommands(String[] commands){
        System.out.println(line + Messages.CHOOSE_U_ACTION.getMessage());
        for(String command : commands){
            System.out.println("- " + command);
        }
    }
}
