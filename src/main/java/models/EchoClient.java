package models;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;
    private int _port;
    private String _host;

    public EchoClient(String host, int port) {
        _host = host;
        _port = port;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName(_host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String sendEcho(byte[] msg) {
        DatagramPacket packet
                = new DatagramPacket(msg, msg.length, address, _port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(msg, msg.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    //Функция рассчета контрольной суммы BCC
    byte BCCCalc(byte dataBCC[], int BCCSize) {  // Передаем в функцию  массив с данными и размер
        byte result = 0;
        for(int i=0; i<BCCSize; i++) {
            result ^= dataBCC[i];
        }
        return result;
    }

    public String sendEcho(String message, byte X, byte Y, byte Color) {
        byte[] textByte = message.getBytes(Charset.forName("windows-1251"));
        byte[] msg = new byte[6 + textByte.length];
        msg[0] = (byte) msg.length;
        msg[1] = 0x46;
        msg[2] = X;
        msg[3] = Y;
        msg[4] = Color;

       for(int i = 0; i < textByte.length; i++){
           msg[i + 5] = textByte[i];
       }

       byte bcc = BCCCalc(msg, msg.length);
       msg[5 + textByte.length] = bcc;

        var packet  = new DatagramPacket(msg, msg.length, address, _port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(msg, msg.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}