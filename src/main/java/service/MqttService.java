package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;

public class MqttService {
    private static final Logger LOG = LogManager.getLogger(MqttService.class);

    private MqttClient mqttClient;
    JsonService jsonService = new JsonService();
    MqttConnectOptions options;

    public void connectedMqtt(String host, int port, String clientName) throws InterruptedException {
        try {
            LOG.info("Попытка подключения. HOST: " + host + " PORT: " + port + " CLIENT_NAME: " + clientName);
            mqttClient = new MqttClient(
                    "tcp://" + host
                            + ":" + port, clientName);
            options = new MqttConnectOptions();
            options.setExecutorServiceTimeout(5000);
            options.setMaxReconnectDelay(5000);
            options.setKeepAliveInterval(5000);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(5000);
            options.setUserName(jsonService.getConfigParam().getMqttUsername());
            options.setPassword(jsonService.getConfigParam().getMqttPassword().toCharArray());
            mqttClient.connect(options);
            LOG.info("Успешное подключение.");
        } catch (MqttException e) {
            Thread.sleep(5000);
            LOG.error("Ошибка: " + e);
            if (!mqttClient.isConnected()) {
                connectedMqtt(host, port, clientName);
            }
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }
}
