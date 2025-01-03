package com.example.fyp_khanabachao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.CharityUsersList;
import com.example.fyp_khanabachao.databinding.ActivityFoodDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityFooddriveBinding;

public class fooddrive extends DrawerBaseActivity {
    ActivityFooddriveBinding activityFooddriveBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddrive);
        activityFooddriveBinding = ActivityFooddriveBinding.inflate(getLayoutInflater());
        setContentView(activityFooddriveBinding.getRoot());
    }

    public void contactCharityBtn(View view) {
        Intent intent = new Intent(fooddrive.this, CharityUsersList.class);
        startActivity(intent);
    }

    public void foodPrepBtn(View view)
    {
        Intent intent = new Intent(fooddrive.this, UploadPost.class);
        startActivity(intent);
    }
}