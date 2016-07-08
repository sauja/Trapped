package com.thehrmsinc.trapped.Database;

/**
 * Created by Acer on 5/18/2016.
 */
public class Messages {

    public int id;
    public int type;
    public String chatText;
    public String chatSource;

    public Messages(int type, String chatText, String chatSource) {
        super();
        this.type = type;
        this.chatText = chatText;
        this.chatSource=chatSource;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public String getChatSource() { return chatSource;}

    public void setChatSource(String chatSource) {this.chatSource = chatSource;}
    public String toString()
    {
        return "ChatSource: "+chatSource+" ::ChatText: "+chatText+" Type: "+type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}