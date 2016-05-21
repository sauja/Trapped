package com.thehrmsinc.trapped;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Acer on 5/21/2016.
 */

public class ParseStoryBlock {
    private static final String TAG=ParseStoryBlock.class.getSimpleName();
    List<String> questions;
    List<String> botReplys;

    ParseStoryBlock()
    {
        questions=new ArrayList<String>();
        botReplys=new ArrayList<String>();
    }
    void clear()
    {
        questions=new ArrayList<String>();
        botReplys=new ArrayList<String>();
    }
    public static ParseStoryBlock parseStory(long storyReadPointer,BufferedReader storyReader)
    {
        Log.e(TAG,storyReader.toString());
        ParseStoryBlock psb=new ParseStoryBlock();
        String line=null;
        boolean startOption=false;
        try{

            while((line=storyReader.readLine())!= null)
            {
                if (line.startsWith("QUESTION:"))
                {
                    line=line.replace("QUESTION: ","");
                    psb.questions.add(line);
                    Log.e(TAG,"Add Question "+line);
                }
                if(line.startsWith("OPTIONBEGIN:"))
                {
                    startOption=true;
                }
                if(startOption==true&& (line.startsWith("OPTION:")||line.startsWith("AMENA:")))
                {
                    psb.botReplys.add(line);
                    Log.e(TAG,"Add options "+line);
                }
                if(line.startsWith("OPTIONEND:"))
                {
                    break;
                }



            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return psb;
    }
}
