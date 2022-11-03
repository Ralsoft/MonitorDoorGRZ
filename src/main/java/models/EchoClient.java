package models;

import com.sun.tools.javac.Main;
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
            LOG.info("����������� � �������. HOST: " + _ip + ":" + _port);
            socket = new DatagramSocket();
            address = InetAddress.getByName(_ip);
        } catch (Exception ex) {
            LOG.error("������: " + ex.getMessage());
        }
    }

    public void sendEchoWithOutReceive(byte[] msg) {
        try {
            LOG.info("������� �������� ���������. MESSAGE: " + Arrays.toString(msg) + " ADDRESS: " + address + " PORT: " + _port);
            DatagramPacket packet = new DatagramPacket(msg, msg.length, address, _port);
            socket.send(packet);
            LOG.info("��������� ������� ����������.");
        } catch (Exception e) {
            LOG.error("������: " + e);
        }
    }

    //������� �������� ����������� ����� BCC
    byte BCCCalc(byte dataBCC[], int BCCSize) {  // �������� � �������  ������ � ������� � ������
        byte result = 0;
        for (int i = 0; i < BCCSize; i++) {
            result ^= dataBCC[i];
        }
        return result;
    }

    public void sendEchoWithoutReceive(String message, byte x, byte y, byte color) {
        try {
            LOG.info("�������� ��������� �� �����. MESSAGE: " + message + " X: " + x + " Y: " + y + " COLOR: " + color);

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

            byte bcc = BCCCalc(msg, msg.length);
            msg[5 + textByte.length] = bcc;
            var packet = new DatagramPacket(msg, msg.length, address, _port);

            socket.send(packet);

            LOG.info("��������� ������� ����������.");
        } catch (Exception e) {
            LOG.error("������: " + e.getMessage());
        }
    }

    public void close() {
        socket.close();
        LOG.info("����� ������. IP: " + _ip + " PORT: " + _port);
    }


//    public String sendEcho(String message, byte X, byte Y, byte Color) {
//        byte[] textByte = message.getBytes(Charset.forName("windows-1251"));
//        byte[] msg = new byte[6 + textByte.length];
//        msg[0] = (byte) msg.length;
//        msg[1] = 0x46;
//        msg[2] = X;
//        msg[3] = Y;
//        msg[4] = Color;
//
//        for (int i = 0; i < textByte.length; i++) {
//            msg[i + 5] = textByte[i];
//        }
//
//        byte bcc = BCCCalc(msg, msg.length);
//        msg[5 + textByte.length] = bcc;
//
//        var packet = new DatagramPacket(msg, msg.length, address, _port);
//        try {
//            socket.send(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        packet = new DatagramPacket(msg, msg.length);
//        try {
//            socket.receive(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String received = new String(
//                packet.getData(), 0, packet.getLength());
//        return received;
//    }

//    public String sendEcho(byte[] msg) {
//        DatagramPacket packet
//                = new DatagramPacket(msg, msg.length, address, _port);
//        try {
//            socket.send(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        packet = new DatagramPacket(msg, msg.length);
//        try {
//            socket.receive(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String received = new String(
//                packet.getData(), 0, packet.getLength());
//        return received;
//    }
}