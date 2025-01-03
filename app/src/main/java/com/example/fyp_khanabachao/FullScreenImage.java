package com.example.fyp_khanabachao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ImageView fullScreenIV = findViewById(R.id.fullscreenimageView);
        Intent callingintent = getIntent();
        if(callingintent != null){
            Uri imageURI = callingintent.getData();
            if(imageURI !=null && fullScreenIV!=null){
                Glide.with(this).load(imageURI).into(fullScreenIV);
            }
            else{
                Toast.makeText(this,"Cant load immage",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Cant load immage",Toast.LENGTH_LONG).show();
        }
       /* Zoomy.Builder builder = new Zoomy.Builder(this)
                .target(fullScreenIV)
                .animateZooming(false)
                .enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(getApplicationContext(),"image clicked", Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
}