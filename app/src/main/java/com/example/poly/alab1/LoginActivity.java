package com.example.poly.alab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;


public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";

    String Lab3;


    @Override
    protected void onCreate(Bundle savedInstanceState) //Bundle is an object for storing data by storing the data along with a String variable name.

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button loginButton = findViewById(R.id.login_button);
        EditText textEmailAddress = findViewById(R.id.textEmailAddress);

//Use this if you need multiple shared preference files identified by name, which you
// specify with the first parameter. We can call this from any Context in your app.
        SharedPreferences prefs = getSharedPreferences(Lab3, Context.MODE_PRIVATE);   //  the created file can only be accessed by the calling application
        SharedPreferences.Editor editor = prefs.edit();     //To write to a shared preferences file
        editor.putString(getString(R.string.login_name), textEmailAddress.getText().toString());     //Pass the keys and values you want to write with methods such as putInt() and putString()
        textEmailAddress.setText(prefs.getString("Email", null));
        //editor.commit(); //to save the changes
        editor.apply();  //Consider using `apply()` instead; `commit` writes its data to persistent storage immediately, whereas `apply` will handle it in the background

        loginButton.setOnClickListener(new View.OnClickListener() {  //callback
            @Override
            public void onClick(View view) {
                //callback.onClick();
               // An Intent object specifies which Activity to start
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
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
