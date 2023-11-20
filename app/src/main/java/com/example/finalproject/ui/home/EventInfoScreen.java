package com.example.finalproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.FirebaseTester;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.ui.GeordieFirebaseMethods;


public class EventInfoScreen extends AppCompatActivity {
    ImageView eventImage;
    TextView name,location,description, time;
    EventModal eventInfo;
    Button delete, edit;
    String id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        //get the EventInfo
        eventInfo = getIntent().getParcelableExtra("object");

        eventImage = findViewById(R.id.full_image);
        name = findViewById(R.id.full_title);
        location = findViewById(R.id.full_location);
        description=findViewById(R.id.full_description);
        time = findViewById(R.id.full_time);
        edit =findViewById(R.id.edit);
        eventImage.setImageResource(getIntent().getIntExtra("image",0));
        name.setText(eventInfo.name);
        location.setText(eventInfo.location);
        time.setText(getIntent().getStringExtra("time"));
        description.setText(eventInfo.description);
        delete = findViewById(R.id.deleteButton);
        Log.d("TAG", "Loaded a new event to info: " + "id is" + (eventInfo.getFireId()));

        id = eventInfo.getFireId();

        delete.setOnClickListener(v ->
        {
            GeordieFirebaseMethods.removeEventModalFromFirestore(id);
            finish();
        });
        edit.setOnClickListener(v ->
        {
            Intent intent = new Intent(EventInfoScreen.this, FirebaseTester.class);
            intent.putExtra("object",(Parcelable)eventInfo);
            EventInfoScreen.this.startActivity(intent);
        });


    }
}
