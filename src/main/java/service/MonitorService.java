package service;

import models.Host;
import models.Monitor;

import java.util.Dictionary;
import java.util.Hashtable;

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
}
