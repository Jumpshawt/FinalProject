package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalproject.ui.GeordieFirebaseMethods;
import com.example.finalproject.ui.home.EventModal;
import com.example.finalproject.ui.home.GeordieMethods;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;



public class FirebaseTester extends AppCompatActivity {
    public static final String TAG = "FirebaseTester";
    Button create_event, date_button;
    TextView output;
    EditText name, description, organizer, location;
    LocalTime time;
    EventModal modal;
    LocalDate date;
    private boolean editing;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_tester);
        create_event = findViewById(R.id.confirm_create);
        output = findViewById(R.id.text);
        name = findViewById(R.id.event_create);
        organizer = findViewById(R.id.org_create);
        location = findViewById(R.id.location_create);
        date_button = findViewById(R.id.date_create);
        description = findViewById(R.id.description_create);

        try {
            modal = getIntent().getParcelableExtra("object");
            setParametersFromModal(modal);
            create_event.setText("UpdateEvent");

            editing = true;
        }
        catch (Exception e)
        {
            editing = false;
        }

        date_button.setOnClickListener(v -> {
            initDatePicker();
        });

        create_event.setOnClickListener(v -> {
            if(valid())
            {

                //check if the user is editing, if they are, edit the event, otherwise, create a new one
                if(editing) {
                    EventModal e = modal;
                    e.setName(name.getText().toString());

                    e.setTime(time);
                    e.setDate(date);
                    e.setDescription(description.getText().toString());
                    e.setOrg(organizer.getText().toString());
                    e.setLocation(location.getText().toString());
                    GeordieFirebaseMethods.editEventModalOnFirestore(e);
                }
                else
                {
                    EventModal e = new EventModal();
                    e.setName(name.getText().toString());
                    e.setTime(time);
                    e.setDate(date);
                    e.setDescription(description.getText().toString());
                    e.setOrg(organizer.getText().toString());
                    e.setLocation(location.getText().toString());
                    GeordieFirebaseMethods.saveEventModalToFirestore(e);
                }

                Intent intent = new Intent(FirebaseTester.this, MainActivity.class);
                FirebaseTester.this.startActivity(intent);
            }
            else {
                Toast.makeText(FirebaseTester.this, "EventIsInvalid", Toast.LENGTH_SHORT).show();
            }
            // Read from the database
        }
        );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    private void setParametersFromModal(EventModal eventModal) {
        String testName, testOrg, testLocation, testDescription;

        name.setText(eventModal.getName());
        organizer.setText(eventModal.getOrg());
        location.setText(eventModal.getLocation());
        description.setText(eventModal.getDescription());
        date = eventModal.getDate();
        time = eventModal.getTime();
    }

    private boolean valid()
    {
        if(name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (organizer.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Organizer", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (location.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date == null) {
            Toast.makeText(this, "Missing Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (time == null) {
            Toast.makeText(this, "Missing Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void initDatePicker(){


        final LocalDate c = LocalDate.now();
        final int mYear = c.getYear();
        final int mMonth = c.getMonthValue() - 1;
        final int mDay = c.getDayOfMonth();

        final LocalTime t = LocalTime.now();
        final int mHour = t.getHour();
        final int mMinute = t.getMinute();
        TimePickerDialog timePickerDialog = new TimePickerDialog(FirebaseTester.this,
                new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        LocalTime of = LocalTime.of(hourOfDay, minute);
                        time = of;

                        Log.d(TAG, "onTimeSet() called with: view = [" + view + "], hourOfDay = [" + hourOfDay + "], minute = [" + minute + "]");
                    }
                },mHour,mMinute,false
        );

        // Date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(FirebaseTester.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display selected date in a toast message
                        date = LocalDate.of(year,monthOfYear + 1,dayOfMonth);
                        Log.d(TAG, "onDateSet() called with: view = [" + view + "], year = [" + year + "], monthOfYear = [" + new DateFormatSymbols().getMonths()[monthOfYear] + "], dayOfMonth = [" + dayOfMonth + "]");

                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }
    private void getEventModal()
    {
        modal.setDate(date);
        modal.setDescription(description.getText().toString());
        modal.setName(name.getText().toString());
        modal.setTime(time);
    }
}