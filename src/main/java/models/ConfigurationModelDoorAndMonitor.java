package models;

import lombok.Data;

@Data
public class ConfigurationModelDoorAndMonitor {
    String ipDoor;
    int portDoor;
    String ipMonitor;
    int portMonitor;
    String idClient;
    String ipClient;
    int portClient;

    @Override
    public String toString() {
        return "{" + "\n" +
                "\"ipDoor\" : " + ipDoor + ",\n" +
                "\"portDoor\" : " + portDoor + ",\n" +
                "\"ipMonitor\" : " + ipMonitor + ",\n" +
                "\"portMonitor\" : " + portMonitor + ",\n" +
                "\"clientId\" : " + idClient + ",\n" +
                "\"ipClient\" : " + ipClient + ",\n" +
                "\"portClient\" : " + portClient + "\n" +
                '}';
    }
}
