package quartz;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import service.MqttService;
import service.MqttServiceSingleton;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TopicSender implements Job {

    private static MqttService _mqttService = MqttServiceSingleton.getService();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ss");

        _mqttService.send("Parking/MonitorDoor/Im Here",
                new MqttMessage(("Я тут: " + formatter.format(date))
                        .getBytes(StandardCharsets.UTF_8)));
    }
}
