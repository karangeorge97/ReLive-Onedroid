package com.onedroid.relive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.onedroid.relive.service.AccountService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.getstream.avatarview.AvatarView;


public class FilteringActivity extends AppCompatActivity {

    // Pick up start and end dates from event
    // Style everything
    // Dynamically render users with names

    boolean isContributorSwitchChecked;
    boolean isTimeSwitchChecked;
    boolean[] selectedUsers;
    String fromDate;
    String toDate;
    long initialDateInMillis;
    long finalDateInMillis;
    long fromDateInMillis;
    long toDateInMillis;
    int fromTimeHours = 0;
    int fromTimeMinutes = 0;
    int toTimeHours = 23;
    int toTimeMinutes = 59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);

        isContributorSwitchChecked = getIntent().getBooleanExtra("contributorSwitchIsChecked",false);
        isTimeSwitchChecked = getIntent().getBooleanExtra("timeSwitchIsChecked",false);
        selectedUsers = getIntent().getBooleanArrayExtra("selectedUsers")!=null?getIntent().getBooleanArrayExtra("selectedUsers"):new boolean[5];

        Button applyButton = findViewById(R.id.applyButton);
        SwitchMaterial contributorSwitch = (SwitchMaterial) findViewById(R.id.contributorSwitch);
        SwitchMaterial timeSwitch = (SwitchMaterial) findViewById(R.id.timeSwitch);
        fromDateInMillis = getIntent().getLongExtra("fromDateInMillis",0);
        initialDateInMillis = getIntent().getLongExtra("initialDateInMillis",0);
        toDateInMillis = getIntent().getLongExtra("toDateInMillis",0);
        finalDateInMillis = getIntent().getLongExtra("finalDateInMillis",0);
        fromTimeHours = getIntent().getIntExtra("fromTimeHours",0);
        fromTimeMinutes = getIntent().getIntExtra("fromTimeMinutes",0);
        toTimeHours = getIntent().getIntExtra("toTimeHours",23);
        toTimeMinutes = getIntent().getIntExtra("toTimeMinutes",59);
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        fromDate = df.format(new Date(fromDateInMillis));
        toDate = df.format(new Date(toDateInMillis));

        AvatarView atharvAvatar = (AvatarView) findViewById(R.id.atharv);
        AvatarView jaiminAvatar = (AvatarView) findViewById(R.id.jaimin);
        AvatarView yadnikiAvatar = (AvatarView) findViewById(R.id.yadniki);
        AvatarView tonyAvatar = (AvatarView) findViewById(R.id.tony);
        AvatarView karanAvatar = (AvatarView) findViewById(R.id.karan);

        Button fromDateButton = (Button) findViewById(R.id.fromDateButton);
        Button toDateButton = (Button) findViewById(R.id.toDateButton);
        Button fromTimeButton = (Button) findViewById(R.id.fromTimeButton);
        Button toTimeButton = (Button) findViewById(R.id.toTimeButton);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        generateView();

        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimeSwitchChecked) {
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
                            .setTitleText("Select a from date")
                            .setCalendarConstraints(calendarConstraints)
                            .setSelection(fromDateInMillis)
                            .build();
                    fromMaterialDatePicker.show(getSupportFragmentManager(), "FROM_MATERIAL_DATE_PICKER");

                    fromMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick(Object selection) {
                            fromDate = fromMaterialDatePicker.getHeaderText();
                            fromDateInMillis = getMillisFromDate(fromDate);
                            generateView();
                        }
                    });
                }
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimeSwitchChecked) {
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
                            .setTitleText("Select a to date")
                            .setCalendarConstraints(calendarConstraints)
                            .setSelection(toDateInMillis)
                            .build();
                    toMaterialDatePicker.show(getSupportFragmentManager(), "TO_MATERIAL_DATE_PICKER");

                    toMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick(Object selection) {
                            toDate = toMaterialDatePicker.getHeaderText();
                            toDateInMillis = getMillisFromDate(toDate);
                            generateView();
                        }
                    });
                }
            }
        });

        fromTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimeSwitchChecked) {
                    MaterialTimePicker fromMaterialTimePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(fromTimeHours)
                            .setMinute(fromTimeMinutes)
                            .setTitleText("Select a from time")
                            .build();
                    fromMaterialTimePicker.show(getSupportFragmentManager(), "FROM_MATERIAL_TIME_PICKER");

                    fromMaterialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fromTimeMinutes = fromMaterialTimePicker.getMinute();
                            fromTimeHours = fromMaterialTimePicker.getHour();
                            generateView();
                        }
                    });
                }
            }
        });

        toTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimeSwitchChecked) {
                    MaterialTimePicker toMaterialTimePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(toTimeHours)
                            .setMinute(toTimeMinutes)
                            .setTitleText("Select a to time")
                            .build();
                    toMaterialTimePicker.show(getSupportFragmentManager(), "FROM_MATERIAL_TIME_PICKER");

                    toMaterialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toTimeMinutes = toMaterialTimePicker.getMinute();
                            toTimeHours = toMaterialTimePicker.getHour();
                            generateView();
                        }
                    });
                }
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageGridActivityIntent = new Intent();
                int numberOfImages = 18;

                //Calculating fraction based on time range
                long diff = (toDateInMillis-fromDateInMillis)/60000 + (toTimeHours-fromTimeHours)*60 + (toTimeMinutes-fromTimeMinutes);
                long maxdiff = (finalDateInMillis-initialDateInMillis)/60000 + 1440;
                if (diff<0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Choose a \"From\" date and time before the \"To\" date and time",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                //Cast to double int division was always returning 0 meaning the numberOfimages in was always 3
                double timeFraction = (double) diff/maxdiff;
                if(!isTimeSwitchChecked || timeFraction==0) timeFraction = 1;

                //Calculating fraction based on contributors selected
                double contributorFraction = 0.0;
                for(boolean b : selectedUsers) {
                    contributorFraction += b ? 0.2 : 0;
                }
                if(!isContributorSwitchChecked || contributorFraction==0) contributorFraction = 1.0;

                imageGridActivityIntent.putExtra("username", getIntent().getStringExtra("username"));
                imageGridActivityIntent.putExtra("filterApplied",isContributorSwitchChecked||isTimeSwitchChecked);
                imageGridActivityIntent.putExtra("contributorSwitchIsChecked",isContributorSwitchChecked);
                imageGridActivityIntent.putExtra("timeSwitchIsChecked",isTimeSwitchChecked);
               //Added this to indicate if no filters
                if(isContributorSwitchChecked||isTimeSwitchChecked) {
                    numberOfImages = (int) (numberOfImages * contributorFraction * timeFraction);
                    if (numberOfImages <= 2) numberOfImages += 3;
                }
                imageGridActivityIntent.putExtra("numberOfImagesToShow",numberOfImages);
                imageGridActivityIntent.putExtra("fromDateInMillis", fromDateInMillis);
                imageGridActivityIntent.putExtra("toDateInMillis",toDateInMillis);
                imageGridActivityIntent.putExtra("selectedUsers",selectedUsers);
                imageGridActivityIntent.putExtra("fromTimeHours",fromTimeHours);
                imageGridActivityIntent.putExtra("fromTimeMinutes",fromTimeMinutes);
                imageGridActivityIntent.putExtra("toTimeHours",toTimeHours);
                imageGridActivityIntent.putExtra("toTimeMinutes",toTimeMinutes);
                setResult(Activity.RESULT_OK, imageGridActivityIntent);
                finish();
            }
        });

        contributorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isContributorSwitchChecked = isChecked;
                generateView();
            }
        });

        timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTimeSwitchChecked = isChecked;
                generateView();
            }
        });

        atharvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isContributorSwitchChecked) {
                    selectedUsers[0] = !selectedUsers[0];
                    if (selectedUsers[0]) {
                        atharvAvatar.setAvatarBorderWidth(10);
                    } else {
                        atharvAvatar.setAvatarBorderWidth(0);
                    }
                }
            }
        });

        jaiminAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isContributorSwitchChecked) {
                    selectedUsers[1] = !selectedUsers[1];
                    if (selectedUsers[1]) {
                        jaiminAvatar.setAvatarBorderWidth(10);
                    } else {
                        jaiminAvatar.setAvatarBorderWidth(0);
                    }
                }
            }
        });

        yadnikiAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isContributorSwitchChecked) {
                    selectedUsers[2] = !selectedUsers[2];
                    if (selectedUsers[2]) {
                        yadnikiAvatar.setAvatarBorderWidth(10);
                    } else {
                        yadnikiAvatar.setAvatarBorderWidth(0);
                    }
                }
            }
        });

        tonyAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isContributorSwitchChecked) {
                    selectedUsers[3] = !selectedUsers[3];
                    if (selectedUsers[3]) {
                        tonyAvatar.setAvatarBorderWidth(10);
                    } else {
                        tonyAvatar.setAvatarBorderWidth(0);
                    }
                }
            }
        });

        karanAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isContributorSwitchChecked) {
                    selectedUsers[4] = !selectedUsers[4];
                    if (selectedUsers[4]) {
                        karanAvatar.setAvatarBorderWidth(10);
                    } else {
                        karanAvatar.setAvatarBorderWidth(0);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void generateView() {
        // All the components needed to generate the view
        SwitchMaterial contributorSwitch = (SwitchMaterial) findViewById(R.id.contributorSwitch);
        SwitchMaterial timeSwitch = (SwitchMaterial) findViewById(R.id.timeSwitch);
        LinearLayout contributorsLinearLayout = (LinearLayout) findViewById(R.id.contributorsLinearLayout);
        AvatarView atharvAvatar = (AvatarView) findViewById(R.id.atharv);
        AvatarView jaiminAvatar = (AvatarView) findViewById(R.id.jaimin);
        AvatarView yadnikiAvatar = (AvatarView) findViewById(R.id.yadniki);
        AvatarView tonyAvatar = (AvatarView) findViewById(R.id.tony);
        AvatarView karanAvatar = (AvatarView) findViewById(R.id.karan);
        LinearLayout fromAndToButtonsLayout = (LinearLayout) findViewById(R.id.fromAndToButtonsLayout);

        // Handling the switches and their functionality
        contributorSwitch.setChecked(isContributorSwitchChecked);
        timeSwitch.setChecked(isTimeSwitchChecked);
        contributorsLinearLayout.setBackgroundColor(isContributorSwitchChecked?Color.parseColor("white"):Color.parseColor("lightgrey"));
        fromAndToButtonsLayout.setBackgroundColor(isTimeSwitchChecked?Color.parseColor("white"):Color.parseColor("lightgrey"));

        // Handle Dates Functionality
        Button fromDateButton = (Button) findViewById(R.id.fromDateButton);
        Button toDateButton = (Button) findViewById(R.id.toDateButton);
        Button fromTimeButton = (Button) findViewById(R.id.fromTimeButton);
        Button toTimeButton = (Button) findViewById(R.id.toTimeButton);
        fromDateButton.setText(fromDate);
        toDateButton.setText(toDate);
        fromTimeButton.setText(String.format("%02d:%02d %s", fromTimeHours%12, fromTimeMinutes, fromTimeHours<12?"AM":"PM"));
        toTimeButton.setText(String.format("%02d:%02d %s", toTimeHours%12, toTimeMinutes, toTimeHours<12?"AM":"PM"));

        // Handle Contributors Functionality
        atharvAvatar.setAvatarBorderWidth(selectedUsers[0]?10:0);
        jaiminAvatar.setAvatarBorderWidth(selectedUsers[1]?10:0);
        yadnikiAvatar.setAvatarBorderWidth(selectedUsers[2]?10:0);
        tonyAvatar.setAvatarBorderWidth(selectedUsers[3]?10:0);
        karanAvatar.setAvatarBorderWidth(selectedUsers[4]?10:0);

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