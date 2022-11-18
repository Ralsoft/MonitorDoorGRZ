package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Arrays;

public class EchoClient {
    private static final Logger LOG = LogManager.getLogger(EchoClient.class);
    private DatagramSocket socket;
    private InetAddress address;
    private int _port;
    private String _ip;

    public EchoClient(Host host) {
        try {
            _ip = host.ip;
            _port = host.port;
            LOG.info("Подключение к клиенту. HOST: " + _ip + ":" + _port);
            socket = new DatagramSocket();
            socket.setSoTimeout(3000);
            address = InetAddress.getByName(_ip);
        } catch (Exception ex) {
            LOG.error("Ошибка: " + ex.getMessage());
        }
    }

    public void sendEchoWithOutReceive(byte[] msg) {
        try {
            LOG.info("Попытка отправки сообщения. MESSAGE: " + Arrays.toString(msg) + " ADDRESS: " + address + " PORT: " + _port);
            DatagramPacket packet = new DatagramPacket(msg, msg.length, address, _port);
            socket.send(packet);
            LOG.info("Сообщение успешно отправлено. Сообщение: " + Arrays.toString(packet.getData()));
            Thread.sleep(50);
        } catch (Exception e) {
            LOG.error("Ошибка: " + e);
        }
    }

    //Функция рассчета контрольной суммы BCC
    byte BCCCalc(byte dataBCC[], int BCCSize) {  // Передаем в функцию  массив с данными и размер
        byte result = 0;
        for (int i = 0; i < BCCSize; i++) {
            result ^= dataBCC[i];
        }
        return result;
    }

    public void sendEchoWithoutReceive(String message, byte x, byte y, byte color) {
        try {
            LOG.info("Отправка сообщения на табло. MESSAGE: " + message + " X: " + x + " Y: " + y + " COLOR: " + color);

            byte[] textByte = message.getBytes(Charset.forName("windows-1251"));
            byte[] msg = new byte[6 + textByte.length];
            msg[0] = (byte) msg.length;
            msg[1] = 0x46;
            msg[2] = x;
            msg[3] = y;
            msg[4] = color;

            for (int i = 0; i < textByte.length; i++) {
                msg[i + 5] = textByte[i];
            }

            byte[] receive = new byte[10];
            DatagramPacket receives = new DatagramPacket(receive, receive.length);
            socket.receive(receives);
            String resultReceive = Arrays.toString(receives.getData());
            LOG.info("Получен ответ от контроллера." +
                    " Сообщение: " + resultReceive +
                    " ADDRESS: " + address +
                    " PORT: " + _port);

            byte bcc = BCCCalc(msg, msg.length);
            msg[5 + textByte.length] = bcc;
            var packet = new DatagramPacket(msg, msg.length, address, _port);
            socket.send(packet);
            Thread.sleep(50);
            LOG.info("Сообщение успешно отправлено. Сообщение: " + Arrays.toString(packet.getData()));

        } catch (Exception e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }

    public void close() {
        socket.close();
        LOG.info("Сокет закрыт. IP: " + _ip + " PORT: " + _port);
    }
}