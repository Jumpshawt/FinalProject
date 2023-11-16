package com.example.finalproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;


public class EventInfoScreen extends AppCompatActivity {
    ImageView eventImage;
    TextView name,location,description, time;
    EventModal eventInfo;
    
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
        eventInfo = getIntent().getParcelableExtra("object");
        
        eventImage.setImageResource(getIntent().getIntExtra("image",0));
        name.setText(eventInfo.name);
        location.setText(eventInfo.location);
        //#todo fix the
        time.setText(getIntent().getStringExtra("time"));
        description.setText(eventInfo.description);
    }
}
