package ui.Out;

import java.util.List;

public abstract class Printer {

    public static void print(String text){
        System.out.println(TextFormatter.formatText(text.split(" ")));
    }

    public static void print(List<?> list){

    }
}
