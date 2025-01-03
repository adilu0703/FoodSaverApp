package com.example.fyp_khanabachao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp_khanabachao.databinding.ActivityAdminAllUserListBinding;
import com.example.fyp_khanabachao.databinding.ActivityDashboardAdminBinding;

public class dashboardAdmin extends DrawerBaseActivityAdmin {
    ActivityDashboardAdminBinding activityDashboardAdminBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        activityDashboardAdminBinding= ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardAdminBinding.getRoot());
    }

    public void allUsersClick(View view)
    {
        Intent intent= new Intent(dashboardAdmin.this,AdminAllUserList.class);
        startActivity(intent);

    }

    public void lowRatedUser(View view) {
        Intent intent= new Intent(dashboardAdmin.this,AdminWarningUserList.class);
        startActivity(intent);
    }
}