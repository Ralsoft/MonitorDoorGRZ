package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;

public class Settings {

    private static final Logger LOG = LogManager.getLogger(Settings.class);
    static JsonService jsonService = new JsonService();

    public static Host getHostDoor(int cameraNumber) {
        LOG.info("Получение информации о хосте двери. IP: " + jsonService.getConfigParam().ipDoor + " PORT: " + jsonService.getConfigParam().portDoor);
        return new Host(jsonService.getConfigParam().ipDoor, jsonService.getConfigParam().portDoor);
    }

    public static Host getHostMonitor(int cameraNumber) {
        LOG.info("Получение информации о хосте монитора. IP: " + jsonService.getConfigParam().ipMonitor + " PORT: " + jsonService.getConfigParam().portMonitor);
        return new Host(jsonService.getConfigParam().ipMonitor, jsonService.getConfigParam().portMonitor);
    }
}
