package com.thehrmsinc.trapped;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.thehrmsinc.trapped.Database.Messages;
import com.thehrmsinc.trapped.notification.NotificationHelper;
import com.thehrmsinc.trapped.storyblock.Bot;
import com.thehrmsinc.trapped.storyblock.QuestionBlock;
import com.thehrmsinc.trapped.storyblock.StoryBlock;

import java.util.ArrayList;
import java.util.Arrays;

public class MainPage extends AppCompatActivity {
    private static final String TAG = MainPage.class.getSimpleName();
    final Context context = this;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private static long storyReadPointer;
    static ListView listView;
    static boolean storyEnd = false;
    Button btn[] = new Button[3];
    NotificationHelper notiHelper = new NotificationHelper(context);
    int clickedButton;
    static StoryBlock block = new StoryBlock();
    static SendFromListCustomAdaptor customAdapter;
    static ArrayList<Messages> items = null;
    String userName = null;
    View mDecorView;
    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "in MainPage onCreate");
        mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main_page);

        btn[0] = (Button) findViewById(R.id.btn_question1);
        btn[1] = (Button) findViewById(R.id.btn_question2);
        btn[2] = (Button) findViewById(R.id.btn_question3);


        settings = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (editor == null)
            editor = settings.edit();


        items = new ArrayList<Messages>();
        if (settings.contains("itemList")) {
            String jsonItems = settings.getString("itemList", null);
            Gson gson = new Gson();
            Messages[] favoriteItems = gson.fromJson(jsonItems, Messages[].class);
            items = new ArrayList<Messages>(Arrays.asList(favoriteItems));
            /*for (SendFromListRow item : items) {
               // Log.e(TAG, item.toString());
            }*/
        }
        if (settings.contains("block")) {
            String jsonItems = settings.getString("block", null);
            Gson gson = new Gson();
            block = gson.fromJson(jsonItems, StoryBlock.class);

        }
        if (settings.contains("button1")) {
            if (settings.getString("button1", null) != "") {
                btn[0].setText(settings.getString("button1", null));
                btn[0].setVisibility(Button.VISIBLE);
            }
        }
        if (settings.contains("button2")) {
            if (settings.getString("button2", null) != "") {
                btn[1].setText(settings.getString("button2", null));
                btn[1].setVisibility(Button.VISIBLE);
            }
        }
        if (settings.contains("button3")) {
            if (settings.getString("button3", null) != "") {
                btn[2].setText(settings.getString("button3", null));
                btn[2].setVisibility(Button.VISIBLE);
            }
        }
        listView = (ListView) findViewById(R.id.listView);
        customAdapter = new SendFromListCustomAdaptor(this, R.id.rowTextView, items);
        hideSystemUI();
        listView.setAdapter(customAdapter);
        if (items.size() == 0) {
            if (settings.contains("userName")) {
                userName = settings.getString("userName", null);
            } else {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.activity_user_name_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editText_userName);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        hideSystemUI();
                                        userName = userInput.getText().toString();
                                        //Log.e(TAG, "UserName: " + userName);
                                        editor.putBoolean("notification",false);
                                        editor.apply();
                                        parseFile("1");


                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                hideSystemUI();
            }
        }
        onNewIntent(getIntent());
        hideSystemUI();
        if (settings.contains("clickedButton")) {
            clickedButton = settings.getInt("clickedButton", -1);
            resumeBlock(clickedButton);
        }
    }

    public void onButtonClick(View v) {
        clickedButton = -1;
        btn[0].setClickable(false);
        btn[1].setClickable(false);
        btn[2].setClickable(false);
        switch (v.getId()) {
            case R.id.btn_question1:
                clickedButton = 0;

                btn[1].setVisibility(Button.GONE);
                btn[1].setText("");
                btn[2].setVisibility(Button.GONE);
                btn[2].setText("");
                break;
            case R.id.btn_question2:
                clickedButton = 1;
                btn[0].setVisibility(Button.GONE);
                btn[2].setVisibility(Button.GONE);
                btn[0].setText("");
                btn[2].setText("");
                break;
            case R.id.btn_question3:
                clickedButton = 2;
                btn[0].setVisibility(Button.GONE);
                btn[1].setVisibility(Button.GONE);
                btn[0].setText("");
                btn[1].setText("");
                break;
        }

        //btn[clickedButton].setBackgroundColor(0xFFFF5722);
        String ques = btn[clickedButton].getText().toString();
        if (ques.startsWith("Incomming message from an unknown number"))
            ques = "Incomming message accepted...";
        items.add(new Messages(SendFromListCustomAdaptor.TYPE_SEND, ques, "You"));
        customAdapter.notifyDataSetChanged();
        //Log.e(TAG,block.toString());
        Bot options[] = block.getOptions().get(clickedButton).getBot().toArray(new Bot[block.getOptions().get(clickedButton).getBot().size()]);
        int optionCount = 0;
        boolean breakFlag = false;
        for (int i = 0; i < options.length; i++) {
            if (options[i].getDelay() > 0 && !options[i].isDelayCompleted()) {
                notiHelper.scheduleNotification(notiHelper.getNotification("Amena is waiting for you"), options[i].getDelay() * 60 * 1000, clickedButton);
                if(settings.contains("notification")) {
                    if(settings.getBoolean("notification",false)==false){
                        editor.putString("block", new Gson().toJson(block));
                        Log.e(TAG,"onNoti: block inserted");
                    }
                }
                //Log.e(TAG, "Delay: " + options[i].getDelay());
                breakFlag = true;
                break;
            }
            if(options[i].getText().contains("USERNAME"))
                new LongOperation(getRemovedText(options[i].getText().replace("USERNAME",userName)), getString(R.string.protagonist_name), false,clickedButton).execute("");
            else
                new LongOperation(getRemovedText(options[i].getText()), getString(R.string.protagonist_name), false,clickedButton).execute("");


        }
        if (!breakFlag)
            new LongOperation("", "", true, clickedButton).execute("");

    }


    private class LongOperation extends AsyncTask<String, Void, String> {
        boolean isLast;
        String chatSource;
        String chatText;
        int clickedButton;

        LongOperation(String chatText, String chatSource, boolean isLast, int clickedButton) {
            this.chatSource = chatSource;
            this.chatText = chatText;
            this.isLast = isLast;
            this.clickedButton=clickedButton;
        }

        @Override
        protected String doInBackground(String... params) {
            int rand;
            try {
                if (chatText.length() > 80) {
                    rand = 5000;
                } else
                    rand = 3000;
                Thread.sleep(rand);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            if (!isLast) {
                items.add(new Messages(SendFromListCustomAdaptor.TYPE_FROM, chatText, chatSource));
                Log.e(TAG, "Items added in LongOperation: " + chatText);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            customAdapter.notifyDataSetChanged();
            if (isLast) {
                Log.e(TAG,"Parse next block:" +block.getQuestions().get(clickedButton).getNextBlock());
                //btn[clickedButton].setBackgroundColor(0xFFEFEBE9);
                parseFile(block.getQuestions().get(clickedButton).getNextBlock());
                clickedButton = -1;
                if (settings.contains("block")) {
                    editor.remove("block");
                    editor.putBoolean("notification",false);
                    editor.apply();
                }
            }
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }


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


    public void parseFile(String nextblock) {
        hideSystemUI();
        if(!block.getBlockName().equals("end")) {
            block = StoryBlock.getNextBlock(nextblock, context);

            QuestionBlock[] questions = block.getQuestions().toArray(new QuestionBlock[block.getQuestions().size()]);
            Log.e(TAG, "Question length " + questions.length);
            Log.e(TAG, "Block " + block.toString());
            for (int i = 0; i < questions.length; i++) {
                btn[i].setText(questions[i].getQuestion());
                Log.e(TAG, "Question  " + " i " + questions.length);
            }
            for (int i = 0; i < questions.length; i++) {
                btn[i].setVisibility(Button.VISIBLE);
                btn[i].setClickable(true);
            }
            for (int i = questions.length; i < 3; i++) {
                btn[i].setVisibility(Button.GONE);
            }
        }

    }

    public String getRemovedText(String text) {
        if (text.startsWith("UnknownNumber:"))
            text = text.replace("UnknownNumber: ", "");
        //text = text.replaceAll("USERNAME", userName);
        return text;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop");
        settings=this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString("itemList", new Gson().toJson(items));
        if(settings.contains("notification")) {
            if(settings.getBoolean("notification",false)==false){
                editor.putString("block", new Gson().toJson(block));
            Log.e(TAG,"onStop: block inserted");
            }
        }
        editor.putInt("clickedButton", clickedButton);
        editor.putString("button1", btn[0].getText().toString());
        editor.putString("button2", btn[1].getText().toString());
        editor.putString("button3", btn[2].getText().toString());
        editor.apply();
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(uiOptions);
    }





    public void resumeBlock(int clickedButton) {
        btn[0].setClickable(false);
        btn[1].setClickable(false);
        btn[2].setClickable(false);
        Log.e(TAG, "Inside Resume block");
        if (clickedButton != -1) {
            btn[clickedButton].setVisibility(Button.VISIBLE);
           // btn[clickedButton].getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
            Messages row = items.get(items.size() - 1);
            Log.e(TAG, row.getChatText());
            if (settings.contains("block")) {
                String jsonItems = settings.getString("block", null);
                Gson gson = new Gson();
                block = gson.fromJson(jsonItems, StoryBlock.class);
            }
            Log.e(TAG,"Block: "+block.toString());
            Bot options[] = block.getOptions().get(clickedButton).getBot().toArray(new Bot[block.getOptions().get(clickedButton).getBot().size()]);
            boolean breakFlag = false;
            int j = 0;
            for (Bot b : options) {
                Log.e(TAG, b.getText() + "\n+++++++++++++++" + row.getChatText());
                if (removeSpaces(row.getChatText())==removeSpaces(b.getText())) {
                    Log.e(TAG, "Resume break successful");
                    break;
                }
                j++;
            }

            for (int i = j+1; i < options.length; i++) {

                if (options[i].getDelay() > 0 && !options[i].isDelayCompleted()) {
                    notiHelper.scheduleNotification(notiHelper.getNotification("Amena is waiting for you"), options[i].getDelay() * 60 * 1000, clickedButton);
                    if(settings.contains("notification")) {
                        if(settings.getBoolean("notification",false)==false){
                            editor.putString("block", new Gson().toJson(block));
                            Log.e(TAG,"onNoti: block inserted");
                        }
                    }
                    breakFlag = true;
                    break;
                }
                Log.e(TAG, "In Rusume: " + options[i].getText());
                new LongOperation(getRemovedText(options[i].getText()), getString(R.string.protagonist_name), false, clickedButton).execute("");


            }
            if (!breakFlag)
                new LongOperation("", "", true, clickedButton).execute("");
        }
        else
        {
            if(!(btn[0].getText()==""))
                btn[0].setClickable(true);
            if(!(btn[1].getText()==""))
                btn[1].setClickable(true);
            if(!(btn[2].getText()==""))
                btn[2].setClickable(true);
        }
    }

    public String removeSpaces(String str) {
        String text = "";
        for (char ch : str.toCharArray()) {
            if (Character.isLetterOrDigit(ch))
                text += text;
        }
        return text;
    }
}
