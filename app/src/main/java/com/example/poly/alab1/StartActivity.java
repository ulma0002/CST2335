package com.example.poly.alab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    String       messagePassed;
    CharSequence text;
    int          duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button buttonText = findViewById(R.id.button);
        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondIntent = new Intent(StartActivity.this, ListItemActivity.class);
                //startActivity(secondIntent);
                // Activity is started with requestCode 50
                startActivityForResult(secondIntent, 50);
                Log.i(ACTIVITY_NAME, "In onCreate()");
            }
        });
        Button buttonChat = findViewById(R.id.button_Chat);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 50) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
        if (responseCode == Activity.RESULT_OK) {
            messagePassed = data.getStringExtra("Response");
            text = "ListItemsActivity passed: My information to share";
            duration = Toast.LENGTH_LONG;
            Toast toast_message = Toast.makeText(StartActivity.this, text, duration);
            toast_message.show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }


}
