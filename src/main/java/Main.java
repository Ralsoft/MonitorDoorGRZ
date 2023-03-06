import db.ParamRepository;
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

    public static void main(String[] args) {

        try {
            someThinkTask();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        ParamRepository paramRepository = new ParamRepository();
        var i = paramRepository.getIpMonitor(2);

        LOG.info("Запуск программы.");

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
