package com.example.poly.alab1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class MessageFragment extends Fragment {

    private long id;
    boolean isTablet;
    String userInput;
    Context parent=null;


    @Override
    public void onAttach(Context activity)
    {
        super.onAttach(activity);
        parent = activity;
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Bundle getInput = getArguments();
        userInput = getInput.getString("userInput");
        id = getInput.getLong("ID");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View     layForFragment = inflater.inflate(R.layout.fragment_layout, null);
        TextView fragment_id        = layForFragment.findViewById(R.id.fragment_id);
        TextView messageFragment     = layForFragment.findViewById(R.id.fragment_message);

        messageFragment.setText("Message: "+ userInput);
        fragment_id.setText(Long.toString(id));


        Button buttonDelete = layForFragment.findViewById(R.id.button_Delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //parent.getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                //FOR PHONE ONLY!!!
                //create intent, get message id (that will be deleted) and pass back to ChatWindow
                // where it will be deleted from database(listview)
                //getActivity().setResult(int resultCode, Intent data)
                //if this is a phone or tablet
                if(!isTablet) {
                    Intent goToTheDetails = new Intent();
                    goToTheDetails.putExtra("ID", id);
                   getActivity().setResult(10, goToTheDetails);
                   getActivity().finish();
                }
                else {
                    ChatWindow p = (ChatWindow)parent;
                    p.deleteMessage(id);
                }
            }
        });
        return layForFragment;
    }
    public void setIsTablet(boolean b)
    {
        isTablet =b;
    }




}
