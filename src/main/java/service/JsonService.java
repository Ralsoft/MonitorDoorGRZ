package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.ConfigurationModelDoorAndMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonService {
    private static final Logger LOG = LogManager.getLogger(JsonService.class);

    public void isNewFile(File file) {
        try {
            if (file.createNewFile()) {
                LOG.info("Файл " + file.getName() + " успешно создан по пути: " + file.getAbsolutePath());

                FileOutputStream out = new FileOutputStream(file);
                out.write(new ConfigurationModelDoorAndMonitor().toString().getBytes());
                out.close();
            }
        } catch (IOException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }

    public ConfigurationModelDoorAndMonitor getConfigParam() {
        ConfigurationModelDoorAndMonitor model = null;
        try {
            model = new ConfigurationModelDoorAndMonitor();
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("MonitorDoorConfig.json");
            isNewFile(file);
            model = mapper.readValue(file, ConfigurationModelDoorAndMonitor.class);
        } catch (IOException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
        return model;
    }
}
