package com.onedroid.relive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onedroid.relive.service.AccountService;

import java.util.ArrayList;
import java.util.List;

import io.getstream.avatarview.AvatarView;


public class FilteringActivity extends AppCompatActivity {

    // Filter thumb range should make sense
    // Pick up start and end dates from event
    // Style everything
    // Dynamically render users with names

    boolean isContributorSwitchChecked;
    boolean isTimeSwitchChecked;
    float leftThumbPosition;
    float rightThumbPosition;
    boolean[] selectedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);

        isContributorSwitchChecked = getIntent().getBooleanExtra("contributorSwitchIsChecked",false);
        isTimeSwitchChecked = getIntent().getBooleanExtra("timeSwitchIsChecked",false);
        leftThumbPosition = getIntent().getFloatExtra("leftThumbPosition",0);
        rightThumbPosition = getIntent().getFloatExtra("rightThumbPosition",100);
        selectedUsers = getIntent().getBooleanArrayExtra("selectedUsers")!=null?getIntent().getBooleanArrayExtra("selectedUsers"):new boolean[5];

        Button applyButton = findViewById(R.id.applyButton);
        SwitchMaterial contributorSwitch = (SwitchMaterial) findViewById(R.id.contributorSwitch);
        SwitchMaterial timeSwitch = (SwitchMaterial) findViewById(R.id.timeSwitch);
        RangeSlider timeRangeSlider = (RangeSlider) findViewById(R.id.timeRangeSlider);
        TextView fromDate = findViewById(R.id.fromDate);
        fromDate.setText(getIntent().getStringExtra("fromDate"));
        TextView toDate = findViewById(R.id.toDate);
        toDate.setText(getIntent().getStringExtra("toDate"));
        AvatarView atharvAvatar = (AvatarView) findViewById(R.id.atharv);
        AvatarView jaiminAvatar = (AvatarView) findViewById(R.id.jaimin);
        AvatarView yadnikiAvatar = (AvatarView) findViewById(R.id.yadniki);
        AvatarView tonyAvatar = (AvatarView) findViewById(R.id.tony);
        AvatarView karanAvatar = (AvatarView) findViewById(R.id.karan);

        generateView();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageGridActivityIntent = new Intent();
                int numberOfImages = 18;

                //Calculating fraction based on time range
                leftThumbPosition = timeRangeSlider.getValues().get(0);
                rightThumbPosition = timeRangeSlider.getValues().get(1);
                float valueFrom = timeRangeSlider.getValueFrom();
                float valueTo = timeRangeSlider.getValueTo();
                double timeFraction = Math.abs(rightThumbPosition-leftThumbPosition)/Math.abs(valueTo-valueFrom);
                if (!isTimeSwitchChecked) timeFraction=1;

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
                imageGridActivityIntent.putExtra("leftThumbPosition",leftThumbPosition);
                imageGridActivityIntent.putExtra("rightThumbPosition",rightThumbPosition);
                numberOfImages = (int)(numberOfImages*timeFraction*contributorFraction);
                if(numberOfImages<=2) numberOfImages+=3;
                imageGridActivityIntent.putExtra("numberOfImagesToShow",numberOfImages);
                imageGridActivityIntent.putExtra("selectedUsers",selectedUsers);
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
        RangeSlider timeRangeSlider = (RangeSlider) findViewById(R.id.timeRangeSlider);
        LinearLayout timeDatesLinearLayout = (LinearLayout) findViewById(R.id.timeDatesLinearLayout);
        LinearLayout contributorsLinearLayout = (LinearLayout) findViewById(R.id.contributorsLinearLayout);
        AvatarView atharvAvatar = (AvatarView) findViewById(R.id.atharv);
        AvatarView jaiminAvatar = (AvatarView) findViewById(R.id.jaimin);
        AvatarView yadnikiAvatar = (AvatarView) findViewById(R.id.yadniki);
        AvatarView tonyAvatar = (AvatarView) findViewById(R.id.tony);
        AvatarView karanAvatar = (AvatarView) findViewById(R.id.karan);

        // Handling the switches and their functionality
        contributorSwitch.setChecked(isContributorSwitchChecked);
        timeSwitch.setChecked(isTimeSwitchChecked);
        timeRangeSlider.setEnabled(isTimeSwitchChecked);
        timeRangeSlider.setBackgroundColor(isTimeSwitchChecked?Color.parseColor("white"):Color.parseColor("lightgrey"));
        timeDatesLinearLayout.setBackgroundColor(isTimeSwitchChecked?Color.parseColor("white"):Color.parseColor("lightgrey"));
        contributorsLinearLayout.setBackgroundColor(isContributorSwitchChecked?Color.parseColor("white"):Color.parseColor("lightgrey"));

        // Handle Slider Functionality
        List<Float> rangeSliderThumbPositions = new ArrayList<>();
        rangeSliderThumbPositions.add(leftThumbPosition);
        rangeSliderThumbPositions.add(rightThumbPosition);
        timeRangeSlider.setValues(rangeSliderThumbPositions);

        // Handle Contributors Functionality
        atharvAvatar.setAvatarBorderWidth(selectedUsers[0]?10:0);
        jaiminAvatar.setAvatarBorderWidth(selectedUsers[1]?10:0);
        yadnikiAvatar.setAvatarBorderWidth(selectedUsers[2]?10:0);
        tonyAvatar.setAvatarBorderWidth(selectedUsers[3]?10:0);
        karanAvatar.setAvatarBorderWidth(selectedUsers[4]?10:0);

    }

}