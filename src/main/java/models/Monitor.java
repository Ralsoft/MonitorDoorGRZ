package models;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Data
public class Monitor {
    private static final Logger LOG = LogManager.getLogger(Monitor.class);
    public int camNumber;
    public List<Messages> messages;

    public Monitor() {
    }

    public Monitor(int camNumber, List<Messages> messages) {
        this.camNumber = camNumber;
        this.messages = messages;
    }

    public void sendMessages() {
        try {
            var echoClient = new EchoClient(Settings.getHostMonitor(camNumber));
            //clear
            echoClient.sendEchoWithOutReceive(new byte[] {0x03, 0x44, 0x47});

            for (var item : messages) {
                echoClient.sendEchoWithoutReceive(item.text, item.x, item.y, item.color);
            }

            echoClient.close();
        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex.getMessage());
        }
    }



}
