package com.example.poly.alab1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.poly.alab1.StartActivity.ACTIVITY_NAME;

//import static com.example.poly.alab1.LoginActivity.ACTIVITY_NAME;

public class ListItemActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    CharSequence text;
    int duration;
    ImageButton camera;
    CompoundButton buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        ImageButton camera = findViewById(R.id.image_Button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        /*
         * Abstract method Called when the checked state of a compound button has changed
         * @param buttonView The compound button view whose state has changed
         * @param isChecked  The new checked state of buttonView
         */


        Switch switchButton = findViewById(R.id.switch_Button);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                   text = "Switch is ON";
                   duration = Toast.LENGTH_SHORT;     //Show the view or text notification for a short period of time.
                } else {
                    text = "Switch is OFF";
                    duration = Toast.LENGTH_LONG;       //Show the view or text notification for a long period of time.
                }
                Toast toast = Toast.makeText(ListItemActivity.this, text, duration);      // Make a standard toast that just contains a text view.
                toast.show();                                       //display  message box

            }
        });

       CheckBox checkBox = findViewById(R.id.check_Button);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListItemActivity.this);
                    builder.setMessage(R.string.dialog_message); //Add a dialog message to strings.xml

                    builder.setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Response", "Here is my response");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();

                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        Log.i(ACTIVITY_NAME, "In onCreate()");
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