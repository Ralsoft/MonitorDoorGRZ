package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.JsonService;
import service.MonitorService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Data
public class Monitor {
    private static final Logger LOG = LogManager.getLogger(Monitor.class);

    public int camNumber;
    public List<Message> messages;


    @JsonIgnore
    private Host host;
    @JsonIgnore
    private Timer timer;

    public Monitor(){

    }

    public void init(){
        this.host = Settings.getHostMonitor(camNumber);
    }

    public Monitor(String ip, int port) {
        this.host = new Host(ip, port);
        this.messages = new ArrayList<>();
        timer = new Timer();
    }

    public Monitor(String ip, int port, List<Message> messages) {
        this.host = new Host(ip, port);
        this.messages = messages;
        timer = new Timer();
    }

    public Monitor(int camNumber) {
        this.host = Settings.getHostMonitor(camNumber);
        this.messages = new ArrayList<>();
        timer = new Timer();
    }

    public Monitor(int camNumber, List<Message> messages) {
        this.host = Settings.getHostMonitor(camNumber);
        this.messages = messages;
        timer = new Timer();
    }


    private void viewMessages(List<Message> messages, boolean clear){
        try {

            var echoClient = new EchoClient(host);

            //clear
            if(clear)
                echoClient.sendEchoWithOutReceive(new byte[] {0x03, 0x44, 0x47});

            for (var item : messages) {
                echoClient.sendEchoWithoutReceive(item.text, item.x, item.y, item.color);
            }

            echoClient.close();

        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex.getMessage());
        }
    }

    public void viewGRZ() {
        viewMessages(messages, true);
    }

    public void viewDateTime(String message) throws InterruptedException {
        new ViewDateTimeThread(message).start();
    }

    private class ViewDateTimeThread extends Thread{

        private String ad;

        public ViewDateTimeThread(String ad){
            this.ad = ad;
        }

        @Override
        public void run() {
            SimpleDateFormat format = new SimpleDateFormat("mm:ss");
            while(true)
            {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                var date = new Date();
                var dateString = format.format(date);
                var messages = new ArrayList<Message>();

                messages.add(new Message((byte) 8, (byte) 0, (byte) 2, dateString));
                messages.add(new Message((byte) 0, (byte) 0, (byte) 2, ad));
                viewMessages(messages, true);
            }
        }
    }

    public void viewAd(ArrayList<Message> ad){
        viewMessages(ad, true);
        MonitorService.remove(this);
    }

    public void view(){

        viewGRZ();

        timer.cancel();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewAd(new JsonService().getConfigParam().getAdMessages());
            }
        }, 20 * 1000);
    }
}
