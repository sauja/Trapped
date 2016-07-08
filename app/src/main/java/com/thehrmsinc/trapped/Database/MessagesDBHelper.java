package com.thehrmsinc.trapped.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Acer on 6/15/2016.
 */

public class MessagesDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "trapped";
    private static final String TABLE_MESSAGES = "messages";
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String CHAT_SOURCE = "chatSource";
    private static final String CHAT_TEXT = "chatText";

    public MessagesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + " ("
                + ID + "INTEGER NOT NULL,"
                + TYPE + "INTEGER NOT NULL,"
                + CHAT_TEXT + "TEXT,"
                + CHAT_SOURCE + "TEXT, PRIMARY KEY(ID)"
                + ");";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        // Creating tables again
        onCreate(db);
    }
    public void addMessage(Messages message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TYPE, getShopsCount()+1);
        values.put(TYPE, message.getType());
        values.put(CHAT_SOURCE, message.getChatSource());
        values.put(CHAT_TEXT, message.getChatText());
        // Inserting Row
        db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection
    }
    public int getShopsCount() {
        String countQuery = "select max("+ID+") from  " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

}
