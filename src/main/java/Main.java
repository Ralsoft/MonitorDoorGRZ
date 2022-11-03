import com.fasterxml.jackson.databind.ObjectMapper;
import models.ConfigurationModelDoorAndMonitor;
import service.JsonService;
import service.MqttService;
import models.Door;
import models.Monitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    static JsonService service = new JsonService();
    static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {
        LOG.info("������ ���������.");
        try {
            var mqttService = new MqttService(service.getConfigParam().getIpClient(), service.getConfigParam().getPortClient(), service.getConfigParam().getIdClient());

            LOG.info("������� �������� �� ����� Parking/MonitorDoor/#");
            mqttService.getMqttClient().subscribeWithResponse("Parking/MonitorDoor/#", (topic, message) -> {
                try {
                    LOG.info("�������� ���������. TOPIC: " + topic + " MESSAGE: " + message);
                    String json = new String(message.getPayload());
                    switch (topic) {
                        case "Parking/MonitorDoor/Monitor/View/" -> {
                            try {
                                LOG.info("������ ����� - Parking/MonitorDoor/Monitor/View/");
                                var monitor = mapper.readValue(json, Monitor.class);
                                monitor.sendMessages();
                            } catch (Exception ex) {
                                LOG.error("������: " + ex.getMessage());
                            }
                        }
//                        case "Parking/MonitorDoor/Door/Open/1/" -> {
//                            try {
//                                LOG.info("������ ����� - Parking/MonitorDoor/Door/Open/1/");
//                                var door = mapper.readValue(json, Door.class);
//                                door.openDoor1();
//                            } catch (Exception ex) {
//                                LOG.error("������: " + ex.getMessage());
//                            }
//                        }
                        case "Parking/MonitorDoor/Door/Open/0/" -> {
                            try {
                                LOG.info("������ ����� - Parking/MonitorDoor/Door/Open/0/");
                                var door = mapper.readValue(json, Door.class);
                                door.openDoor0();
                            } catch (Exception ex) {
                                LOG.error("������: " + ex.getMessage());
                            }
                        }
                        case "Parking/MonitorDoor/Door/Warning/" -> {
                            try {
                                LOG.info("������ ����� - Parking/MonitorDoor/Door/Warning/");
                                //
                            } catch (Exception ex) {
                                LOG.error("������: " + ex.getMessage());
                            }
                        }
                    }
                } catch (Exception ex) {
                    LOG.error("������: " + ex.getMessage());
                }
            });
            LOG.info("�������� ��������� �������.");
        } catch (Exception ex) {
            LOG.error("������: " + ex.getMessage());
        }
    }
}
