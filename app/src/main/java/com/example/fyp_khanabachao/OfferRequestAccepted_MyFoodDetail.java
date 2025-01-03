package com.example.fyp_khanabachao;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp_khanabachao.databinding.ActivityOfferRequestAcceptedMyFoodDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityOfferRequestReceivedMyFoodDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OfferRequestAccepted_MyFoodDetail extends DrawerBaseActivity {

    ActivityOfferRequestAcceptedMyFoodDetailBinding activityOfferRequestAcceptedMyFoodDetailBinding;
    TextView PLfooddescname;
    TextView PLfooddescetime;
    TextView PLfooddescquantity;
    TextView PLfooddescaddress;
    TextView PLfooddesccity;


    String PLkey="";
    String PLFoodStatus="";
    String PLRequesterid="";
    String PLRequesterName="";
    String PLUploaderid="";
    String PLUploadername="";
    DatabaseReference reference;
    DatabaseReference databaseReference;
    FirebaseDatabase rootnodee;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_request_accepted_my_food_detail);
        activityOfferRequestAcceptedMyFoodDetailBinding= ActivityOfferRequestAcceptedMyFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(activityOfferRequestAcceptedMyFoodDetailBinding.getRoot());

        //--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.postreceiverreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.postreceiverreqnavid:
                        startActivity(new Intent(getApplicationContext(), OfferRequestReceivedDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.postreceiveraccnavid:
                        startActivity(new Intent(getApplicationContext(), OfferRequestAcceptedDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
//--------------------------------------------------------

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();
        rootnodee = FirebaseDatabase.getInstance();
        reference = rootnodee.getReference("users");


        PLfooddescname = findViewById(R.id.PLfooddescnameid);
        PLfooddescquantity= findViewById(R.id.PLfooddescquantityid);
        PLfooddescetime = findViewById(R.id.PLfoodestime);
        PLfooddescaddress = findViewById(R.id.PLfooddescaddressid);
        PLfooddesccity = findViewById(R.id.PLfooddesccityid);


        Bundle mbundle = getIntent().getExtras();

        if(mbundle!=null)
        {

            PLfooddescname.setText(mbundle.getString("name"));
            PLfooddescetime.setText(mbundle.getString("time"));
            PLfooddescquantity.setText(mbundle.getString("quantity"));
            PLfooddescaddress.setText(mbundle.getString("address"));
            PLfooddesccity.setText(mbundle.getString("city"));


            //for update
            PLkey = mbundle.getString("keyvalue");


            PLUploaderid = mbundle.getString("ppuserId");
            PLFoodStatus = mbundle.getString("foodstatus");
            PLRequesterid = mbundle.getString("requesterid");
            PLUploadername = mbundle.getString("uploadername");
            progressDialog.dismiss();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food").child(PLkey);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                PLRequesterName = snapshot.child(PLRequesterid).child("name").getValue(String.class);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"Consumer profile view : " + error.getMessage());
            }});
    }


    public void openChatPostAccepter(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",PLRequesterid);
        i.putExtra("hisName",PLRequesterName);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}