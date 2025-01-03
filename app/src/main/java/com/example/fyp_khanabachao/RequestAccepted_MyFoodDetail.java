package com.example.fyp_khanabachao;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.example.fyp_khanabachao.databinding.ActivityRequestAcceptedMyFoodDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class RequestAccepted_MyFoodDetail extends DrawerBaseActivity
{
    ActivityRequestAcceptedMyFoodDetailBinding activityRequestAcceptedMyFoodDetailBinding;
    TextView fooddescdetail;
    ImageView foodimgdetail;

    TextView fooddescname;
    TextView fooddescprice;
    TextView fooddescsource;
    TextView fooddescexpiry;
    TextView fooddescdelivery;
    TextView fooddescquantity;
    TextView fooddescaddress;
    TextView fooddesccity;


    String key="";
    String imageurl="";
    String FoodStatus="";
    String Requesterid="";
    String Uploaderid="";
    String Uploadername="";
   // String Requestername="";

    DatabaseReference databaseReference;
    DatabaseReference reference;
    FirebaseDatabase rootnodee;
    String Requestername;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted_my_food_detail);
        activityRequestAcceptedMyFoodDetailBinding= ActivityRequestAcceptedMyFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(activityRequestAcceptedMyFoodDetailBinding.getRoot());
        rootnodee = FirebaseDatabase.getInstance();
        reference = rootnodee.getReference("users");

        foodimgdetail = findViewById(R.id.foodimgdetailid);
        fooddescname = findViewById(R.id.fooddescnameid);
        fooddescdetail = findViewById(R.id.fooddescdetailid);
        fooddescquantity= findViewById(R.id.fooddescquantityid);
        fooddescexpiry = findViewById(R.id.fooddescexpiryid);
        fooddescsource = findViewById(R.id.fooddescsourceid);
        fooddescdelivery = findViewById(R.id.fooddescdeliveryid);
        fooddescprice = findViewById(R.id.fooddescpriceid);
        fooddescaddress = findViewById(R.id.fooddescaddressid);
        fooddesccity = findViewById(R.id.fooddesccityid);


        Bundle mbundle = getIntent().getExtras();

        if(mbundle!=null) {
            //foodimgdetail.setImageResource(mbundle.getInt("image"));
            Glide.with(this).load(mbundle.getString("image")).into(foodimgdetail);

            fooddescname.setText(mbundle.getString("name"));
            fooddescdetail.setText(mbundle.getString("description"));
            fooddescquantity.setText(mbundle.getString("quantity"));
            fooddescexpiry.setText(mbundle.getString("expiry"));
            fooddescsource.setText(mbundle.getString("source"));
            fooddescdelivery.setText(mbundle.getString("delivery"));
            fooddescprice.setText(mbundle.getString("price"));
            fooddescaddress.setText(mbundle.getString("address"));
            fooddesccity.setText(mbundle.getString("city"));


            //for update
            key = mbundle.getString("keyvalue");
            imageurl = mbundle.getString("image");

            Uploaderid = mbundle.getString("uploaderid");
            FoodStatus = mbundle.getString("foodstatus");
            Requesterid = mbundle.getString("requesterid");
           // Requestername = mbundle.getString("requestername");
            Uploadername = mbundle.getString("uploadername");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food").child(key);

        //--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.receiverreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.receiverreqnavid:
                        startActivity(new Intent(getApplicationContext(),RequestReceivedDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.receiveraccnavid:
                        startActivity(new Intent(getApplicationContext(),RequestAcceptedDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
//---------------------------------------------------
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                 Requestername = snapshot.child(Requesterid).child("name").getValue(String.class);
                Log.d(TAG,"name:"+Requestername);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"Consumer profile view : " + error.getMessage());
            }});

    }


    public void openChatFoodAccepter(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",Requesterid);
        i.putExtra("hisName",Requestername);
        i.putExtra("add",add);

        activity.startActivity(i);
    }

    public void generateReceipt(View view)
    {
        Intent intent = new Intent(RequestAccepted_MyFoodDetail.this, paymentReceipt.class);
        intent.putExtra("keyv" , key );
        startActivity(intent);
    }
}