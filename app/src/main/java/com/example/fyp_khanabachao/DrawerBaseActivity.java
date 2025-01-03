package com.example.fyp_khanabachao;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.CharityUsersList;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;


    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        NavigationView navigationView= drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_DrawerOpen,R.string.menu_DrawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      drawerLayout.closeDrawer(GravityCompat.START);
      switch (item.getItemId()){
          case R.id.nav_Home:
              startActivity(new Intent(this,MainActivity2chooseuser.class));
              //overridePendingTransition(5,5);
              break;
          case R.id.nav_profile:
              startActivity(new Intent(this,consumerProfile.class));
              //overridePendingTransition(5,5);
              break;
          case R.id.nav_UitemList:
              startActivity(new Intent(this,FoodUpload_01.class));
              //overridePendingTransition(0,0);
              break;
          case R.id.nav_UploadItems:
              startActivity(new Intent(this,MyFoodUpload.class));
              //overridePendingTransition(0,0);
              break;
          case R.id.nav_AllItems:
              startActivity(new Intent(this,ConsumerDashboard.class));
              //overridePendingTransition(0,0);
              break;
          case R.id.nav_RItems:
              startActivity(new Intent(this,RestaurantItems.class));
              //overridePendingTransition(0,0);
              break;
          case R.id.nav_HItems:
              startActivity(new Intent(this,HomeItems.class));
              //overridePendingTransition(0,0);
              break;
          case R.id.nav_showPosts:
              startActivity(new Intent(this,PostConsumerDashBoard.class));
              break;
          case R.id.nav_showmyPosts:
              startActivity(new Intent(this,PostListView.class));
              break;
          case R.id.nav_uploadposts:
              startActivity(new Intent(this,UploadPost.class));
              break;
          case R.id.nav_charity:
              startActivity(new Intent(this, CharityUsersList.class));
              break;
          case R.id.nav_chatadmin:
              startActivity(new Intent(this, ContactAdmin.class));
              break;
          case R.id.nav_chatlist:
              startActivity(new Intent(this, Test.class));
              break;
          case R.id.nav_notif:
              startActivity(new Intent(this, AppNotification.class));
              break;
          case R.id.nav_Logout:
              FirebaseAuth.getInstance().signOut();
              startActivity(new Intent(getApplicationContext(),consumerLOGIN.class));
              finish();
              break;
      }
        return false;
    }



   /* protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }

    }*/
}