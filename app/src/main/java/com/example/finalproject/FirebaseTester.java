package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseTester extends AppCompatActivity {
    Button add;
    TextView output;
    EditText path, content;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_tester);

        add = findViewById(R.id.add);
        output = findViewById(R.id.text);
        path = findViewById(R.id.firebase_path);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void
            onClick(View v) {

                // Read from the database
            }
            }
            );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test");
        myRef = database.getReference("test/" + path.getText().toString());

        content = findViewById(R.id.firebase_path_contents);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }
}