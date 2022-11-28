package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

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
    boolean liked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        likes = (int) ThreadLocalRandom.current().nextInt(0, 5+1);

        imageView = findViewById(R.id.imageView);
        upvote = findViewById(R.id.upvote);
        upvote.setText(String.valueOf(likes) + " Likes");
        upvote.setIconResource(R.drawable.ic_heart_thin_icon);

        if (getIntent().getExtras() != null) {
            int selectedImage = getIntent().getIntExtra("image", 0);
            imageView.setImageResource(selectedImage);

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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}