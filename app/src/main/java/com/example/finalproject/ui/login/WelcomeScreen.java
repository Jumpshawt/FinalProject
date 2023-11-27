package com.example.finalproject.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

public class WelcomeScreen extends AppCompatActivity{

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setTheme(R.style.Theme_FinalProject_NoActionBar);
        setContentView(R.layout.activity_welcome);

        Button skip = (Button) findViewById(R.id.register_skip_button);
        Button register = (Button) findViewById(R.id.register_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterReal.class);
                startActivity(i);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            Toast.makeText(getBaseContext(), "nap time is over", Toast.LENGTH_SHORT).show();
                            Thread.sleep(5000);

                            Intent i = new Intent(WelcomeScreen.this, MainActivity.class);
                            startActivity(i);
                        }
                        catch (InterruptedException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                };


            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.getWindow().setStatusBarContrastEnforced(true);
        }


        VideoView videoView = (VideoView)findViewById(R.id.videoView4);

        Uri uri=Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.solid);

        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

    }
}