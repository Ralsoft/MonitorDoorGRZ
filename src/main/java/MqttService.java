import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttService {

    private MqttClient _mqttClient;

    public MqttService(String host, String port, String clientName){
        try {
            _mqttClient = new MqttClient(
                    "tcp://" + host
                            + ":" + port, clientName);
            _mqttClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getMqttClient() {
        return _mqttClient;
    }

    public void publish(final String topic, final String payload) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            _mqttClient.publish(topic, mqttMessage);

        } catch (Exception e) {

        }
    }
}
