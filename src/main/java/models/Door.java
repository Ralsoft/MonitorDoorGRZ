package models;

public class Door {
    private EchoClient _client;

    public Door(String host, String port){
        _client = new EchoClient(host, Integer.getInteger(port));
    }

    public String openDoor(){
        byte[] openDoorMessage = {0x04, 0x4F, 0x01, 0x4A};
        return _client.sendEcho(openDoorMessage);
    }
}
