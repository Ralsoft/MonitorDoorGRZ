
import com.fasterxml.jackson.databind.ObjectMapper;
import models.EchoClient;
import models.Message;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        var mqttService = new MqttService("194.87.237.67", "1883", "DoorMonitorGRZ");

        ObjectMapper mapper = new ObjectMapper();
        try {
            mqttService.getMqttClient().subscribeWithResponse("Parking/MonitorDoor/#", (topic, message) -> {
                String json = new String(message.getPayload());
                switch (topic) {
                    case "Parking/MonitorDoor/Monitor/View/" -> {
                        Map map = mapper.readValue(json, Map.class);

                        String ip = map.get("ip").toString();
                        int port = Integer.parseInt(map.get("port").toString());

                        List<Message> messages = (List<Message>) map.get("messages");
                        var echoClient = new EchoClient(ip, port);
                        for (var item : messages) {
                            echoClient.sendEcho(item.Text, item.X, item.Y, item.Color);
                        }
                        echoClient.close();

                    }
                    case "Parking/MonitorDoor/Door/Open/" -> {

                    }
                    case "Parking/MonitorDoor/Door/Warning/" -> {

                    }
                }
            });
        } catch (MqttException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
