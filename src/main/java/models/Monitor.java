package models;

import org.springframework.messaging.Message;

import java.util.List;

public class Monitor {
    public String camNumber;
    public String ip;
    public int port;
    public List<Message> messages;

    public String getCamNumber() {
        return camNumber;
    }

    public void setCamNumber(String camNumber) {
        this.camNumber = camNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Monitor() {
    }

    public Monitor(String camNumber, String ip, int port, List<Message> messages) {
        this.camNumber = camNumber;
        this.ip = ip;
        this.port = port;
        this.messages = messages;
    }
}
