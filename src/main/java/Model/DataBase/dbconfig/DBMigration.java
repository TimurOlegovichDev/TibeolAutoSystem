package Model.DataBase.dbconfig;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMigration {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DataBaseConfiguration.URL, DataBaseConfiguration.USER_NAME, DataBaseConfiguration.PASSWORD)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(
                            new JdbcConnection(connection)
                    );
            Liquibase liquibase =
                    new Liquibase(
                            "db/changelog/v.1.0/changelog.xml",
                            new ClassLoaderResourceAccessor(),
                            database
                    );
            liquibase.update();
            System.out.println("The migration was successful!");
        } catch (SQLException e){
            System.out.println("Got SQL exception " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Got DataBaseException " + e.getMessage());
        } catch (LiquibaseException e) {
            System.out.println("Got Liquibase exception " + e.getMessage());
        }
    }
}
