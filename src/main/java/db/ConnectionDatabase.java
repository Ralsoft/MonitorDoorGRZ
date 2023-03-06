package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {
    private static final Logger LOG = LogManager.getLogger(ConnectionDatabase.class);

    JsonService jsonService = new JsonService();

    Connection getConnectionDb(){

        Connection connection = null;
        try {
            if ( connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        "jdbc:firebirdsql://" + jsonService.getConfigParam().getDatabaseIp() + ":" +
                                jsonService.getConfigParam().getDatabasePort() + "/" +
                                jsonService.getConfigParam().getDatabasePath() + "?encoding=WIN1251",
                        jsonService.getConfigParam().getDatabaseLogin(),
                        jsonService.getConfigParam().getDatabasePassword()
                );
            }
            if (!connection.isClosed()) {
                LOG.info("Соединение с базой данных произошло успешно!");
            }
        } catch (SQLException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }

        return connection;
    }
}
