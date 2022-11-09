package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.onedroid.relive.databinding.ActivityMainBinding;
import com.onedroid.relive.databinding.ActivitySearchEventBinding;
import com.onedroid.relive.design.CustomAdapter;
import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchEvent extends AppCompatActivity implements AdapterView.OnItemClickListener,ServiceConnection {
    AccountService mService;
    boolean mBound = false;
    private Set<Event> eventList;
    private SearchView searchView;
    private ActivitySearchEventBinding binding;

    private TextView fromDate;
    private TextView toDate;

    private DatePickerDialog.OnDateSetListener fromDateSetListener;
    private DatePickerDialog.OnDateSetListener toDateSetListener;

    private static final String TAG = "Search Event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySearchEventBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_search_event);
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());


        // Search By Event
        searchView = findViewById(R.id.eventListSearchView);
        searchView.clearFocus();



        //Search By Date
        fromDate = (TextView) findViewById(R.id.tvFromDate);
        toDate = (TextView) findViewById(R.id.tvToDate);


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get((Calendar.YEAR));
                int month = cal.get((Calendar.MONTH));
                int day = cal.get((Calendar.DAY_OF_MONTH));

                DatePickerDialog dialog = new DatePickerDialog(
                        SearchEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        fromDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                fromDate.setText(date);

            }
        };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get((Calendar.YEAR));
                int month = cal.get((Calendar.MONTH));
                int day = cal.get((Calendar.DAY_OF_MONTH));

                DatePickerDialog dialog = new DatePickerDialog(
                        SearchEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        toDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        toDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                toDate.setText(date);

            }
        };



    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mBound) generateEvents();
    }

    private void filterList(String text, CustomAdapter customAdapter) {
        Set<Event> filteredEvents = new HashSet<>();
        for(Event event: eventList){
            if(event.getName().toLowerCase().contains(text.toLowerCase())){
                filteredEvents.add(event);
            }

            if (filteredEvents.isEmpty()){
                Toast.makeText(this,"No Data Found",Toast.LENGTH_SHORT).show();
            }
            else{
                customAdapter.setFilteredList(filteredEvents);
            }

        }

    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        List<Event> events = new ArrayList<>(eventList);
        Event selectedEvent = events.get(i);

        Intent intent = new Intent(SearchEvent.this, ImageGridActivity.class);
        intent.putExtra("eventName", selectedEvent.getName());
        intent.putExtra("userName", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    private void generateEvents()
    {
        eventList = mService.getEvents();
        CustomAdapter customAdapter = new CustomAdapter(SearchEvent.this, 0, eventList);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventList = mService.getEvents();
                filterList(newText,customAdapter);
                return false;
            }
        });

        ListView listView = findViewById(R.id.eventListView);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
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
            generateEvents();
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