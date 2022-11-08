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
                            + ":" + port, MqttClient.generateClientId());
            mqttClient.connect();
            LOG.info("Успешное подключение.");
        } catch (MqttException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }


    public MqttClient getMqttClient() {
        return mqttClient;
    }
}
