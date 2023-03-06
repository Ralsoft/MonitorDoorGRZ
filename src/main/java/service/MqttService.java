package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Door;
import models.Monitor;
import models.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class MqttService {
    private static final Logger log = LogManager.getLogger(MqttService.class);
    JsonService jsonService = new JsonService();
    static ObjectMapper mapper = new ObjectMapper();

    private MqttClient _mqttClient;

    public MqttService(String host, int port){
        log.info(
                "Создание подключения клиента... HOST_NAME = " + jsonService.getConfigParam().getIpClient() +
                        ", PORT = " + jsonService.getConfigParam().getPortClient() +
                        ", USERNAME = " + jsonService.getConfigParam().getMqttUsername() +
                        ", PASSWORD = " + jsonService.getConfigParam().getMqttPassword()
        );
        try {
            _mqttClient = new MqttClient(
                    "tcp://" + host + ":" + port,
                    InetAddress.getLocalHost() + "-Monitor");

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(5000);
            options.setUserName(jsonService.getConfigParam().getMqttUsername());
            options.setPassword(jsonService.getConfigParam().getMqttPassword().toCharArray());

            log.info(
                    "Выставленные настройки MQTT: " +
                            "Автоматический реконнект = " + options.isAutomaticReconnect()
            );
            _mqttClient.connect(options);

            subscribe();
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
    }

    private void subscribe() {
        try {
//            log.info("Успешное подключение к MQTT. " + serverURI);
            log.info("Выполнение подписки на топик... ТОПИК: Parking/MonitorDoor/#");
            _mqttClient.subscribe("Parking/MonitorDoor/#", (topic, message) -> {
                try {
                    log.info("Получено сообщение! ТОПИК: " + topic + " СООБЩЕНИЕ: " + message);
                    String json = new String(message.getPayload());
                    switch (topic) {
                        case "Parking/MonitorDoor/Monitor/View" -> {
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/Monitor/View");
                                Monitor monitor = mapper.readValue(json, Monitor.class); // Преобразуем JSON в java объект
                                monitor.sendMessages();

                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/Door/Open" -> { // выдается команда на открывание шлагбаума по результатам валидации Doors исправлен на Door, как в валидаторе
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/Door/Open/1");
                                var door = mapper.readValue(json, Door.class);
                                int mode = Settings.getDoorMode(door.cameraNumber);

                                switch (mode) {
                                    case 0:
                                        log.info("Режим шлюза. Выполняем команду gateway()");
                                        door.gateway();
                                        break;
                                    case 1:
                                        log.info("Режим окрывать только реле 0. Выполняем команду openRelay0()");
                                        door.openRelay(0);
                                        break;
                                    case 2:
                                        log.info("Режим окрывать только реле 1. Выполняем команду openRelay1()");
                                        door.openRelay(1);
                                        break;
                                    case 3:
                                        log.info("Режим окрывать и реле 0, и реле 1. Выполняем команду openRelay 0 и 1");
                                        door.openRelay(0);
                                        door.openRelay(1);
                                        break;
                                }



                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/update/mode" -> { // Тест для ручного (с телефона) открывания
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/update/mode");
                                var door = mapper.readValue(json, Door.class);
                                Settings.updateDoorMode(door.modeDoor, door.cameraNumber);


                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/get/mode" -> { // ????? ??? ????????? ?????? ?????
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/update/mode");
                                var door = mapper.readValue(json, Door.class);
                                int camNUm = door.cameraNumber;
                                int mode = Settings.getDoorMode(door.cameraNumber);
                                //30.01.2022
                                var mqttMessage = new MqttMessage(String.valueOf(mode).getBytes(StandardCharsets.UTF_8));
                                _mqttClient.publish("Parking/MonitorDoor/MODE)", mqttMessage);

                                //log.info("???????? ? ????? Parking/MonitorDoor/MODE ?????????");
                                //Получено сообщение! ТОПИК: Parking/MonitorDoor/update/mode СООБЩЕНИЕ:



                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/0/Door0" -> { // Тест для ручного (с телефона) открывания
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/0/Door0");
                                var door = mapper.readValue("{\"cameraNumber\" : \"2\"}", Door.class);
                                door.openRelay(0);
                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/0/Door1" -> { // Тест для ручного (с телефона) открывания
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/0/Door1");
                                var door = mapper.readValue("{\"cameraNumber\" : \"2\"}", Door.class);
                                door.openRelay(1);
                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/1/Door0" -> { // Тест для ручного (с телефона) открывания
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/1/Door0");
                                var door = mapper.readValue("{\"cameraNumber\" : \"4\"}", Door.class);
                                door.openRelay(0);
                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/1/Door1" -> { // Тест для ручного (с телефона) открывания
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/1/Door1");
                                var door = mapper.readValue("{\"cameraNumber\" : \"4\"}", Door.class);
                                door.openRelay(1);
                            } catch (Exception ex) {
                                log.error("Ошибка: " + ex);
                            }
                        }
                        case "Parking/MonitorDoor/gateway" -> { // Тест для ручного (с телефона) открывания
                            try {
                                log.info("Принят топик - Parking/MonitorDoor/gateway");
                                var door = mapper.readValue(json, Door.class);
                                door.gateway();
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
        }catch(Exception ex){
            log.error("Ошибка: " + ex);
        }
    }

    public void send(String topic, MqttMessage message){
        try {
            _mqttClient.publish(topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
