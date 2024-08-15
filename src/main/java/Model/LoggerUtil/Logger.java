package Model.LoggerUtil;

import Model.DataBaseHandler;
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
    private final DateTimeFormatter formatter;

    public Logger() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Логирует действие
     * @param level уровень сообщения
     * @param message сообщение для логирования
     */

    public void log(Levels level, String message) {
        DataBaseHandler.logMessage(level, message, LocalDateTime.now().format(formatter));
    }

    public List<String> getDataList(){
        List<List<String>> logTable = DataBaseHandler.getData(DataBaseHandler.logsTableName);
        List<String> logs = new ArrayList<>();
        for(List<String> list : logTable)
            logs.add(list.get(0) + " " + list.get(1) + " " + list.get(2));
        return logs;
    }

    /**
     * Сохраняет логи в файл
     * @param filePath путь к файлу
     */
    public void saveLogsToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath + ".txt"))) {
            for (String log : getDataList())
                writer.println(log);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения логов в файл, попробуйте снова " + e.getMessage());
        }
    }
}