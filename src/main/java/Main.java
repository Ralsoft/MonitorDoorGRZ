import com.fasterxml.jackson.databind.ObjectMapper;
import models.Door;
import models.Messages;
import models.Monitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import service.JsonService;
import service.MqttService;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    static JsonService service = new JsonService();

    static MqttService mqttService = new MqttService();
    public static void main(String[] args) {
        LOG.info("Запуск программы.");
        try {
            mqttService.connectedMqtt(service.getConfigParam().getIpClient(), service.getConfigParam().getPortClient(), service.getConfigParam().getIdClient());
        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex);
        }
    }


}
