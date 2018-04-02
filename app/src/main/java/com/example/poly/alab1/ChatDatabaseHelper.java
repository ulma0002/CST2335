package com.example.poly.alab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by GalUlma on 2018-02-23.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static       String DATABASE_NAME = "Messages.db";
    public static       String TABLE_NAME    = "CHAT_HISTORY";
   // public final static String ID            = "id";
    public static final String KEY_ID        = "ID";           // Column I (Primary Key)
    public static final String KEY_MESSAGE   = "MESSAGE";    // Column II
    public static final String CREATE_TABLE  = "CREATE TABLE " + TABLE_NAME +            //query of creating table
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);";
    public static final String DROP_TABLE    = "DROP TABLE IF EXISTS " + TABLE_NAME;   // query of dropping table
    public static       int    VERSION_NUM   = 9;
    Context ctx;


    //constructor that opens a database file “Messages.db”
    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //this function executes a string SQL statement.
            db.execSQL(CREATE_TABLE);
            Log.i("ChatDataBaseHelper", "Calling OnCreate");
        } catch (Exception e) {
            message(ctx, "Create table was unsuccessful" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        try {
            db.execSQL(DROP_TABLE);
            Log.i("ChatDataBaseHelper", "Calling OnUpdate, oldVersion= " + oldVer + " newVersion= " + newVer);
            onCreate(db);
        } catch (Exception e) {
            message(ctx, "Drop table was unsuccessful" + e);

        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

    public static void message(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();

    }
}
