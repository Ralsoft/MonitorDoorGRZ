package models;

import db.ParamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;

public class Settings {

    private static final Logger LOG = LogManager.getLogger(Settings.class);
    static JsonService jsonService = new JsonService();
    static ParamRepository repository = new ParamRepository();


    public static Host getHostDoor(int cameraNumber) {
        try{
            var ipDoor = repository.getIpDoor(cameraNumber);
            var portDoor = repository.getPortDoor(cameraNumber);
            LOG.info("Получение информации о хосте двери. IP: " + ipDoor + " PORT: " + portDoor);
            return new Host(ipDoor, portDoor);
        }catch (Exception e){
            LOG.error("Ошибка: " + e);
        }
        return null;
    }

    public static int getDoorMode(int cameraNumber) {
        try{
            var doorMode = repository._getDoorMode(cameraNumber);
            LOG.info("Получение из БД режима работы контроллера. doorMode: " + doorMode );
            return doorMode;
        }catch (Exception e){
            LOG.error("Ошибка: " + e);
            return 5;
        }

    }

    public static void updateDoorMode(int modeDoor, int camNum) {
        try{
            var Doors = repository._updateDoorMode(modeDoor,camNum);
            LOG.info("Запрос на обновление в БД параметра. doorMode: " + modeDoor );
            //return doorMode;
        }catch (Exception e){
            LOG.error("Ошибка: " + e);
            //return 5;
        }

    }

    public static Host getHostMonitor(int cameraNumber) {
        try{
            var ipMonitor = repository.getIpMonitor(cameraNumber);
            var portMonitor = repository.getPortMonitor(cameraNumber);
            LOG.info("Получение информации о хосте монитора. IP: " + ipMonitor + " PORT: " + portMonitor);
            return new Host(ipMonitor, portMonitor);
        }catch (Exception e){
            LOG.error("Не найден ip или port монитора.");
        }
        return null;
    }
}
