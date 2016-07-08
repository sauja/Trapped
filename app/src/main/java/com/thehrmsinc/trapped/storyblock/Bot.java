package com.thehrmsinc.trapped.storyblock;


public class Bot {

    String text;
    long delay=0;
    boolean isDelayCompleted = false;

    public boolean isDelayCompleted() {
        return isDelayCompleted;
    }

    public void setDelayCompleted(boolean delayCompleted) {
        isDelayCompleted = delayCompleted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public Bot(String text, long delay) {
        super();
        this.text = text;
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "BOT: " + text + "\t\tDelay: " + delay;
    }
}