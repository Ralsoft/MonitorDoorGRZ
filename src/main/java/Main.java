import db.ParamRepository;
import models.Monitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import quartz.TopicSender;
import service.JsonService;
import service.MqttService;


public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    static JsonService settings = new JsonService();

    private String MessageAd = "Это реклама";

    public static void main(String[] args) {

        try {
            someThinkTask();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        ViewAd();
        LOG.info("Запуск программы.");

    }

    public static void ViewAd(){
        var monitor_0 = new Monitor("192.168.8.105", 1985);
        monitor_0.viewAd(settings.getConfigParam().getAdMessages());
    }

    public static void someThinkTask() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        scheduler.start();

        JobDetail job = JobBuilder.newJob(TopicSender.class)
                .withIdentity("topic_sender", "group1")
                .usingJobData("key", "value")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("topic_trigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(settings.getConfigParam().getIntervalInSeconds())
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);

    }
}
