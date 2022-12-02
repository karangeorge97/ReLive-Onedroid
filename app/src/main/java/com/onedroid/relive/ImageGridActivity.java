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
import android.view.Menu;
import android.view.MenuItem;
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
import com.onedroid.relive.model.Event;
import com.onedroid.relive.model.Image;
import com.onedroid.relive.service.AccountService;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ImageGridActivity extends AppCompatActivity {

    private ActivityImageGridBinding binding;
    AccountService mService;
    boolean mBound = false;
    CustomAdapter customAdapter;

    GridView gridView;
    boolean filterApplied = false;
    private final int FILTER_ACTIVITY_CODE = 1;
    private final int SHARE_EVENT_CODE = 4;
    private final int CLICKED_EVENT_CODE = 5;
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
    boolean ascendingSortedImages = true;
    int numberOfImagesToShow = 18;
    
    


    List<Image> images_selected = new ArrayList<>();

    List<Image>images_grad = new ArrayList<>();

    List<Image> images_bir= new ArrayList<>();

    List<Image> images_hal= new ArrayList<>();

    List<Image> images_lal= new ArrayList<>();

    HashMap<String , Boolean> attendeesMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityImageGridBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this, AccountService.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(binding.getRoot());

        TextView topLabel = (TextView) findViewById(R.id.eventName);
        topLabel.setText(getIntent().getStringExtra("eventName"));


        for(int i =1 ; i<19 ; i++)
        {

            images_grad.add(
                    new Image(getResources().getIdentifier("m" + i +"_grad", "drawable", getPackageName()),(int) ThreadLocalRandom.current().nextInt(0, 5+1),attendeesMap)
            );
            images_bir.add(
                    new Image(getResources().getIdentifier("m" + i +"_bir", "drawable", getPackageName()),(int) ThreadLocalRandom.current().nextInt(0, 5+1),attendeesMap)
            );
            images_lal.add(
                    new Image(getResources().getIdentifier("m" + i +"_lal", "drawable", getPackageName()),(int) ThreadLocalRandom.current().nextInt(0, 5+1),attendeesMap)
            );
            images_hal.add(
                    new Image(getResources().getIdentifier("m" + i +"_hal", "drawable", getPackageName()),(int) ThreadLocalRandom.current().nextInt(0, 5+1),attendeesMap)
            );

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
            topPhotoOne.setImageResource( images_selected.get(0).getResouceId());
            topPhotoOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedImage =  images_selected.get(0).getResouceId();
                    Intent clickedItemIntent = new Intent(ImageGridActivity.this , ClickedItemActivity.class);
                    clickedItemIntent.putExtra("index",0);
                    clickedItemIntent.putExtra("image",selectedImage);
                    clickedItemIntent.putExtra("likes",(Integer) images_selected.get(0).getLikes());
                    clickedItemIntent.putExtra("liked",images_selected.get(0).getLiked().get(getIntent().getStringExtra("userName")));
                    startActivityForResult(clickedItemIntent,CLICKED_EVENT_CODE);
                }
            });
            ImageView topPhotoTwo = findViewById(R.id.topPhoto2);
            topPhotoTwo.setImageResource((Integer) images_selected.get(1).getResouceId());
            topPhotoTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedImage = (Integer) images_selected.get(1).getResouceId();
                    Intent clickedItemIntent = new Intent(ImageGridActivity.this , ClickedItemActivity.class);
                    clickedItemIntent.putExtra("index",1);
                    clickedItemIntent.putExtra("image",selectedImage);
                    clickedItemIntent.putExtra("likes", images_selected.get(1).getLikes());
                    clickedItemIntent.putExtra("liked",images_selected.get(1).getLiked().get(getIntent().getStringExtra("userName")));
                    startActivityForResult(clickedItemIntent,CLICKED_EVENT_CODE);
                }
            });
            ImageView topPhotoThree = findViewById(R.id.topPhoto3);
            topPhotoThree.setImageResource((Integer) images_selected.get(2).getResouceId());
            topPhotoThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedImage = (Integer) images_selected.get(2).getResouceId();
                    Intent clickedItemIntent = new Intent(ImageGridActivity.this , ClickedItemActivity.class);
                    clickedItemIntent.putExtra("index",2);
                    clickedItemIntent.putExtra("image",selectedImage);
                    clickedItemIntent.putExtra("likes",(Integer) images_selected.get(2).getResouceId());
                    clickedItemIntent.putExtra("liked",images_selected.get(2).getLiked().get(getIntent().getStringExtra("userName")));
                    startActivityForResult(clickedItemIntent,CLICKED_EVENT_CODE);
                }
            });
        }
        customAdapter = new CustomAdapter(images_selected, this);
        gridView.setAdapter(customAdapter);
        TextView filteredNumberOfPhotosText = (TextView) findViewById(R.id.filteredNumberOfPhotosText);
        numberOfImagesToShow = images_selected.size();
        filteredNumberOfPhotosText.setText(numberOfImagesToShow + "/" + images_selected.size());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedImage = (Integer) images_selected.get(i).getResouceId();
                Intent clickedItemIntent = new Intent(ImageGridActivity.this , ClickedItemActivity.class);
                clickedItemIntent.putExtra("index",i);
                clickedItemIntent.putExtra("image",selectedImage);
                clickedItemIntent.putExtra("likes", images_selected.get(i).getLikes());
                clickedItemIntent.putExtra("liked",images_selected.get(i).getLiked().get(getIntent().getStringExtra("userName")));
                startActivityForResult(clickedItemIntent,CLICKED_EVENT_CODE);
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

        FloatingActionButton shareEvent = findViewById(R.id.shareEvent);
        shareEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareEventActivityIntent = new Intent(ImageGridActivity.this,ShareEvent.class);
                ArrayList<String> attendees = mService.getEventAttendees(getIntent().getStringExtra("eventName"));
                shareEventActivityIntent.putStringArrayListExtra("attendees", attendees);
                startActivityForResult(shareEventActivityIntent , SHARE_EVENT_CODE);

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
                Toast toast;
                if(ascendingSortedImages) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Images sorted by date (ascending)",
                            Toast.LENGTH_SHORT);
                }
                else {
                    toast = Toast.makeText(getApplicationContext(),
                            "Images sorted by date (descending)",
                            Toast.LENGTH_SHORT);
                }
                toast.show();
                ascendingSortedImages = !ascendingSortedImages;
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
        List<Image> new_images_selected = images_selected;
        if (resultCode==Activity.RESULT_OK && requestCode == FILTER_ACTIVITY_CODE) {
            filterApplied = data.getBooleanExtra("filterApplied", false);
            contributorSwitchIsChecked = data.getBooleanExtra("contributorSwitchIsChecked", false);
            timeSwitchIsChecked = data.getBooleanExtra("timeSwitchIsChecked", false);
            numberOfImagesToShow = data.getIntExtra("numberOfImagesToShow", images_selected.size());
            selectedUsers = data.getBooleanArrayExtra("selectedUsers");
            fromTimeHours = data.getIntExtra("fromTimeHours",0);
            toTimeHours = data.getIntExtra("toTimeHours",23);
            fromTimeMinutes = data.getIntExtra("fromTimeMinutes", 0);
            toTimeMinutes = data.getIntExtra("toTimeMinutes",59);
            fromDateInMillis = data.getLongExtra("fromDateInMillis",0);
            toDateInMillis = data.getLongExtra("toDateInMillis",0);
            new_images_selected = images_selected.subList(0, numberOfImagesToShow);
            TextView filteredNumberOfPhotosText = (TextView) findViewById(R.id.filteredNumberOfPhotosText);
            filteredNumberOfPhotosText.setText(numberOfImagesToShow + "/" + images_selected.size());
        }
        else if (requestCode==200 && data!=null) {
            switch (getIntent().getStringExtra("eventName")) {
                case "Graduation":
                    new_images_selected.add(0,new Image(R.drawable.m13_grad,0,attendeesMap));
                    break;
                case "Halloween":
                    new_images_selected.add(0,new Image(R.drawable.m13_hal,0,attendeesMap));
                    break;
                case "Birthday":
                    new_images_selected.add(0,new Image(R.drawable.m13_bir,0,attendeesMap));
                    break;
                case "Lakers Game":
                    new_images_selected.add(0,new Image(R.drawable.m13_lal,0,attendeesMap));
                    break;
            }
            TextView filteredNumberOfPhotosText = (TextView) findViewById(R.id.filteredNumberOfPhotosText);
            filteredNumberOfPhotosText.setText(new_images_selected.size() + "/" + new_images_selected.size());
            filterApplied = false;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Image uploaded successfully",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        else if (requestCode==SHARE_EVENT_CODE && data!=null) {
            ArrayList<String> invitedUsers = data.getStringArrayListExtra("attendees");
            if(!invitedUsers.isEmpty())
            {
                for(String user : invitedUsers) {
                    try {
                        if(!user.equals(getIntent().getStringExtra("userName"))) {
                            Event event = mService.getEvent(getIntent().getStringExtra("eventName"));
                            mService.addInvite(event, user);
                        }

                    } catch (Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        }
        else if (requestCode==CLICKED_EVENT_CODE && data!=null)
        {
            int selectedImageIndex = data.getIntExtra("imageIndex",0);
            int resourceId = data.getIntExtra("image",0);
            int likes = data.getIntExtra("likes",0);
            boolean liked = data.getBooleanExtra("liked",false);
            HashMap<String,Boolean> map = new HashMap<>();
            for(Map.Entry<String,Boolean> entry :  new_images_selected.get(selectedImageIndex).getLiked().entrySet())
            {
                map.put(entry.getKey(),entry.getValue());
            }
            map.put(getIntent().getStringExtra("userName"),liked);
            Image img = new Image(resourceId,likes,map);
            new_images_selected.remove(selectedImageIndex);
            new_images_selected.add(selectedImageIndex,img);


        }

        customAdapter = new CustomAdapter(new_images_selected, this);
        gridView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        generateInitialView();
    }


    public class CustomAdapter extends BaseAdapter {

        private List<Image> imagesPhoto;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(List<Image> imagesPhoto, Context context) {
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
            imageView.setImageResource((Integer) imagesPhoto.get(i).getResouceId());
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
            ArrayList<String> attendees = mService.getEventAttendees(getIntent().getStringExtra("eventName"));
            for(String attendee : attendees)
            {
                attendeesMap.put(attendee,false);
            }
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
            contributorSwitchIsChecked = false;
            timeSwitchIsChecked = false;
            selectedUsers = new boolean[5];
            fromDateInMillis = convertDateToTimeInMillis(initialDate);
            toDateInMillis = convertDateToTimeInMillis(finalDate);
            fromTimeHours = 0;
            fromTimeMinutes = 0;
            toTimeHours = 23;
            toTimeMinutes = 59;
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
