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
import com.onedroid.relive.design.CustomAdapter;
import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;


import java.util.ArrayList;
import java.util.Calendar;

public class SearchEvent extends AppCompatActivity implements AdapterView.OnItemClickListener,ServiceConnection {
    AccountService mService;
    boolean mBound = false;
    private ArrayList<Event> eventList;
    private SearchView searchView;

    private TextView fromDate;
    private TextView toDate;

    private DatePickerDialog.OnDateSetListener fromDateSetListener;
    private DatePickerDialog.OnDateSetListener toDateSetListener;

    private static final String TAG = "Search Event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_event);
        Intent intent = new Intent(this, AccountService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        eventList = setEventDetails();
        CustomAdapter customAdapter = new
                CustomAdapter(SearchEvent.this, 0, eventList);

        // Search By Event
        searchView = findViewById(R.id.eventListSearchView);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText,customAdapter);
                return false;
            }
        });

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



        // Event List View
        ListView listView = findViewById(R.id.eventListView);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);

    }

    private void filterList(String text, CustomAdapter customAdapter) {
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
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

    private ArrayList<Event> setEventDetails() {
        eventList = new ArrayList<>();
        eventList.add(new Event("Bob's Birthday", "10-22-2022","10-22-2022"));
        eventList.add(new Event("Alice's Birthday", "10-22-2022","10-22-2022"));
        eventList.add(new Event("Ree's Wedding", "10-22-2022","10-29-2022"));
        eventList.add(new Event("NYC Trip", "12-22-2022","12-31-2022"));
        eventList.add(new Event("Halloween", "10-31-2022","10-31-2022"));
        return eventList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event list = eventList.get(i);
        Toast.makeText(SearchEvent.this, "Event Name : "
                        + list.getName(),
                Toast.LENGTH_SHORT).show();
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