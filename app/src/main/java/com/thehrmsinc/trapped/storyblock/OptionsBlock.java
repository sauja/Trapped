package com.thehrmsinc.trapped.storyblock;

import java.util.ArrayList;

/**
 * Created by Acer on 6/1/2016.
 */

public class OptionsBlock {
    ArrayList<Bot> bot;
    @Override
    public String toString() {
        String line="";
        for(Bot b:bot)
        {
            line+=b.toString()+"\n";
        }
        return line;
    }
    public ArrayList<Bot> getBot() {
        return bot;
    }
    public void setBot(ArrayList<Bot> bot) {
        this.bot = bot;
    }
    public void addBot(Bot b)
    {
        bot.add(b);
    }
    public OptionsBlock(ArrayList<Bot> bot) {
        super();
        this.bot = bot;
    }

}

