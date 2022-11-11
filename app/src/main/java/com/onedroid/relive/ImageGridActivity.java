package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onedroid.relive.databinding.ActivityImageGridBinding;
import com.onedroid.relive.service.AccountService;


import java.util.Arrays;
import java.util.List;

public class ImageGridActivity extends AppCompatActivity {

    private ActivityImageGridBinding binding;
    AccountService mService;
    boolean mBound = false;
    CustomAdapter customAdapter;

    GridView gridView;
    List<Integer> images_selected;
    boolean filterApplied = false;
    private final int FILTER_ACTIVITY_CODE = 1;
    boolean contributorSwitchIsChecked = false;
    boolean timeSwitchIsChecked = false;
    float leftThumbPosition = 0.0f;
    float rightThumbPosition = 100.0f;
    boolean[] selectedUsers = new boolean[5];

   List<Integer>images_grad = Arrays.asList(R.drawable.m1_grad,R.drawable.m2_grad,R.drawable.m3_grad,R.drawable.m4_grad,
            R.drawable.m5_grad,R.drawable.m6_grad,R.drawable.m7_grad, R.drawable.m8_grad,
            R.drawable.m9_grad, R.drawable.m10_grad, R.drawable.m11_grad, R.drawable.m12_grad,
            R.drawable.m13_grad, R.drawable.m14_grad, R.drawable.m15_grad, R.drawable.m16_grad,
            R.drawable.m17_grad, R.drawable.m18_grad);

    List<Integer> images_bir = Arrays.asList(R.drawable.m1_bir, R.drawable.m2_bir, R.drawable.m3_bir, R.drawable.m4_bir,
            R.drawable.m5_bir, R.drawable.m6_bir, R.drawable.m7_bir, R.drawable.m8_bir, R.drawable.m9_bir,
            R.drawable.m10_bir, R.drawable.m11_bir, R.drawable.m12_bir, R.drawable.m13_bir, R.drawable.m14_bir,
            R.drawable.m15_bir, R.drawable.m16_bir, R.drawable.m17_bir, R.drawable.m18_bir, R.drawable.m19_bir,
            R.drawable.m20_bir, R.drawable.m21_bir);

    List<Integer>images_hal = Arrays.asList(R.drawable.m1_hal, R.drawable.m2_hal, R.drawable.m3_hal, R.drawable.m4_hal,
            R.drawable.m5_hal, R.drawable.m16_hal, R.drawable.m7_hal, R.drawable.m8_hal, R.drawable.m9_hal,
            R.drawable.m10_hal, R.drawable.m11_hal, R.drawable.m12_hal, R.drawable.m13_hal, R.drawable.m14_hal,
            R.drawable.m15_hal, R.drawable.m16_hal, R.drawable.m17_hal, R.drawable.m18_hal, R.drawable.m19_hal,
            R.drawable.m20_hal, R.drawable.m21_hal);

    List<Integer>images_lal = Arrays.asList(R.drawable.m1_lal, R.drawable.m2_lal, R.drawable.m3_lal, R.drawable.m4_lal,
            R.drawable.m5_lal, R.drawable.m6_lal, R.drawable.m7_lal, R.drawable.m8_lal, R.drawable.m9_lal,
            R.drawable.m10_lal, R.drawable.m11_lal, R.drawable.m12_lal, R.drawable.m13_lal, R.drawable.m14_lal,
            R.drawable.m15_lal, R.drawable.m16_lal, R.drawable.m17_lal, R.drawable.m18_lal, R.drawable.m19_lal,
            R.drawable.m20_lal, R.drawable.m21_lal);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        binding = ActivityImageGridBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());

        gridView = findViewById(R.id.gridView);

        switch(getIntent().getStringExtra("eventName"))
        {
            case "Graduation":images_selected = images_grad;
                break;
            case "Halloween":images_selected = images_hal;
                break;
            case "Birthday":images_selected = images_bir;
                break;
            case "Lakers Game":images_selected = images_lal;
                break;
        }

        ImageView topPhotoOne = findViewById(R.id.topPhoto1);
        topPhotoOne.setImageResource(images_selected.get(0));
        ImageView topPhotoTwo = findViewById(R.id.topPhoto2);
        topPhotoTwo.setImageResource(images_selected.get(1));
        customAdapter = new CustomAdapter(images_selected, this);

        FloatingActionButton filter = findViewById(R.id.filter);
        if (filterApplied) {
            filter.setImageResource(R.drawable.ic_filter_filtering_icon);
        } else {
            filter.setImageResource(R.drawable.ic_filter_off_icon);
        }

        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedImage = images_selected.get(i);
                startActivity(new Intent(ImageGridActivity.this,ClickedItemActivity.class).putExtra("image",selectedImage));
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filterActivityIntent = new Intent(ImageGridActivity.this,FilteringActivity.class);
                filterActivityIntent.putExtra("eventName",getIntent().getStringExtra("eventName"));
                filterActivityIntent.putExtra("contributorSwitchIsChecked", contributorSwitchIsChecked);
                filterActivityIntent.putExtra("timeSwitchIsChecked", timeSwitchIsChecked);
                filterActivityIntent.putExtra("leftThumbPosition",leftThumbPosition);
                filterActivityIntent.putExtra("rightThumbPosition",rightThumbPosition);
                filterActivityIntent.putExtra("selectedUsers",selectedUsers);
                startActivityForResult(filterActivityIntent,FILTER_ACTIVITY_CODE);
            }
        });

        FloatingActionButton uploadPhoto = findViewById(R.id.addNew);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == FILTER_ACTIVITY_CODE) {
            filterApplied = data.getBooleanExtra("filterApplied", false);
            contributorSwitchIsChecked = data.getBooleanExtra("contributorSwitchIsChecked", false);
            timeSwitchIsChecked = data.getBooleanExtra("timeSwitchIsChecked", false);
            int numberOfImagesToShow = data.getIntExtra("numberOfImagesToShow",images_selected.size());
            leftThumbPosition = data.getFloatExtra("leftThumbPosition",0.0f);
            rightThumbPosition = data.getFloatExtra("rightThumbPosition",100.0f);
            selectedUsers = data.getBooleanArrayExtra("selectedUsers");
            List<Integer> new_images_selected = images_selected.subList(0,numberOfImagesToShow);
            TextView filteredNumberOfPhotosText = (TextView) findViewById(R.id.filteredNumberOfPhotosText);
            if(filterApplied) {
                filteredNumberOfPhotosText.setText(numberOfImagesToShow + "/18");
            }
            else {
                filteredNumberOfPhotosText.setText("");
            }
            customAdapter = new CustomAdapter(new_images_selected, this);
            gridView.setAdapter(customAdapter);
            generateInitialView();
        }
    }

        public class CustomAdapter extends BaseAdapter {

            private List<Integer> imagesPhoto;
            private Context context;
            private LayoutInflater layoutInflater;

            public CustomAdapter(List<Integer> imagesPhoto, Context context) {
                this.imagesPhoto = imagesPhoto;
                this.context = context;
                this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return imagesPhoto.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = layoutInflater.inflate(R.layout.image_display, viewGroup, false);

                }
                ImageView imageView = view.findViewById(R.id.imageView);
                imageView.setImageResource(imagesPhoto.get(i));
                return view;
            }
        }


        private ServiceConnection connection = new ServiceConnection() {
            /**
             * Override onServiceConnected
             * get service from binder and set bound to be true and then generate cities.
             *
             * @param componentName
             * @param service
             */
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                AccountService.AccountBinder binder = (AccountService.AccountBinder) service;
                mService = binder.getService();
                mBound = true;
                generateInitialView();
            }

            /**
             * Override onServiceDiconnected.
             * Set bound to be false.
             *
             * @param componentName
             */
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };

        @Override
        protected void onResume () {
            super.onResume();
            if (mBound) generateInitialView();
        }

        private void generateInitialView() {
            TextView topLabel = (TextView) findViewById(R.id.eventName);
            topLabel.setText(getIntent().getStringExtra("eventName"));
            FloatingActionButton filter = findViewById(R.id.filter);
            if (filterApplied) {
                filter.setImageResource(R.drawable.ic_filter_filtering_icon);
            }
            else {
                filter.setImageResource(R.drawable.ic_filter_off_icon);
            }
        }
    }
