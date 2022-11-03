package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttService {
    private static final Logger LOG = LogManager.getLogger(MqttService.class);

    private MqttClient mqttClient;

    public MqttService(String host, int port, String clientName){
        try {
            LOG.info("Попытка подключения. HOST: " + host + " PORT: " + port + " CLIENT_NAME: " + clientName);
            mqttClient = new MqttClient(
                    "tcp://" + host
                            + ":" + port, clientName);
            mqttClient.connect();
            LOG.info("Успешное подключение.");
        } catch (MqttException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }


    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void publish(final String topic, final String payload) {
        try {
            LOG.info("Попытка публикации. TOPIC: " + topic + " PAYLOAD: " + payload);
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            mqttClient.publish(topic, mqttMessage);
            LOG.info("Сообщение успешно опубликовано.");
        } catch (Exception e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }
}
