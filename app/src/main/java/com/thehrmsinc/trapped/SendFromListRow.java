package com.thehrmsinc.trapped;

/**
 * Created by Acer on 5/18/2016.
 */
public class SendFromListRow {


    public int type;
    public String text;

    public SendFromListRow(int type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}