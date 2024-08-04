package ui.out;

import Model.Entities.Car.Car;
import Model.Entities.Order.Order;
import lombok.Getter;
import ui.messageSrc.Messages;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class Printer {

    @Getter
    private static final String line = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n";
    private static final String bottomLine = "\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n";

    public static void print(String text){
        System.out.println(line + TextFormatter.formatText(text.split(" ")) + bottomLine);
    }

    public static void print(List<?> list){
        System.out.print(line);
        System.out.println(TextFormatter.formatText(list));
        System.out.print(bottomLine);
    }

    public static void print(Map<?,?> map){
        System.out.print(line);
        System.out.println(TextFormatter.formatText(map));
        System.out.print(bottomLine);
    }

    public static void print(Stream<?> stream){
        System.out.print(line);
        System.out.println(TextFormatter.formatText(stream.toList()));
        System.out.print(bottomLine);
    }

    public static void print(String[] text){
        System.out.println(line + TextFormatter.formatText(text) + bottomLine);
    }

    public static void printCommands(String[] commands){
        System.out.println(line + Messages.CHOOSE_U_ACTION.getMessage());
        for(String command : commands)
            System.out.println(TextFormatter.formatText("- " + command));
        System.out.print(bottomLine);
    }

    public static void printCommandsWithCustomQuestion(String[] commands, String question){
        System.out.println(line + question);
        for(String command : commands)
            System.out.println(TextFormatter.formatText("- " + command));
        System.out.print(bottomLine);
    }

    public static void printAsIs(String text) {
        System.out.println(line + text + bottomLine);
    }

    public static void printWithoutLines(String text) {
        System.out.println(line + text + bottomLine);
    }

    public static void notify(int countNewMessages) {
        print(Messages.NEW_MESSAGE.getMessage() + countNewMessages);
    }

    public static void print(List<?> list, boolean numbered) {
        if(!numbered) print(list);
        else {
            int index = 1;
            for(Object o : list){
                print( " " + index++ + ".  | " + o.toString());
            }
        }
    }

    public static void printCentered(String s) {
        System.out.println(line + TextFormatter.centerText(s) + bottomLine);
    }

    public static void printDealerCars(Map<Integer, Car> carData) {
        System.out.print(line);
        System.out.println(TextFormatter.formatForm(carData));
        System.out.print(bottomLine);
    }
}
