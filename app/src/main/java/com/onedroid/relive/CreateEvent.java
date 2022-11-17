package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.onedroid.relive.databinding.ActivityCreateEventBinding;
import com.onedroid.relive.databinding.ActivityMainBinding;
import com.onedroid.relive.model.Event;
import com.onedroid.relive.service.AccountService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity {


    private ActivityCreateEventBinding binding;
    AccountService mService;
    boolean mBound = false;

    String fromDate;
    String toDate;
    long initialDateInMillis;
    long finalDateInMillis;
    long fromDateInMillis;
    long toDateInMillis;

    MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        EditText eventName = (EditText) findViewById(R.id.eventName);
        Button createEvent = (Button) findViewById(R.id.createEvent);
        Button startDateButton = (Button) findViewById(R.id.start_date);
        Button endDateButton = (Button) findViewById(R.id.end_date);
        Button shareButton = (Button) findViewById(R.id.shareButton);

        fromDateInMillis = getIntent().getLongExtra("fromDateInMillis",0);
        initialDateInMillis = getIntent().getLongExtra("initialDateInMillis",0);
        toDateInMillis = getIntent().getLongExtra("toDateInMillis",0);
        finalDateInMillis = getIntent().getLongExtra("finalDateInMillis",0);
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        fromDate = df.format(new Date(fromDateInMillis));
        toDate = df.format(new Date(toDateInMillis));

        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());

        generateView();

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long diff = (toDateInMillis-fromDateInMillis)/60000;
                if (diff<0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Choose a \"From\" date and time before the \"To\" date and time",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Event event = new Event(String.valueOf(eventName.getText()),fromDate, toDate);
                try {
                    mService.addEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(initialDateInMillis-86400000);
                CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(finalDateInMillis);
                ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
                listValidators.add(dateValidatorMin);
                listValidators.add(dateValidatorMax);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
                CalendarConstraints calendarConstraints = calendarConstraintsBuilder
                        .setValidator(validators)
                        .build();

                MaterialDatePicker fromMaterialDatePicker = materialDateBuilder
                        .setTitleText("Select a Start Date")
                        .setCalendarConstraints(calendarConstraints)
                        .setSelection(fromDateInMillis)
                        .build();
                fromMaterialDatePicker.show(getSupportFragmentManager(), "FROM_MATERIAL_DATE_PICKER");

                fromMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        fromDate = fromMaterialDatePicker.getHeaderText();
                        startDateButton.setText(fromDate);
                        fromDateInMillis = getMillisFromDate(fromDate);
                        generateView();
                    }
                });
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(initialDateInMillis - 86400000);
                CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(finalDateInMillis);
                ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<>();
                listValidators.add(dateValidatorMin);
                listValidators.add(dateValidatorMax);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
                CalendarConstraints calendarConstraints = calendarConstraintsBuilder
                        .setValidator(validators)
                        .build();

                MaterialDatePicker toMaterialDatePicker = materialDateBuilder
                        .setTitleText("Select an End Date")
                        .setCalendarConstraints(calendarConstraints)
                        .setSelection(toDateInMillis)
                        .build();
                toMaterialDatePicker.show(getSupportFragmentManager(), "TO_MATERIAL_DATE_PICKER");

                toMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        toDate = toMaterialDatePicker.getHeaderText();
                        endDateButton.setText("toDate");
                        toDateInMillis = getMillisFromDate(toDate);
                        generateView();
                    }
                });
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareActivity = new Intent();


            }
        });

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

    private void generateView() {

        LinearLayout headerLL = (LinearLayout) findViewById(R.id.headerLL);
        LinearLayout eventNameLL =(LinearLayout) findViewById(R.id.EventNameLL);
        RelativeLayout FromToRL = (RelativeLayout) findViewById(R.id.FromToRL);
        TextView FromText = (TextView) findViewById(R.id.FromText);
        TextView ToText = (TextView) findViewById(R.id.ToText);
        RelativeLayout dateButtonsRL = (RelativeLayout) findViewById(R.id.dateButtonsRL);
        LinearLayout shareButtonLL = (LinearLayout) findViewById(R.id.shareButtonLL);
        RelativeLayout CreateEventRL = (RelativeLayout) findViewById(R.id.CreateEventRL);
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