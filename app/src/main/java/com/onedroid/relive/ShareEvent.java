package com.onedroid.relive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.onedroid.relive.model.Event;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShareEvent extends AppCompatActivity {

    EditText usernameBox;
    Button addButton;
    Button doneButton;
    ArrayList<String> usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

         usernameBox = (EditText) findViewById(R.id.editTextBox);
         addButton = (Button) findViewById(R.id.addButton);
         doneButton = (Button) findViewById(R.id.doneButton);
         usernames = getIntent().getStringArrayListExtra("attendees");


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(usernames.contains(usernameBox.getText().toString()))) {
                    usernames.add(usernameBox.getText().toString());
                    usernameBox.setText("");
                    generateInviteRow();
                }
                else
                {
                    Toast.makeText(ShareEvent.this,"User is already a part of the event", Toast.LENGTH_SHORT).show();
                }

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEventActivityIntent = new Intent();
                createEventActivityIntent.putStringArrayListExtra("attendees",usernames);
                setResult(Activity.RESULT_OK, createEventActivityIntent);
                finish();

            }
        });

        generateInviteRow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void generateInviteRow() {

        final LinearLayout invitees = (LinearLayout) findViewById(R.id.sharEventParent);


        final LinearLayout newEventList = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        newEventList.setId(R.id.shareRows);
        newEventList.setOrientation(LinearLayout.VERTICAL);
        newEventList.setLayoutParams(params);

        for(String username : usernames) {
            LinearLayoutCompat view = (LinearLayoutCompat) getLayoutInflater().inflate(R.layout.share_event_row, null);
            ExtendedFloatingActionButton inviteButton = (ExtendedFloatingActionButton) view.getChildAt(0);
            inviteButton.setText(username);
            inviteButton.setIconResource(R.drawable.ic_user_profile_icon);
            newEventList.addView(view);
        }
//      Replace Existing list of events with updated list of events
        invitees.removeViewAt(2);
        invitees.addView(newEventList, 2);
    }




}
