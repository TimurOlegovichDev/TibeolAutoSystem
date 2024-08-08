package Model.LoggerUtil;

import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для логирования действий
 */
public class Logger {

    @Getter
    private List<String> logs;
    private final DateTimeFormatter formatter;

    public Logger() {
        this.logs = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Логирует действие
     *
     * @param message сообщение для логирования
     */

    public void log(String message, Levels level) {
        String log = String.format("[%s] %s %s", LocalDateTime.now().format(formatter), level.toString(), message);
        logs.add(log);
    }

    /**
     * Сохраняет логи в файл
     *
     * @param filePath путь к файлу
     */

    public void saveLogsToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String log : logs) {
                writer.println(log);
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения логов в файл, попробуйте снова");
        }
    }
}