package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Door;
import models.Monitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import javax.sound.sampled.AudioFormat;
import java.beans.Encoder;

public class MqttService {
    private static final Logger LOG = LogManager.getLogger(MqttService.class);

    private MqttClient mqttClient;
    JsonService jsonService = new JsonService();
    MqttConnectOptions options;

    static ObjectMapper mapper = new ObjectMapper();

    public void connectedMqtt(String host, int port, String clientName) throws InterruptedException {
        try {
            LOG.info("Попытка подключения. HOST: " + host + " PORT: " + port + " CLIENT_NAME: " + clientName);
            mqttClient = new MqttClient(
                    "tcp://" + host
                            + ":" + port, MqttClient.generateClientId());
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setUserName(jsonService.getConfigParam().getMqttUsername());
            options.setPassword(jsonService.getConfigParam().getMqttPassword().toCharArray());


            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    LOG.error("Соединение потеряно " + throwable.getMessage());
                    if (!mqttClient.isConnected()) {
                        try {
                            Thread.sleep(5000);
                            connectedMqtt(host, port, clientName);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    LOG.info("пришло сообщение " + s);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    LOG.info("deliveryComplete " + iMqttDeliveryToken.toString());
                }
            });
            mqttClient.connect(options);
            subscribe();
            LOG.info("Успешное подключение.");
        } catch (MqttException e) {
            LOG.error("Ошибка: " + e);
            if (!mqttClient.isConnected()) {
                Thread.sleep(5000);
                connectedMqtt(host, port, clientName);
            }
        }
    }

    private void subscribe() throws MqttException {
        LOG.info("Попытка подписки на топик Parking/MonitorDoor/#");
        mqttClient.subscribe("Parking/MonitorDoor/#", (topic, message) -> {
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
    }
    public MqttClient getMqttClient() {
        return mqttClient;
    }
}
