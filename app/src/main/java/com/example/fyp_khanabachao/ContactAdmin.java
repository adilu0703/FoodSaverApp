package com.example.fyp_khanabachao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp_khanabachao.databinding.ActivityContactAdminBinding;
import com.example.fyp_khanabachao.databinding.ActivityUploadPostBinding;

public class ContactAdmin extends DrawerBaseActivity {
    ActivityContactAdminBinding activityContactAdminBinding;
String CuserID,Cusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_admin);
        activityContactAdminBinding = ActivityContactAdminBinding.inflate(getLayoutInflater());
        setContentView(activityContactAdminBinding.getRoot());


    }

    public void openChatWithAdmin(View view) {
        String add ="";
        CuserID="nmahRSb4vKedibYjwJv9iQ5KgQy2";
        Cusername="Admin";
        Intent i = new Intent(ContactAdmin.this, ChatActivity.class);
        i.putExtra("hisUid",CuserID);
        i.putExtra("hisName",Cusername);
        i.putExtra("add",add);
        startActivity(i);
    }
}