package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;

public class JoinEvent extends AppCompatActivity implements ServiceConnection{

    AccountService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);
        Intent intent = new Intent(this, AccountService.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);

         FloatingActionButton addEvent = (FloatingActionButton) findViewById(R.id.accept);
         FloatingActionButton addEvent2 = (FloatingActionButton) findViewById(R.id.accept2);
        FloatingActionButton addEvent3 = (FloatingActionButton) findViewById(R.id.accept3);
        FloatingActionButton addEvent4 = (FloatingActionButton) findViewById(R.id.accept4);

        addEvent.setOnClickListener(new View.OnClickListener() {
            /**
             * Overrides onClick.
             * Try to add a Event with eventName from and to date
             * @param view
             */
            @Override
            public void onClick(View view) {
                ExtendedFloatingActionButton input = (ExtendedFloatingActionButton) findViewById(R.id.graduation);
                String eventName = input.getText().toString().trim();

                try {
                    mService.addEvent(new Event(eventName, "23/10/2022", "23/10/2022"));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(JoinEvent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addEvent2.setOnClickListener(new View.OnClickListener() {
            /**
             * Overrides onClick.
             * Try to add a Event with eventName from and to date
             * @param view
             */
            @Override
            public void onClick(View view) {
                ExtendedFloatingActionButton input = (ExtendedFloatingActionButton) findViewById(R.id.birthday);
                String eventName = input.getText().toString().trim();

                try {
                    mService.addEvent(new Event(eventName, "16/10/2022", "16/10/2022"));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(JoinEvent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addEvent3.setOnClickListener(new View.OnClickListener() {
            /**
             * Overrides onClick.
             * Try to add a Event with eventName from and to date
             * @param view
             */
            @Override
            public void onClick(View view) {
                ExtendedFloatingActionButton input = (ExtendedFloatingActionButton) findViewById(R.id.halloween);
                String eventName = input.getText().toString().trim();

                try {
                    mService.addEvent(new Event(eventName, "31/10/2022", "31/10/2022"));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(JoinEvent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addEvent4.setOnClickListener(new View.OnClickListener() {
            /**
             * Overrides onClick.
             * Try to add a Event with eventName from and to date
             * @param view
             */
            @Override
            public void onClick(View view) {
                ExtendedFloatingActionButton input = (ExtendedFloatingActionButton) findViewById(R.id.nba);
                String eventName = input.getText().toString().trim();

                try {
                    mService.addEvent(new Event(eventName, "20/10/2022", "20/10/2022"));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(JoinEvent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


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
}