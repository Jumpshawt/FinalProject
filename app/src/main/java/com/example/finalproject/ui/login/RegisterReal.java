package com.example.finalproject.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.ui.GeordieFirebaseMethods;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterReal extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    private static final String USERNAME_KEY = "username_key";
    private static final String PASSWORD_KEY = "password_key";
    private EditText usernameBox, passwordBox;
    TextView error;
    Button register, login;
    SharedPreferences sharedPreferences;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        error = findViewById(R.id.error_register_screen);
        register = findViewById(R.id.user_register_button);
        login = findViewById(R.id.user_login_button);
        usernameBox = findViewById(R.id.username_entry);
        passwordBox = findViewById(R.id.password_entry);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }
    private void register()
    {
        //check if the user doesn't exist already
        boolean valid = !userExists(usernameBox.toString());
        //if user doesn't exist then create new
        if (valid)
        {
            createNewUser();
        }
        else
        {
            error.setText("Error: User already exists");
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
        }
    }


    private void createNewUser() {
        username = usernameBox.getText().toString();
        password = passwordBox.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSWORD_KEY, password);

        editor.apply();

        Toast.makeText(this, "user" + sharedPreferences.getString(USERNAME_KEY, null) +  "has been created", Toast.LENGTH_SHORT).show();
        //switch activity to the home.
        Intent i = new Intent(RegisterReal.this, MainActivity.class);
        startActivity(i);
    }

    private void login()
    {
        boolean valid = checkUsernamePassword(usernameBox.toString(), passwordBox.toString());
        if (valid)
        {

            Intent i = new Intent(RegisterReal.this, MainActivity.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(this, "Error: Username/Password is incorrect", Toast.LENGTH_SHORT).show();
            error.setText("Error: Username/password is incorrect");
        }
    }

    private boolean checkUsernamePassword(String username, String password)
    {
        return true;
    }

    private boolean userExists(String username)
    {
        return false;
    }

}