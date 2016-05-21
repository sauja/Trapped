package com.thehrmsinc.trapped;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {
    private static final String TAG=MainPage.class.getSimpleName();
    private static long storyReadPointer;
    static ListView listView;

    Button btn[]=new Button[3];
    private ArrayAdapter<String> listAdapter ;
    BufferedReader storyReader;
    File storyFile;
    static ParseStoryBlock psb;
    static SendFromListCustomAdaptor customAdapter;
    static ArrayList<SendFromListRow> items=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_main_page);
        storyReadPointer=getIntent().getExtras().getLong("storyReadPointer");
        Log.e(TAG,"Pointer: "+storyReadPointer);
        try{

            storyReader =new BufferedReader(new InputStreamReader(getAssets().open("story.txt")));
        }
       catch (IOException e)
       {
           Log.e(TAG,"Cannot Open file");
       }
        btn[0]=(Button)findViewById(R.id.btn_question1);
        btn[1]=(Button)findViewById(R.id.btn_question2);
        btn[2]=(Button)findViewById(R.id.btn_question3);

        listView=(ListView) findViewById(R.id.listView);
        items = new ArrayList<SendFromListRow>();

        if(items.size()==0)
        {
            try{

                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,storyReader.readLine() ));
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,storyReader.readLine() ));
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,storyReader.readLine() ));
                parseFile();
                customAdapter = new SendFromListCustomAdaptor(this, R.id.rowTextView, items);
                listView.setAdapter(customAdapter);


            }
            catch (IOException e)
            {
                Log.e(TAG,"Cannot Open file");
            }
        }

        btn[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_SEND, btn[0].getText().toString()));
                customAdapter.notifyDataSetChanged();
                String options[]=psb.botReplys.toArray(new String[psb.botReplys.size()]);
                int optionCount=0;
                for(int i=0;i<options.length;i++)
                {
                    if(options[i].startsWith("OPTION:")) {
                        optionCount++;
                        i++;
                    }
                    if(optionCount==1)
                    {
                        items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,options[i]));

                    }
                }

customAdapter.refresh(items);
                parseFile();
            }
        });
        btn[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_SEND, btn[0].getText().toString()));
                String options[]=psb.botReplys.toArray(new String[psb.botReplys.size()]);
                int optionCount=0;
                for(int i=0;i<options.length;i++)
                {
                    if(options[i].startsWith("OPTION:")) {
                        optionCount++;
                        i++;
                    }
                    if(optionCount==2)
                    {
                        items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,options[i]));
                    }
                }

                parseFile();
            }
        });
        btn[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_SEND, btn[0].getText().toString()));
                String options[]=psb.botReplys.toArray(new String[psb.botReplys.size()]);
                int optionCount=0;
                for(int i=0;i<options.length;i++)
                {
                    if(options[i].startsWith("OPTION:")) {
                        optionCount++;
                        i++;
                    }
                    if(optionCount==3)
                    {
                        items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,options[i]));
                    }
                }

                parseFile();
            }
        });
       /* for (int i = 0; i < 40; i++) {
            if (i %2==0) {
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_FROM,"From " + i ));
            } else {
                items.add(new SendFromListRow(SendFromListCustomAdaptor.TYPE_SEND, "Send  " + i));
            }
        }*/






    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public void parseFile()
    {
        psb=ParseStoryBlock.parseStory(storyReadPointer,storyReader);
        String questions[]=psb.questions.toArray(new String[psb.questions.size()]);
        String options[]=psb.botReplys.toArray(new String[psb.botReplys.size()]);
        Log.e(TAG,"Question length "+questions.length);
        for(int i=0;i<questions.length;i++)
        {

            btn[i].setText(questions[i]);
        }
        if(questions.length==2)
        {
            btn[2].setVisibility(Button.GONE);
        }
        else
            btn[2].setVisibility(Button.VISIBLE);
    }

}
