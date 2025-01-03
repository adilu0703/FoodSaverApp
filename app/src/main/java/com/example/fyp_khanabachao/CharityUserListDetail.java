package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fyp_khanabachao.databinding.ActivityCharityUserListDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CharityUserListDetail extends DrawerBaseActivity
{
    ActivityCharityUserListDetailBinding activityCharityUserListDetailBinding;
    TextView cname,cusername,cphone,cemail;
    String CuserID,Cusername;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_user_list_detail);
        activityCharityUserListDetailBinding= ActivityCharityUserListDetailBinding.inflate(getLayoutInflater());
        setContentView(activityCharityUserListDetailBinding.getRoot());


        cname= findViewById(R.id.charitynamedeid);
        cusername = findViewById(R.id.charityuserdenameid);
        cphone = findViewById(R.id.charitycontactdeid);
        cemail = findViewById(R.id.charityemaildeid);


        Bundle mbundle = getIntent().getExtras();

        if(mbundle!=null)
        {
            cname.setText(mbundle.getString("name"));
            cusername.setText(mbundle.getString("username"));
            cphone.setText(mbundle.getString("contact"));
            cemail.setText(mbundle.getString("email"));
            CuserID=mbundle.getString("uid");
            Cusername=mbundle.getString("name");

        }

    }


    public void openChatCharity(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add ="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",CuserID);
        i.putExtra("hisName",Cusername);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}