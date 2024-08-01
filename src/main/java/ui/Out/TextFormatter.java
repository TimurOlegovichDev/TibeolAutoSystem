package ui.Out;

public abstract class TextFormatter {

    private static final int WIDTH = 80;

    public static String formatText(String ... textArray) {

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
}
