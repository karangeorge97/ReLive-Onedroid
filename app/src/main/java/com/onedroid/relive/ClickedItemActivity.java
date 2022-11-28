package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.concurrent.ThreadLocalRandom;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClickedItemActivity extends AppCompatActivity {

    ImageView imageView;
    ExtendedFloatingActionButton upvote;
    int likes;
    int imageIndex;
    int image;
    boolean liked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        likes = getIntent().getIntExtra("likes",0);
        imageIndex = getIntent().getIntExtra("index",0);
        image = getIntent().getIntExtra("image",0);



        imageView = findViewById(R.id.imageView);
        upvote = findViewById(R.id.upvote);
        upvote.setText(String.valueOf(likes) + " Likes");
        upvote.setIconResource(R.drawable.ic_heart_thin_icon);

        if (getIntent().getExtras() != null) {
            imageView.setImageResource(image);

        }

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!liked) {
                    upvote.setIconResource(R.drawable.ic_heart_icon);
                    likes++;
                    upvote.setText(String.valueOf(likes) + " Likes");
                }
                else {
                    upvote.setIconResource(R.drawable.ic_heart_thin_icon);
                    likes--;
                    upvote.setText(String.valueOf(likes) + " Likes");
                }
                liked = !liked;
            }
        });
        //set values;
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

    @Override
    public void onBackPressed() {
        Intent imageGridActivitityIntent = new Intent();
        imageGridActivitityIntent.putExtra("imageIndex",imageIndex);
        imageGridActivitityIntent.putExtra("image",image);
        imageGridActivitityIntent.putExtra("likes",likes);
        setResult(Activity.RESULT_OK, imageGridActivitityIntent);
        finish();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}