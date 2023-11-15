package com.example.finalproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import java.util.PriorityQueue;

public class EventInfoScreen extends AppCompatActivity {
    ImageView eventImage;
    TextView name,location,description, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("shit","balls3");

        Log.d("STATETHING", "attempt");
        setContentView(R.layout.activity_event_info);
        eventImage = findViewById(R.id.full_image);
        name = findViewById(R.id.full_title);
        location = findViewById(R.id.full_location);
        description=findViewById(R.id.full_description);

        time = findViewById(R.id.full_time);

        eventImage.setImageResource(getIntent().getIntExtra("image",0));
        name.setText(getIntent().getStringExtra("name"));
        location.setText(getIntent().getStringExtra("location"));
        time.setText(getIntent().getStringExtra("time"));
        description.setText(getIntent().getStringExtra("description"));
    }
}
