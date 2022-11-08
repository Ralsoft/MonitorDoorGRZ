package models;

public class MonitorDoor {
    public String ipDoor;
    public int portDoor;
    public String ipMonitor;
    public int portMonitor;

    public MonitorDoor() {
    }

    public MonitorDoor(String ipDoor, int portDoor, String ipMonitor, int portMonitor) {
        this.ipDoor = ipDoor;
        this.portDoor = portDoor;
        this.ipMonitor = ipMonitor;
        this.portMonitor = portMonitor;
    }
}
