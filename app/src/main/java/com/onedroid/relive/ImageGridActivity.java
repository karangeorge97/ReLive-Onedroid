package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onedroid.relive.databinding.ActivityImageGridBinding;
import com.onedroid.relive.service.AccountService;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ImageGridActivity extends AppCompatActivity {

    private ActivityImageGridBinding binding;
    AccountService mService;
    boolean mBound = false;
    CustomAdapter customAdapter;

    GridView gridView;
    boolean filterApplied = false;
    private final int FILTER_ACTIVITY_CODE = 1;
    boolean contributorSwitchIsChecked = false;
    boolean timeSwitchIsChecked = false;
    boolean[] selectedUsers = new boolean[5];
    String initialDate;
    String finalDate;
    long fromDateInMillis;
    long toDateInMillis;
    int fromTimeHours = 0;
    int fromTimeMinutes = 0;
    int toTimeHours = 23;
    int toTimeMinutes = 59;

    List<Integer> images_selected = new ArrayList<>();

    List<Integer>images_grad = new ArrayList<>();

    List<Integer> images_bir= new ArrayList<>();

    List<Integer>images_hal= new ArrayList<>();

    List<Integer>images_lal= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        binding = ActivityImageGridBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());

        TextView topLabel = (TextView) findViewById(R.id.eventName);
        topLabel.setText(getIntent().getStringExtra("eventName"));

        for(int i =1 ; i<19 ; i++)
        {
            images_grad.add(getResources().getIdentifier("m" + i +"_grad", "drawable", getPackageName()));
            images_bir.add(getResources().getIdentifier("m" + i +"_bir", "drawable", getPackageName()));
            images_hal.add(getResources().getIdentifier("m" + i +"_hal", "drawable", getPackageName()));
            images_lal.add(getResources().getIdentifier("m" + i +"_lal", "drawable", getPackageName()));
        }

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

        gridView = findViewById(R.id.gridView);


        if(images_selected.size()>=3) {
            ImageView topPhotoOne = findViewById(R.id.topPhoto1);
            topPhotoOne.setImageResource(images_selected.get(0));
            topPhotoOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedImage = images_selected.get(0);
                    startActivity(new Intent(ImageGridActivity.this, ClickedItemActivity.class).putExtra("image", selectedImage));
                }
            });
            ImageView topPhotoTwo = findViewById(R.id.topPhoto2);
            topPhotoTwo.setImageResource(images_selected.get(1));
            topPhotoTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedImage = images_selected.get(1);
                    startActivity(new Intent(ImageGridActivity.this, ClickedItemActivity.class).putExtra("image", selectedImage));
                }
            });
            ImageView topPhotoThree = findViewById(R.id.topPhoto3);
            topPhotoThree.setImageResource(images_selected.get(2));
            topPhotoThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedImage = images_selected.get(2);
                    startActivity(new Intent(ImageGridActivity.this, ClickedItemActivity.class).putExtra("image", selectedImage));
                }
            });
        }
        customAdapter = new CustomAdapter(images_selected, this);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedImage = images_selected.get(i);
                startActivity(new Intent(ImageGridActivity.this,ClickedItemActivity.class).putExtra("image",selectedImage));
            }
        });

        FloatingActionButton filter = findViewById(R.id.filter);
        if (filterApplied) {
            filter.setImageResource(R.drawable.ic_filter_filtering_icon);
        } else {
            filter.setImageResource(R.drawable.ic_filter_off_icon);
        }
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filterActivityIntent = new Intent(ImageGridActivity.this,FilteringActivity.class);
                initialDate = mService.getEvent(getIntent().getStringExtra("eventName")).getFromDate();
                finalDate = mService.getEvent(getIntent().getStringExtra("eventName")).getToDate();
                if (fromDateInMillis == 0) fromDateInMillis = convertDateToTimeInMillis(initialDate);
                if (toDateInMillis == 0) toDateInMillis = convertDateToTimeInMillis(finalDate);
                filterActivityIntent.putExtra("eventName",getIntent().getStringExtra("eventName"));
                filterActivityIntent.putExtra("contributorSwitchIsChecked", contributorSwitchIsChecked);
                filterActivityIntent.putExtra("timeSwitchIsChecked", timeSwitchIsChecked);
                filterActivityIntent.putExtra("selectedUsers",selectedUsers);
                filterActivityIntent.putExtra("initialDateInMillis",convertDateToTimeInMillis(initialDate));
                filterActivityIntent.putExtra("finalDateInMillis",convertDateToTimeInMillis(finalDate));
                filterActivityIntent.putExtra("fromDateInMillis", fromDateInMillis);
                filterActivityIntent.putExtra("toDateInMillis", toDateInMillis);
                filterActivityIntent.putExtra("fromTimeHours", fromTimeHours);
                filterActivityIntent.putExtra("fromTimeMinutes",fromTimeMinutes);
                filterActivityIntent.putExtra("toTimeHours", toTimeHours);
                filterActivityIntent.putExtra("toTimeMinutes", toTimeMinutes);
                startActivityForResult(filterActivityIntent,FILTER_ACTIVITY_CODE);
            }
        });

        FloatingActionButton uploadPhoto = findViewById(R.id.addNew);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create an instance of the
                // intent of the type image
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });

        FloatingActionButton sortPhoto = findViewById(R.id.sortPhoto);
        sortPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.reverse(images_selected);
                customAdapter = new CustomAdapter(images_selected, ImageGridActivity.this);
                gridView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
        });



    }


    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Integer> new_images_selected = images_selected;
        if (resultCode==Activity.RESULT_OK && requestCode == FILTER_ACTIVITY_CODE) {
            filterApplied = data.getBooleanExtra("filterApplied", false);
            contributorSwitchIsChecked = data.getBooleanExtra("contributorSwitchIsChecked", false);
            timeSwitchIsChecked = data.getBooleanExtra("timeSwitchIsChecked", false);
            int numberOfImagesToShow = data.getIntExtra("numberOfImagesToShow", images_selected.size());
            selectedUsers = data.getBooleanArrayExtra("selectedUsers");
            fromTimeHours = data.getIntExtra("fromTimeHours",0);
            toTimeHours = data.getIntExtra("toTimeHours",23);
            fromTimeMinutes = data.getIntExtra("fromTimeMinutes", 0);
            toTimeMinutes = data.getIntExtra("toTimeMinutes",59);
            fromDateInMillis = data.getLongExtra("fromDateInMillis",0);
            toDateInMillis = data.getLongExtra("toDateInMillis",0);
            new_images_selected = images_selected.subList(0, numberOfImagesToShow);
            TextView filteredNumberOfPhotosText = (TextView) findViewById(R.id.filteredNumberOfPhotosText);

            if (filterApplied) {
                filteredNumberOfPhotosText.setText(numberOfImagesToShow + "/18");
            } else {
                filteredNumberOfPhotosText.setText("");
            }
        }
        else if (requestCode==200 && data!=null) {
            switch (getIntent().getStringExtra("eventName")) {
                case "Graduation":
                    new_images_selected.add(R.drawable.m3_grad);
                    break;
                case "Halloween":
                    new_images_selected.add(R.drawable.m3_hal);
                    break;
                case "Birthday":
                    new_images_selected.add(R.drawable.m3_bir);
                    break;
                case "Lakers Game":
                    new_images_selected.add(R.drawable.m3_lal);
                    break;
            }
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Image uploaded successfully",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        customAdapter = new CustomAdapter(new_images_selected, this);
        gridView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        generateInitialView();
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
            if(view == null){
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

    private long convertDateToTimeInMillis(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTimeInMillis();
    }
}
