package models;

import lombok.Data;

@Data
public class Messages {
    public byte x;
    public byte y;
    public byte color;
    public String text;

    public Messages() {
    }

    public Messages(byte x, byte y, byte color, String text) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.text = text;
    }

}
