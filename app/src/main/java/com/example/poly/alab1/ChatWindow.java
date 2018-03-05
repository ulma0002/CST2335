package com.example.poly.alab1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    ArrayList<String> msgChat_list = new ArrayList<>();
    ListView       listView;
    Button         buttonSend;
    EditText       chatText;
    ChatAdapter    messageAdapter;
    Cursor         cursor;
    SQLiteDatabase db;


    public class ChatAdapter extends ArrayAdapter<String> {
        //constructor for ChatAdapter that takes a Context parameter,
        // and passes it to the parent constructor (ArrayAdapter)
        ChatAdapter(Context ctx)

        {
            super(ctx, 0);
        }

        @Override
        public int getCount()
        //returns the number of rows that
        // will be in the listView,the number of strings in the array list
        {
            return msgChat_list.size();
        }

        @Override
        public String getItem(int position)
        //returns the item to show in the list at the specified position
        {
            return msgChat_list.get(position);
        }

        @Override
        public long getItemId(int position) {

            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndex("id"));
            //return position;
        }

        @Override
        public View getView(int position, View result, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            //View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_outgoing, null);//set layout for displaying items
            else
                result = inflater.inflate(R.layout.chat_row_ingoing, null);

            TextView message_pic = result.findViewById(R.id.message_text);//get id for  view
            message_pic.setText(getItem(position)); // get the string at position
            return result;//this returns the layout that will be positioned at the specified row in the list

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //creates a temporary ChatDatabaseHelper object
        ChatDatabaseHelper chatDbHelper = new ChatDatabaseHelper(this);

        // gets a writable database and stores that as an instance variable
        db = chatDbHelper.getWritableDatabase();

        listView = findViewById(R.id.list_View);
        buttonSend = findViewById(R.id.button_Send);
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatText = findViewById(R.id.chat_field);
                //get text from the EditText field and put into array list
                String chatString = chatText.getText().toString();
                msgChat_list.add(chatString);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount() & getView()
                chatText.setText("");

                //insert message to database
                // This class - ContentValues is used to store a set of values/insert rows into database
                ContentValues contentValues = new ContentValues();

                // description is column in CHAT_HISTORY table
                //contentValues.put("ID", 0);

                // name - column
                contentValues.put("MESSAGE", chatString);
                db.insert("CHAT_HISTORY", null, contentValues);  //CHAT_HISTORY is table name
            }
        });
        cursor = db.query(ChatDatabaseHelper.TABLE_NAME, new String[]
                        {ChatDatabaseHelper.ID, ChatDatabaseHelper.KEY_MESSAGE},
                null,
                null,
                null,
                null,
                null,
                null);

        while (!cursor.isAfterLast()) {

          /*  Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(
                    cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));*/
            cursor.moveToNext();
            Log.i(ACTIVITY_NAME, "Cursor’s  column count = " + cursor.getColumnCount());
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, "Column #" + (i + 1) + " is " + cursor.getColumnName(i));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
