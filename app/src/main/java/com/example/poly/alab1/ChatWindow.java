package com.example.poly.alab1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    ArrayList<String> msgChat_list = new ArrayList<>();
    ArrayList<Long> chat_ID  = new ArrayList<>();
    ListView       listView;
    Button         buttonSend;
    EditText       chatText;
    ChatAdapter    messageAdapter;
    Cursor         cursor;
    SQLiteDatabase db;
    boolean isTablet;
    MessageFragment messageFragment = new MessageFragment();



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
//returns the database id of the item at position i

            return chat_ID.get(position);
            //return position;
        }
//then iterates in a for loop, calling  getView(int position)
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

        isTablet = findViewById(R.id.frameLayout) != null;

        listView = findViewById(R.id.list_View);
        buttonSend = findViewById(R.id.button_Send);

        messageAdapter = new ChatAdapter(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("userInput", msgChat_list.get(i));
                bundle.putLong("ID", id);
//if on tablet:
                if(!isTablet)//this is a phone
                {
                    Intent goTo = new Intent(ChatWindow.this, MessageDetails.class);
                    goTo.putExtras(bundle);
                    startActivityForResult(goTo, 1);

                }
                else //this is Tablet
                {
                    FragmentManager     fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    messageFragment.setIsTablet(true);
                    messageFragment.setArguments( bundle );
                    fragmentTransaction.addToBackStack(" ") //You can call transaction.addToBackStack(String name) if you want to undo this transaction
                    // with the back button. Otherwise the back button changes the Activity. The name parameter is optional.
                                       .replace( R.id.frameLayout , messageFragment)
                                       .commit();

                }
            }
        });


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
               chat_ID.add(db.insert("CHAT_HISTORY", null, contentValues));  //CHAT_HISTORY is table name
            }
        });
        cursor = db.query(true,ChatDatabaseHelper.TABLE_NAME, new String[]
                        {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            Log.i("ChatWindow activity", "SQL MESSAGE:" + message);
            msgChat_list.add(message);
            chat_ID.add(cursor.getLong( cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID) ));
            Log.i(ACTIVITY_NAME, "Cursor’s  column count = " + cursor.getColumnCount());
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, "Column #" + (i + 1) + " is " + cursor.getColumnName(i));
            }
            cursor.moveToNext();
        }
        listView.setAdapter(messageAdapter);
    }

    public void deleteMessage(long id)
    {
        db.delete(ChatDatabaseHelper.TABLE_NAME, "ID is ?", new String[] {Long.toString(id)});
        if(isTablet)
        {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(messageFragment)
                               .commit();
        }
        cursor = db.query( ChatDatabaseHelper.TABLE_NAME,
                new String[] {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null,
                null,
                null,
                null,
                null,
                null);
       // msgChat_list = new ArrayList<>();
        //chat_ID = new ArrayList<>();

        //remove old messages:
chat_ID.clear();
msgChat_list.clear();

//load new messages:
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            Log.i("ChatWindow activity", "SQL MESSAGE:" +message );
           int ind = cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID);
            chat_ID.add(new Long(cursor.getInt(ind)));
            msgChat_list.add(message);
            cursor.moveToNext();
        }
        messageAdapter.notifyDataSetChanged();//reload table
    }

    public void onActivityResult(int req, int res, Intent data)
    {
        if(res == 10)
        {
            long id=data.getExtras().getLong("ID");
            deleteMessage(id);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
