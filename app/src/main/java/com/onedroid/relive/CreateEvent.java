package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.onedroid.relive.databinding.ActivityCreateEventBinding;
import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateEvent extends AppCompatActivity {


    private ActivityCreateEventBinding binding;
    AccountService mService;
    boolean mBound = false;

    String fromDate;
    String toDate;
    long fromDateInMillis;
    long toDateInMillis;

    private final int SHARE_ACTIVITY_CODE = 2;
    private ArrayList<String> invitedUsers = new ArrayList<>();

    MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText eventName = (EditText) findViewById(R.id.eventName);
        Button createEvent = (Button) findViewById(R.id.createEvent);
        Button startDateButton = (Button) findViewById(R.id.start_date);
        Button endDateButton = (Button) findViewById(R.id.end_date);
        Button shareButton = (Button) findViewById(R.id.shareButton);


        long currTime = System.currentTimeMillis();
        long curPlusMonth = System.currentTimeMillis() + 30L * 86400000;
        fromDateInMillis = getIntent().getLongExtra("fromDateInMillis", currTime);
        toDateInMillis = getIntent().getLongExtra("toDateInMillis",curPlusMonth);
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        fromDate = df.format(new Date(fromDateInMillis));
        toDate = df.format(new Date(toDateInMillis));




        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventNameStr = eventName.getText().toString();
                if(!eventNameStr.isEmpty()) {
                    long diff = (toDateInMillis - fromDateInMillis) / 60000;
                    if (diff < 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Choose a \"From\" date and time before the \"To\" date and time",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    } else {
                        for (Event other : mService.getEvents()) {
                            if (other.getName().equals(eventNameStr)) {
                                try {
                                    if (df.parse(other.getToDate()).after(df.parse(fromDate)) || df.parse(other.getFromDate()).before(df.parse(toDate))) {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Another event already exists with the same name and dates",
                                                Toast.LENGTH_LONG);
                                        toast.show();
                                        return;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    //List of invited users from the share event activity
                    UUID eventId = UUID.randomUUID();
                    Event event = new Event(eventId, String.valueOf(eventName.getText()), fromDate, toDate, invitedUsers);
                    try {
                        mService.addEvent(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(!invitedUsers.isEmpty()) {
                        for (String user : invitedUsers) {
                            try {
                                mService.addInvite(event, user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Event name cannot be empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MaterialDatePicker fromMaterialDatePicker = materialDateBuilder
                        .setTitleText("Select a Start Date")
                        .setSelection(fromDateInMillis)
                        .build();
                fromMaterialDatePicker.show(getSupportFragmentManager(), "FROM_MATERIAL_DATE_PICKER");

                fromMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        fromDate = fromMaterialDatePicker.getHeaderText();
                        startDateButton.setText(fromDate);
                        fromDateInMillis = getMillisFromDate(fromDate);
                    }
                });
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(fromDateInMillis);
                ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
                listValidators.add(dateValidatorMin);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
                CalendarConstraints calendarConstraints = calendarConstraintsBuilder
                        .setValidator(validators)
                        .build();

                MaterialDatePicker toMaterialDatePicker = materialDateBuilder
                        .setTitleText("Select an End Date")
                        .setSelection(fromDateInMillis)
                        .setCalendarConstraints(calendarConstraints)
                        .build();
                toMaterialDatePicker.show(getSupportFragmentManager(), "TO_MATERIAL_DATE_PICKER");

                toMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        toDate = toMaterialDatePicker.getHeaderText();
                        endDateButton.setText(toDate);
                        toDateInMillis = getMillisFromDate(toDate);
                    }
                });
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareActivityIntent = new Intent(CreateEvent.this,ShareEvent.class);
                shareActivityIntent.putStringArrayListExtra("attendees",new ArrayList<String>());
                startActivityForResult(shareActivityIntent,SHARE_ACTIVITY_CODE);

            }
        });

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

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SHARE_ACTIVITY_CODE) {
            invitedUsers = data.getStringArrayListExtra("attendees");
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
    }



    public long getMillisFromDate(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTimeInMillis();
    }
}