package service;

import models.Host;
import models.Message;
import models.Monitor;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class MonitorService {
    private static Dictionary<String, Monitor> monitors = new Hashtable<>();
    private Monitor monitor;

    public static String getMonitorKey(Monitor monitor){
        return monitor.getHost().getIp() + ":" + monitor.getHost().getPort();
    }

    public static void remove(Monitor monitor){
        var key = getMonitorKey(monitor);
        monitors.remove(key);
    }

    public MonitorService(Monitor monitor){
        var key = getMonitorKey(monitor);

        var currentMonitor = monitors.get(key);

        if(currentMonitor == null){
            monitors.put(key, monitor);
            this.monitor = monitor;
        }else{
            this.monitor = currentMonitor;
        }
    }

    public void view(){
        monitor.view();
    }

    public void viewAd(){
        monitor.viewAd();
    }

    public static void viewAllAd(){
        var map = new JsonService().getConfigParam().getAdMessages();
        for(Map.Entry<Integer, ArrayList<Message>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            //ArrayList<Message> value = entry.getValue();

            var monitor = new Monitor(key);

            //var monitor = new Monitor();
            //monitor.setHost(new Host("192.168.8.105", 1985));
            //monitor.setAd(new JsonService().getConfigParam().getAdMessages().get(3));


            var MonitorService = new MonitorService(monitor);
            MonitorService.viewAd();
        }
    }
}
