package ui.out;

import Model.Entities.Car.Car;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class TextFormatter {

    private static final int WIDTH = Printer.getLine().length();
    private static final String listIsEmpty = "Список пуст";

    static String formatText(String ... textArray) {

        StringBuilder result = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (String word : textArray) {
            if (line.length() + word.length() + 1 <= WIDTH) {
                line.append(word).append(' ');
            } else {
                result.append(line.toString().trim()).append('\n');
                line = new StringBuilder(word).append(' ');
            }
        }

        if (!line.isEmpty()) {
            result.append(line.toString().trim());
        }

        return result.toString();
    }

    static String formatText(List<?> list) {
        if(list.isEmpty())
            return centerText("Список пуст");
        StringBuilder stringBuilder = new StringBuilder();
        for(Object object : list)
            stringBuilder.append(object.toString()).append("\n");
        return stringBuilder.toString();
    }



    static String formatText(Map<?,?> map) {
        if(map.isEmpty())
            return centerText("Список пуст");
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<?,?> entry : map.entrySet())
            stringBuilder.append(entry.getValue().toString()).append("\n");
        return stringBuilder.toString();
    }

    static String formatForm(Map<?, Car> map) {
        if(map.isEmpty())
            return centerText("Список пуст");
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<?,Car> entry : map.entrySet())
            stringBuilder.append(entry.getValue().getForm()).append("\n");
        return stringBuilder.toString();
    }

    public static String centerText(String text) {
        int textLength = text.length();
        int padding = (WIDTH - textLength) / 2;
        StringBuilder centeredText = new StringBuilder();

        for (int i = 0; i < padding; i++)
            centeredText.append(" ");


        centeredText.append(text);

        for (int i = 0; i < padding; i++)
            centeredText.append(" ");


        return centeredText.toString();
    }
}
