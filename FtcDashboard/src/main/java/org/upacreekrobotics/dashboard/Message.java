package org.upacreekrobotics.dashboard;

public class Message{

    private MessageType type;
    private String text;

    public Message(MessageType type, String message){
        text = message;
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getMessage(){
        return (type.getMessage(type)+"~"+text);
    }
}