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

public class DrawerBaseActivityAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base_admin,null);
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
            case R.id.nav_HomeAdmin:
                startActivity(new Intent(this,dashboardAdmin.class));
                //overridePendingTransition(5,5);
                break;
            case R.id.nav_AllUsersAdmin:
                startActivity(new Intent(this,AdminAllUserList.class));
                //overridePendingTransition(0,0);
                break;
            case R.id.nav_WarnedUsersAdmin:
                startActivity(new Intent(this,AdminWarningUserList.class));
                //overridePendingTransition(0,0);
                break;
            case R.id.nav_adminchatlist:
                startActivity(new Intent(this,TestAdmin.class));
                //overridePendingTransition(0,0);
                break;

            case R.id.nav_adminLogout:
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