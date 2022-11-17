package com.onedroid.relive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShareEvent extends AppCompatActivity {

    String usernames = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        EditText usernameBox = (EditText) findViewById(R.id.editTextBox);
        Button addButton = (Button) findViewById(R.id.addButton);
        Button doneButton = (Button) findViewById(R.id.doneButton);
        TextView participantList = (TextView) findViewById(R.id.participant_list);
        generateView();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usernameBox.getText().toString().trim().isEmpty()) {
                    usernames += usernameBox.getText().toString() + "\n";
                    usernameBox.setText("");
                    participantList.setText(usernames);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateEvent.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void generateView() {
        return;
    }



}
