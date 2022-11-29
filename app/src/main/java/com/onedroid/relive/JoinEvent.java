package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;

import java.util.ArrayList;

public class JoinEvent extends AppCompatActivity implements ServiceConnection{

    AccountService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);
        Intent intent = new Intent(this, AccountService.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBound) generateInvites();
    }

    /**
     * Initialize ServiceConnection.
     */
    private ServiceConnection connection = new ServiceConnection() {
        /**
         * Override onServiceConnected.
         * Get service from binder and set bound to be true.
         * @param componentName
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AccountService.AccountBinder binder = (AccountService.AccountBinder) service;
            mService = binder.getService();
            mBound = true;
            generateInvites();
        }

        /**
         * Override onServiceDisconnected.
         * Set bound to be false
         * @param componentName
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }



    private void generateInvites() {
//       Retrieve the existing list of events
        final LinearLayout invites = (LinearLayout) findViewById(R.id.invites);


//       Initialise new box of invites to be populated
        final LinearLayout newInviteList = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        newInviteList.setId(R.id.inviteRows);
        newInviteList.setOrientation(LinearLayout.VERTICAL);
        newInviteList.setLayoutParams(params);



//    Iterate through the list of Event for the user
        for (Event invite : mService.getInvites()) {
            LinearLayoutCompat view = (LinearLayoutCompat) getLayoutInflater().inflate(R.layout.invite_row, null);

            ExtendedFloatingActionButton inviteButton = (ExtendedFloatingActionButton) view.getChildAt(0);
            inviteButton.setText(invite.getName());
            Drawable icon = getDrawable(invite.getName());
            inviteButton.setIcon(icon);

            FloatingActionButton accept = (FloatingActionButton) view.getChildAt(1);
            accept.setImageResource(R.drawable.ic_approved_accept_icon__1_);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mService.removeInvite(invite);
                        invite.addAttendee(getIntent().getStringExtra("userName"));
                        mService.addEvent(invite);
                        ((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());
                        Toast.makeText(JoinEvent.this,"Successfully Joined "+ invite.getName(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


            FloatingActionButton decline = (FloatingActionButton) view.getChildAt(2);
            decline.setImageResource(R.drawable.ic_close_round_line_icon);
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mService.removeInvite(invite);
                        ((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());
                        Toast.makeText(JoinEvent.this,"Declined to join "+ invite.getName(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            newInviteList.addView(view);
        }
//      Replace Existing list of events with updated list of events
        invites.removeViewAt(1);
        invites.addView(newInviteList, 1);
    }


    private Drawable getDrawable(String eventName)
    {

        switch (eventName)
        {
            case "Graduation": return  getResources().getDrawable( R.drawable.ic_date_12_icon );
            case "Birthday": return  getResources().getDrawable( R.drawable.ic_date_14_icon );
            case "Halloween": return  getResources().getDrawable( R.drawable.ic_date_16_icon );
            case "Lakers Game": return  getResources().getDrawable( R.drawable.ic_date_23_icon );
            default: return getResources().getDrawable( R.drawable.ic_date_23_icon );

        }

    }



}