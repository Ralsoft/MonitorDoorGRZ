package service;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import models.ConfigurationModelDoorAndMonitor;
import models.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static org.quartz.impl.StdSchedulerFactory.PROPERTIES_FILE;

public class JsonService {
    private static final Logger LOG = LogManager.getLogger(JsonService.class);
    private static final String PATH = "MonitorDoorConfig.json";

    public void isNewFile(File file) {
        if (!file.exists()) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(PATH), StandardCharsets.UTF_8));
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(new ConfigurationModelDoorAndMonitor());

                out.write(json);
                out.close();
            }
            catch(IOException e) {
                System.err.println("Something went wrong.");
            }

            LOG.info("Файл конфигурации успешно создан. " +
                    "Запустите программу заново. Путь до файла: " +
                    file.getAbsolutePath());
            System.exit(0);
        }
    }

    public void save(ConfigurationModelDoorAndMonitor model) {
        try {
            var file = new File(PATH);

            var out = new FileOutputStream(file);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String json = ow.writeValueAsString(model);

            out.write(json.getBytes());
            out.close();

        } catch (IOException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }


    public ConfigurationModelDoorAndMonitor getConfigParam() {
        ConfigurationModelDoorAndMonitor model = null;
        try {
            model = new ConfigurationModelDoorAndMonitor();
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(PATH);
            isNewFile(file);
            model = mapper.readValue(file, ConfigurationModelDoorAndMonitor.class);
        } catch (IOException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
        return model;
    }

    public void setMainText(Map<Integer, ArrayList<Message>> ad){

        try {
            ObjectMapper mapper = new ObjectMapper();

            File file = new File(PATH);
            isNewFile(file);
            var model =
                    mapper.readValue(file, ConfigurationModelDoorAndMonitor.class);

            model.setAdMessages(ad);
            save(model);
        } catch (IOException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }
}
