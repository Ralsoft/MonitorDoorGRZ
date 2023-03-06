package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationModelDoorAndMonitor {
    String mqttUsername = "admin";
    String mqttPassword = "333";
    String idClient = "MonitorDoor";
    String ipClient = "194.87.237.67";
    int portClient = 1883;
    String databaseLogin = "SYSDBA";
    String databasePassword = "temp";
    String databasePath = "C:\\\\Program Files (x86)\\\\CardSoft\\\\DuoSE\\\\Access\\\\ShieldPro_rest.gdb";
    //String databasePath = "C:\\\\JAVA\\\\HL.gdb";
    //String databaseIp = "127.0.0.1";
    //String databaseIp = "192.168.1.5";  //hedliner
    String databaseIp = "26.98.93.81";

    ArrayList<Message> adMessages = new ArrayList<>(){{
        add(new Message((byte) 0, (byte) 0, (byte) 2, "Ворота 1"));
        add(new Message((byte) 8, (byte) 0, (byte) 2, "Шмитовский проезд 39"));
    }};

    int databasePort = 3050;
    int intervalInSeconds = 10;

    /*Map<Integer, MonitorDoor> monitorDoorDictionary = new HashMap<>() {{
        put(1, new MonitorDoor("1244", 1234, "192.168.8.110", 1985));
        put(2, new MonitorDoor("1244", 1234, "192.168.8.111", 1245));
    }};*/
}
