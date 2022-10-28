package models;

public class Message {
    public byte X;
    public byte Y;
    public byte Color;
    public String Text;

    public byte getX() {
        return X;
    }

    public void setX(byte x) {
        X = x;
    }

    public byte getY() {
        return Y;
    }

    public void setY(byte y) {
        Y = y;
    }

    public byte getColor() {
        return Color;
    }

    public void setColor(byte color) {
        Color = color;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Message() {
    }

    public Message(byte x, byte y, byte color, String text) {
        X = x;
        Y = y;
        Color = color;
        Text = text;
    }
}
