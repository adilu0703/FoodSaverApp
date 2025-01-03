package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp_khanabachao.databinding.ActivityAdminUserListDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityCharityUserListDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminUserListDetail extends DrawerBaseActivityAdmin {

    ActivityAdminUserListDetailBinding activityAdminUserListDetailBinding;
    TextView name,username,contact,rating,usertype,email;
    float r;
    String uid,namee;

    DatabaseReference referencee;
    float fetchRating;
    String userrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list_detail);

        activityAdminUserListDetailBinding= ActivityAdminUserListDetailBinding.inflate(getLayoutInflater());
        setContentView(activityAdminUserListDetailBinding.getRoot());

        name = findViewById(R.id.nameid);
        email = findViewById(R.id.emailid);
        username = findViewById(R.id.usernameid);
        contact = findViewById(R.id.contactid);
        rating = findViewById(R.id.userratingid);
        usertype = findViewById(R.id.usertypeid);


        Bundle mbundle = getIntent().getExtras();
        if(mbundle!=null)
        {
            name.setText(mbundle.getString("name"));
            username.setText(mbundle.getString("username"));
            contact.setText(mbundle.getString("contact"));
            email.setText(mbundle.getString("email"));
            usertype.setText(mbundle.getString("usertype"));
            uid=mbundle.getString("uid");
            namee=mbundle.getString("name");

        }



        //-----rating--------------

        referencee = FirebaseDatabase.getInstance().getReference("users");

        // Toast.makeText(FoodDetailPublic.this,"id: "+ uidd,Toast.LENGTH_SHORT).show();

        referencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                fetchRating = snapshot.child(uid).child("rating").getValue(Float.class);
                //float rating= Float.parseFloat(a);
                if(fetchRating == 0)
                {
                    userrating = "Not Rated";
                }
                else
                {
                    userrating = Float.toString(fetchRating);
                }
                rating.setText(userrating);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  Log.d(TAG,"Consumer profile view : " + error.getMessage());
            }});

    }



    public void openChatAdmin(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add ="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",uid);
        i.putExtra("hisName",namee);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}