package com.example.poly.alab1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        Bundle              info = getIntent().getExtras();
        FragmentManager     fragmentManager   = getFragmentManager();
        FragmentTransaction fragmentTransaction   = fragmentManager.beginTransaction();
        MessageFragment     messageFragment   =  new MessageFragment();
        messageFragment.setIsTablet(false);
        messageFragment.setArguments( info );
        // ft.addToBackStack(" "); //You can call transaction.addToBackStack(String name) if you want to undo this transaction
        // with the back button. Otherwise the back button changes the Activity. The name parameter is optional.

        fragmentTransaction.replace( R.id.fragment_layout1 , messageFragment)
                           .commit();
    }

    }



