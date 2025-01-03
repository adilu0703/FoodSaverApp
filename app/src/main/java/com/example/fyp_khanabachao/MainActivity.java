package com.example.fyp_khanabachao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //for animations
    private static int SPLASHSCREEN = 3040;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView title, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // hide notif bar
        setContentView(R.layout.activity_main);


        //animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.topanimation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottomanimation);

        image = findViewById(R.id.imageViewTitle);
        title = findViewById(R.id.textViewTitle);
        slogan = findViewById(R.id.textViewSlogan);

        image.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(MainActivity.this,consumerLOGIN.class);
                startActivity(intent);
                finish();
            }
        },SPLASHSCREEN);


    }
}