package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Door;
import models.Monitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.net.InetAddress;

public class MqttService {
    private static final Logger log = LogManager.getLogger(MqttService.class);

    private MqttClient mqttClient;
    JsonService jsonService = new JsonService();
    MqttConnectOptions options;

    static ObjectMapper mapper = new ObjectMapper();

    public void connectedMqtt(String host, int port) throws InterruptedException {
        try(MqttClient mqttClient = new MqttClient(
                "tcp://" + host + ":" + port,
                InetAddress.getLocalHost() + "-Monitor")) {
            log.info(
                    "Создание подключения клиента... HOST_NAME = " + jsonService.getConfigParam().getIpClient() +
                            ", PORT = " + jsonService.getConfigParam().getPortClient() +
                            ", USERNAME = " + jsonService.getConfigParam().getMqttUsername() +
                            ", PASSWORD = " + jsonService.getConfigParam().getMqttPassword()
            );


            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(5000);
            options.setUserName(jsonService.getConfigParam().getMqttUsername());
            options.setPassword(jsonService.getConfigParam().getMqttPassword().toCharArray());
            log.info(
                    "Выставленные настройки MQTT: " +
                            "Автоматический реконнект = " + options.isAutomaticReconnect()
            );

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    log.warn("Соединение с MQTT потеряно!");
                    try {
                        subscribe();
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) {
                    log.info("Получено сообщение: " + s);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                }
            });
            mqttClient.connect(options);
            log.info("Успешное подключение.");
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
    }

    private void subscribe() throws MqttException {
        log.info("Выполнение подписки на топик... ТОПИК: Parking/MonitorDoor/#");
        mqttClient.subscribe("Parking/MonitorDoor/#", (topic, message) -> {
            try {
                log.info("Получено сообщение! ТОПИК: " + topic + " СООБЩЕНИЕ: " + message);
                String json = new String(message.getPayload());
                switch (topic) {
                    case "Parking/MonitorDoor/Monitor/View" -> {
                        try {
                            log.info("Принят топик - Parking/MonitorDoor/Monitor/View");
                            var monitor = mapper.readValue(json, Monitor.class);
                            monitor.sendMessages();
                            Thread.sleep(50);
                        } catch (Exception ex) {
                            log.error("Ошибка: " + ex);
                        }
                    }
                    case "Parking/MonitorDoor/Door/Open" -> {
                        try {
                            log.info("Принят топик - Parking/MonitorDoor/Door/Open");
                            var door = mapper.readValue(json, Door.class);
                            door.openDoor0();
                        } catch (Exception ex) {
                            log.error("Ошибка: " + ex);
                        }
                    }
                    case "Parking/MonitorDoor/Door/Warning/" -> {
                        try {
                            log.info("Принят топик - Parking/MonitorDoor/Door/Warning/");
                            //
                        } catch (Exception ex) {
                            log.error("Ошибка: " + ex);
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("Ошибка: " + ex);
            }
        });
        log.info("Подписка на топик Parking/MonitorDoor/# произошла успешно.");
    }
    public MqttClient getMqttClient() {
        return mqttClient;
    }
}
