package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ParamRepository {
    private static final Logger LOG = LogManager.getLogger(ParamRepository.class);
    ConnectionDatabase connectionDatabase = new ConnectionDatabase();
    Connection connection = connectionDatabase.getConnectionDb();


    public String getIpMonitor(int camNum){

        String query = "select tablo_ip from hl_param where id_cam = " + camNum;

        return getValueStringFromDb(query);
    }

    public int getPortMonitor(int camNum){

        String query = "select tablo_port from hl_param where id_cam = " + camNum;

        return getValueIntFromDb(query);
    }

    public String getIpDoor(int camNum){

        String query = "select box_ip from hl_param where id_cam = " + camNum;

        return getValueStringFromDb(query);
    }

    public int getPortDoor(int camNum){

        String query = "select box_port from hl_param where id_cam = " + camNum;

        return getValueIntFromDb(query);
    }

    public int _getDoorMode(int camNum){

        //String query = "select box_port from hl_param where id_cam = " + camNum;
        String query = "select hlp.mode from hl_param hlp where hlp.id_cam=" + camNum;
        /*
        0 - Открывание реле в режиме шлюз. Сначала шлагбаум ,потом ворота
        1 - Отрывание реле в режиме все. Одновременно и ворота и шлагбаум
         */


        return getValueIntFromDb(query);
        //return query;
    }

    public int _updateDoorMode(int modeDoor, int camNum){

        //String query = "select box_port from hl_param where id_cam = " + camNum;
        //String query = "select hlp.mode from hl_param hlp where hlp.id_cam=2";
        String query = "UPDATE HL_PARAM SET MODE = " + modeDoor + " WHERE ID_CAM =" + camNum;
        /*
        0 - Открывание реле в режиме шлюз. Сначала шлагбаум ,потом ворота
        1 - Отрывание реле в режиме все. Одновременно и ворота и шлагбаум
         */


        return getValueSetFromDb(query);
        //return query;
    }

    private String getValueStringFromDb(String query){
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                query = resultSet.getString(1);
            }
        } catch (SQLException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }

        return query;
    }

    private int getValueIntFromDb(String query){
        int result = 0;
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }

        return result;
    }

    private int getValueSetFromDb(String query){
        int result = 0;
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }

        return result;
    }
}
