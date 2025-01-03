package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.CharityUsersList;
import com.example.fyp_khanabachao.databinding.ActivityConsumerDashboardBinding;
import com.example.fyp_khanabachao.databinding.ActivityMainActivity2chooseuserBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity2chooseuser extends DrawerBaseActivity
{
ActivityMainActivity2chooseuserBinding activityMainActivity2chooseuserBinding;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;
    String mUID;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_activity2chooseuser);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityMainActivity2chooseuserBinding = ActivityMainActivity2chooseuserBinding.inflate(getLayoutInflater());
        setContentView(activityMainActivity2chooseuserBinding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
//--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.consumernavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.consumernavid:
                        startActivity(new Intent(getApplicationContext(),ConsumerDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.providernavid:
                        startActivity(new Intent(getApplicationContext(),FoodUpload_01.class));
                        overridePendingTransition(0,0);
                        return true;
                }
           return false;
            }
        });
//--------------------------------------------------------
    checkUserStatus();
        //updateToken

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }


    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            mUID = user.getUid();
            //save uid of currently signed in user in shared preferences

        }
        else{
            startActivity(new Intent(MainActivity2chooseuser.this,consumerLOGIN.class));

        }
    }

    public void providerbtnClick(View view) {   //function called in xml on provider image click
       // Intent intent = new Intent(MainActivity2chooseuser.this, FoodUpload_01.class);
        //Intent intent = new Intent(MainActivity2chooseuser.this, AdminAllUserList.class);
       // startActivity(intent);
    }


    public void consumerbtnClick(View view) {   //function called in xml on consumer image click
    Intent intent = new Intent(MainActivity2chooseuser.this,ConsumerDashboard.class);
    startActivity(intent);
    }

    public void blockClick(View view)
    {
        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

    }

    public void allItemsClick(View view) {
        Intent intent = new Intent(MainActivity2chooseuser.this,ConsumerDashboard.class);
       //Intent intent = new Intent(MainActivity2chooseuser.this, AdminAllUserList.class);
                startActivity(intent);
    }

    public void homeMadeViewBtn(View view) {
        Intent intent = new Intent(MainActivity2chooseuser.this,HomeItems.class);
        startActivity(intent);
    }

    public void restaurantMadeViewBtn(View view) {
        Intent intent = new Intent(MainActivity2chooseuser.this,RestaurantItems.class);
        startActivity(intent);
    }

    public void moveToCharity(View view) {
        Intent intent = new Intent(MainActivity2chooseuser.this, fooddrive.class);
        startActivity(intent);
    }
}