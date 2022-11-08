package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;

public class Settings {

    private static final Logger LOG = LogManager.getLogger(Settings.class);
    static JsonService jsonService = new JsonService();

    public static Host getHostDoor(int cameraNumber) {
        try{
            var ipDoor = jsonService.getConfigParam().monitorDoorDictionary.get(cameraNumber).ipDoor;
            var portDoor = jsonService.getConfigParam().monitorDoorDictionary.get(cameraNumber).portDoor;
            LOG.info("Получение информации о хосте двери. IP: " + ipDoor + " PORT: " + portDoor);
            return new Host(ipDoor, portDoor);
        }catch (Exception e){

        }
        return null;
    }

    public static Host getHostMonitor(int cameraNumber) {
        try{
            var ipMonitor = jsonService.getConfigParam().monitorDoorDictionary.get(cameraNumber).ipMonitor;
            var portMonitor = jsonService.getConfigParam().monitorDoorDictionary.get(cameraNumber).portMonitor;
            LOG.info("Получение информации о хосте монитора. IP: " + ipMonitor + " PORT: " + portMonitor);
            return new Host(ipMonitor, portMonitor);
        }catch (Exception e){

        }
        return null;
    }
}
