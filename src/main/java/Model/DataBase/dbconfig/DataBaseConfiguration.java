package Model.DataBase.dbconfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class DataBaseConfiguration {

    static {
        FileInputStream fileInputStream;
        Properties property = new Properties();

        try {
            fileInputStream = new FileInputStream("src/main/resources/configs/database.properties");
            property.load(fileInputStream);
            URL = "jdbc:" + property.getProperty("DRIVER") + "://localhost:" + property.getProperty("PORT") + "/" + property.getProperty("POSTGRES_DB");
            USER_NAME = property.getProperty("POSTGRES_USER");
            PASSWORD = property.getProperty("POSTGRES_PASSWORD");
            fileInputStream.close();
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует или в нем ошибка!");
        }
    }

    public static String URL;

    public static String USER_NAME;

    public static String PASSWORD;

}
