package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.fyp_khanabachao.databinding.ActivitySendNeedyLocationBinding;
import com.example.fyp_khanabachao.databinding.ActivitySenderRequestDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SendNeedyLocationActivity extends DrawerBaseActivity
{   ActivitySendNeedyLocationBinding activitySendNeedyLocationBinding;
    Button btnLocation ;
    String add,UPname,UPid;
    TextInputLayout tv1,tv2;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_needy_location);
        activitySendNeedyLocationBinding = ActivitySendNeedyLocationBinding.inflate(getLayoutInflater());
        setContentView(activitySendNeedyLocationBinding.getRoot());

        btnLocation = findViewById(R.id.getlocationid);

        tv1= findViewById(R.id.addressid);

        Intent intent = getIntent();
        UPid = intent.getStringExtra("upUID");
        UPname = intent.getExtras().get("upName").toString();

        //initialize and creating instance of fusedlocationproviderclient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check permission
                if(ActivityCompat.checkSelfPermission(SendNeedyLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    //when permission granted
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            // initialize location object
                            Location location = task.getResult();
                            if(location!=null)
                            {
                                try {
                                    //initialize geocoder
                                    Geocoder geocoder = new Geocoder(SendNeedyLocationActivity.this, Locale.getDefault());
                                    //initialize adress list
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                                   // add=addresses.toString();
                                    add= addresses.get(0).getAddressLine(0);
                                    //setting addresses in txtviews
                                    tv1.getEditText().setText( addresses.get(0).getAddressLine(0));


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                else
                {
                    ActivityCompat.requestPermissions(SendNeedyLocationActivity.this,new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }



    public void openChatSendNeedyLoc(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("add",add);
        i.putExtra("hisUid",UPid);
        i.putExtra("hisName",UPname);


        activity.startActivity(i);
    }
}