package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Door {
    public int modeDoor;

    private static final Logger LOG = LogManager.getLogger(Door.class);

    public int cameraNumber;

    byte[] dataOpenDoor = {0x03, 0x44, 0x47};
    byte[] dataOpenDoor0 = {0x04, 0x4F, 0x00, 0x4B};        //19.12.2022
    byte[] dataOpenDoor1 = {0x04, 0x4F, 0x01, 0x4A};
    byte[] gateway = {0x03, 0x44, 0x47};
    public void openDoor1() throws UnknownHostException {
        openDoor(dataOpenDoor);
    }

    public void openRelay1() throws UnknownHostException {
        openDoor(dataOpenDoor1);
    }

    public void openRelay(int numRelay) throws UnknownHostException {
        if(numRelay == 0) {
            openDoor(dataOpenDoor0);
        } else if(numRelay == 1) {
            openDoor(dataOpenDoor1);
        }

    }

    public void gateway() throws UnknownHostException {
        openDoor(gateway);
    }

    public void openDoor0() throws UnknownHostException {
        openDoor(dataOpenDoor);
    }

    public void openDoor(byte[] data) {
        try {
            var echoClient = new EchoClient(Settings.getHostDoor(cameraNumber)); // создается клиент. IP адрес берется из настроек по номеру камеры
            echoClient.sendEchoWithOutReceive(data);
            echoClient.close();
        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex.getMessage());
        }
    }
}
