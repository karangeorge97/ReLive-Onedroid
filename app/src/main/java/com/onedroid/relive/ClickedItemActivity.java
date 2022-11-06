package com.onedroid.relive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
public class ClickedItemActivity extends AppCompatActivity {

    ImageView imageView;
    //TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_item);

        imageView = findViewById(R.id.imageView);
        //textView = findViewById(R.id.tvName);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            //String selectedName = intent.getStringExtra("name");
            int selectedImage = intent.getIntExtra("image", 0);

            //textView.setText(selectedName);
            imageView.setImageResource(selectedImage);
        }
        //set values;
    }
}