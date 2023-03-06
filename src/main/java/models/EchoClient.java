package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
            LOG.error("EchoClient Ошибка: " + ex);
        }
    }

    public void sendEchoWithOutReceive(byte[] msg) {
        try {
            LOG.info("Попытка отправки сообщения. MESSAGE: " + Arrays.toString(msg) + " ADDRESS: " + address + " PORT: " + _port);

            try{
                LOG.info("Отправка пакета -> " + Arrays.toString(msg));
                DatagramPacket packetSend = new DatagramPacket(msg, msg.length, address, _port);
                socket.send(packetSend);
            }catch (Exception ex){
                LOG.error(ex.getMessage());
            }

            try {
                byte[] receiveByte = new byte[10];
                DatagramPacket packetReceive = new DatagramPacket(receiveByte, receiveByte.length, address, _port);
                socket.receive(packetReceive);
                LOG.info("Получен ответ. -> " + Arrays.toString(receiveByte));

                if(receiveByte[3] == 0x00){
                    LOG.info("OK");
                } else {
                    LOG.info("ERR. Получен код ошибки "+ receiveByte[3]);
                }

            }catch (Exception ex){
                LOG.info("Ответ контроллера не получен.");
            }

        } catch (Exception e) {
            LOG.error("sendEchoWithOutReceive Ошибка: " + e);
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
            LOG.info("78 Отправка сообщения на табло. MESSAGE: " + message + " X: " + x + " Y: " + y + " COLOR: " + color);
            //message = "ПРОБА";

            byte[] textByte = message.getBytes(Charset.forName("windows-1251"));

            LOG.info("82 " + Arrays.toString(textByte));
            LOG.info("83 " + message);

            byte[] msg = new byte[6 + textByte.length];
            msg[0] = (byte) msg.length;


            if(textByte.length>9) {
                msg[1] = 0x4A;
            } else {
                msg[1] = 0x46;
            }

            msg[2] = x;
            msg[3] = y;
            msg[4] = color;

            for (int i = 0; i < textByte.length; i++) {
                msg[i + 5] = textByte[i];
            }

            try{
                byte bcc = BCCCalc(msg, msg.length);
                msg[5 + textByte.length] = bcc;
                LOG.info("Отправка пакета -> " + Arrays.toString(msg));
                var packet = new DatagramPacket(msg, msg.length, address, _port);
                socket.send(packet);

                byte[] receive = new byte[10];
                DatagramPacket packetReceive = new DatagramPacket(receive, receive.length, address, _port);
                socket.receive(packetReceive);
                LOG.info("Получен ответ от контроллера." +
                        " Сообщение: " + convertByteToHex(receive) +
                        " ADDRESS: " + address +
                        " PORT: " + _port);

                Thread.sleep(50);
                LOG.info("Сообщение успешно отправлено. Сообщение: " + message);
            }catch (Exception ex){
                LOG.error("Ошибка: " + ex.getMessage());
            }
        } catch (Exception e) {
            LOG.error("sendEchoWithoutReceive Ошибка: " + e.getMessage());
        }
    }

    public void close() {

        socket.close();

        LOG.info("Сокет закрыт. IP: " + _ip + " PORT: " + _port);
    }

    String convertByteToHex(byte[] text){

        StringBuilder sb = new StringBuilder();
        for (byte b : text) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}