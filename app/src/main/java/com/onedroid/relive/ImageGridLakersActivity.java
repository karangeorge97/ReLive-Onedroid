package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onedroid.relive.databinding.ActivityImageGridBinding;
import com.onedroid.relive.databinding.ActivityMainBinding;
import com.onedroid.relive.service.AccountService;

public class ImageGridLakersActivity extends AppCompatActivity {

    private ActivityImageGridBinding binding;
    AccountService mService;
    boolean mBound = false;

    GridView gridView;

    //    String[] names = {"image1","image2","image3","image4","image5","image6","image7", "image8",
//                        "image9", "image10", "image11", "image12"};
    int[] images_lal = {R.drawable.m1_lal, R.drawable.m2_lal, R.drawable.m3_lal, R.drawable.m4_lal,
            R.drawable.m5_lal, R.drawable.m6_lal, R.drawable.m7_lal, R.drawable.m8_lal, R.drawable.m9_lal,
            R.drawable.m10_lal, R.drawable.m11_lal, R.drawable.m12_lal, R.drawable.m13_lal, R.drawable.m14_lal,
            R.drawable.m15_lal, R.drawable.m16_lal, R.drawable.m17_lal, R.drawable.m18_lal, R.drawable.m19_lal,
            R.drawable.m20_lal, R.drawable.m21_lal};


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

        CustomAdapter customAdapter = new CustomAdapter(images_lal, this);//graduation
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedImage = images_lal[i];

                startActivity(new Intent(ImageGridLakersActivity.this,ClickedItemActivity.class).putExtra("image",selectedImage));
            }
        });
    }
    public class CustomAdapter extends BaseAdapter {

        private int[] imagesPhoto;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(int[] imagesPhoto, Context context) {
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagesPhoto.length;
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
            imageView.setImageResource(imagesPhoto[i]);
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
            generateInitialView();
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
        if(mBound) generateInitialView();
    }

    private void  generateInitialView()
    {
        TextView topLabel = (TextView) findViewById(R.id.eventName);
        topLabel.setText(getIntent().getStringExtra("eventName"));

    }


}

