import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;
import service.MqttService;


public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    static JsonService service = new JsonService();

    static MqttService mqttService = new MqttService();
    public static void main(String[] args) {
        LOG.info("Запуск программы.");
        try {
            mqttService.connectedMqtt(
                    service.getConfigParam().getIpClient(),
                    service.getConfigParam().getPortClient()
            );
            startBackgroundMethods();
        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex);
        }
    }

    public static void startBackgroundMethods(){
        new Thread(()->{ // проверка подключения к mqtt
            while (true){
                try {
                    LOG.info("подключение к mqtt: " +(
                            mqttService.getMqttClient().isConnected() ? "присутствует" : "отсутствует"));
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

/*
        new Thread(()->{ // вывод времени на монитор
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");

            while (true){
                var messages = new ArrayList<Messages>();
                messages.add( new Messages((byte) 9,(byte) 15,(byte) 1, dateFormat.format(new Date())));
                var monitor = new Monitor(1, messages);
                monitor.sendMessages();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();*/
    }
}
