package com.onedroid.relive;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.IBinder;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onedroid.relive.databinding.ActivityMainBinding;
import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private ActivityMainBinding binding;
    AccountService mService;
    boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());


        Button createEvent = findViewById(R.id.createEvent);
        Button joinEvent = findViewById(R.id.joinevent);
        FloatingActionButton searchEvent = findViewById(R.id.searchevent);


        createEvent.setOnClickListener(this);
        joinEvent.setOnClickListener(this);
        searchEvent.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //Logout button ends user session
            case R.id.createEvent:
                Intent createEventActivity = new Intent(getApplicationContext(), CreateEvent.class);
                startActivity(createEventActivity);
                break;
            case R.id.joinevent:
                Intent joinEventActivity = new Intent(getApplicationContext(), JoinEvent.class);
                startActivity(joinEventActivity);
                break;

            case R.id.searchevent:
                Intent searchEventActivity = new Intent(getApplicationContext(), SearchEvent.class);
                searchEventActivity.putExtra("userName", getIntent().getStringExtra("username"));
                startActivity(searchEventActivity);
                break;
        }

    }

    /**
     * Initialize ServiceConnection.
     */
    private ServiceConnection connection = new ServiceConnection() {
        /**
         * Override onServiceConnected
         * get service from binder and set bound to be true and then generate cities.
         * @param componentName
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AccountService.AccountBinder binder = (AccountService.AccountBinder) service;
            mService = binder.getService();
            mBound = true;
            generateEvents();
        }

        /**
         * Override onServiceDiconnected.
         * Set bound to be false.
         * @param componentName
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(mBound) generateEvents();
    }


    private void generateEvents() {
//       Retrieve the existing list of events
        final LinearLayout recentEvents = (LinearLayout) findViewById(R.id.recentEvents);


//       Initialise new box of cities to be populated
        final LinearLayout newEventList = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        newEventList.setId(R.id.eventRows);
        newEventList.setOrientation(LinearLayout.VERTICAL);
        newEventList.setLayoutParams(params);



//    Iterate through the list of Event for the user
        for (Event event : mService.getEvents()) {
            MaterialCardView  view = (MaterialCardView) getLayoutInflater().inflate(R.layout.custom_event_list_card_view, null);
            TextView eventName = (TextView) view.findViewById(R.id.eventName);
            TextView fromDate = (TextView) view.findViewById(R.id.fromDate);
            TextView toDate = (TextView) view.findViewById(R.id.toDate);
            ImageView image = (ImageView) view.findViewById(R.id.eventDisplay) ;


            eventName.setText(event.getName());
            Drawable icon = getDrawable(event.getName());
            image.setImageDrawable(icon);
            fromDate.setText(event.getFromDate());
            toDate.setText(event.getToDate());


            view.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                       Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
                                                       intent.putExtra("eventName", event.getName());
                                                       intent.putExtra("userName", getIntent().getStringExtra("username"));
                                                       startActivity(intent);
                                               }
                                           });
            newEventList.addView(view);
        }
//      Replace Existing list of events with updated list of events
        recentEvents.removeViewAt(1);
        recentEvents.addView(newEventList, 1);
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


    private static  class HolderView{
        private final TextView eventName;
        private final TextView fromDate;
        private final TextView toDate;
        private final ImageView image;

        public HolderView(View view){
            eventName = view.findViewById(R.id.eventName);
            fromDate = view.findViewById(R.id.fromDate);
            toDate = view.findViewById(R.id.toDate);
            image = view.findViewById(R.id.eventDisplay);
        }
    }

}