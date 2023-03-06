package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MqttServiceSingleton {
    private static MqttService _service;
    private static final Logger LOG = LogManager.getLogger(MqttServiceSingleton.class);
    static JsonService settings = new JsonService();

    public static MqttService getService(){
        if(_service == null) {
            _service = new MqttService(
                    settings.getConfigParam().getIpClient(),
                    settings.getConfigParam().getPortClient());
        }
        return _service;
    }
}
