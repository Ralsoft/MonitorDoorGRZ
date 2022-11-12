import com.fasterxml.jackson.databind.ObjectMapper;
import models.Door;
import models.Messages;
import models.Monitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;
import service.MqttService;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    static JsonService service = new JsonService();
    static ObjectMapper mapper = new ObjectMapper();
    static MqttService mqttService = new MqttService();
    public static void main(String[] args) {
        LOG.info("Запуск программы.");
        try {
            mqttService.connectedMqtt(service.getConfigParam().getIpClient(), service.getConfigParam().getPortClient(), service.getConfigParam().getIdClient());

            LOG.info("Попытка подписки на топик Parking/MonitorDoor/#");
            mqttService.getMqttClient().subscribe("Parking/MonitorDoor/#", (topic, message) -> {
                try {
                    LOG.info("Получено сообщение. TOPIC: " + topic + " MESSAGE: " + message);
                    String json = new String(message.getPayload());
                    switch (topic) {
                        case "Parking/MonitorDoor/Monitor/View" -> {
                            try {
                                LOG.info("Принят топик - Parking/MonitorDoor/Monitor/View");
                                var monitor = mapper.readValue(json, Monitor.class);
                                monitor.sendMessages();
                                Thread.sleep(50);
                            } catch (Exception ex) {
                                LOG.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/Door/Open" -> {
                            try {
                                LOG.info("Принят топик - Parking/MonitorDoor/Door/Open");
                                var door = mapper.readValue(json, Door.class);
                                door.openDoor0();
                            } catch (Exception ex) {
                                LOG.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/Door/Warning/" -> {
                            try {
                                LOG.info("Принят топик - Parking/MonitorDoor/Door/Warning/");
                                //
                            } catch (Exception ex) {
                                LOG.error("Ошибка: " + ex);
                            }
                        }
                    }
                } catch (Exception ex) {
                    LOG.error("Ошибка: " + ex);
                }
            });
            LOG.info("Подписка произошла успешно.");
        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex);
        }
    }
}
