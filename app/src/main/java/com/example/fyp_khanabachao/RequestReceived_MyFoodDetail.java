package com.example.fyp_khanabachao;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyp_khanabachao.databinding.ActivityRequestAcceptedMyFoodDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityRequestReceivedDashboardBinding;
import com.example.fyp_khanabachao.databinding.ActivityRequestReceivedMyFoodDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class RequestReceived_MyFoodDetail extends DrawerBaseActivity
{
    ActivityRequestReceivedMyFoodDetailBinding activityRequestReceivedMyFoodDetailBinding;
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

    String fooddescname2;
    String fooddescprice2;
    String fooddescsource2;
    String fooddescexpiry2;
    String fooddescdelivery2;
    String fooddescquantity2;
    String fooddescaddress2;
    String fooddesccity2;
    String fooddescdetail2;

    String key="";
    String imageurl="";
    String FoodStatus="";
    String Requesterid="";
    String Uploaderid="";
    String Uploadername="";

    DatabaseReference databaseReference;
    DatabaseReference reference;
    FirebaseDatabase rootnodee;
    String requestername;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_received_my_food_detail);
        activityRequestReceivedMyFoodDetailBinding= ActivityRequestReceivedMyFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(activityRequestReceivedMyFoodDetailBinding.getRoot());
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

            // for cancel/accept request
            fooddescname2=mbundle.getString("name");
            fooddescdetail2=mbundle.getString("description");
            fooddescquantity2=mbundle.getString("quantity");
            fooddescexpiry2=mbundle.getString("expiry");
            fooddescsource2=mbundle.getString("source");
            fooddescdelivery2=mbundle.getString("delivery");
            fooddescprice2=mbundle.getString("price");
            fooddescaddress2=mbundle.getString("address");
            fooddesccity2=mbundle.getString("city");

            //for update
            key = mbundle.getString("keyvalue");
            imageurl = mbundle.getString("image");

            Uploaderid = mbundle.getString("uploaderid");
            FoodStatus = mbundle.getString("foodstatus");
            Requesterid = mbundle.getString("requesterid");
          //  Requestername=mbundle.getString("requestername");
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
//--------------------------------------------------------
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                requestername = snapshot.child(Requesterid).child("name").getValue(String.class);
                Log.d(TAG,"name:"+requestername);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"Consumer profile view : " + error.getMessage());
            }});
    }

    public void btnAcceptRequest(View view)
    {
        FoodStatus = "accepted";

        FoodDataModelClass foodDataModelClass = new FoodDataModelClass(fooddescname2,fooddescdetail2,fooddescprice2,fooddescquantity2,
                fooddescexpiry2,imageurl,Uploaderid,fooddescsource2,fooddescdelivery2,fooddescaddress2,fooddesccity2,FoodStatus,key,Requesterid
        ,Uploadername);

        databaseReference.setValue(foodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(RequestReceived_MyFoodDetail.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RequestReceived_MyFoodDetail.this, "Request Acceptance Failure : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(RequestReceived_MyFoodDetail.this,"Request Accepted ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }

    public void btnCancelRequest(View view)
    {
        FoodStatus = "na";

        FoodDataModelClass foodDataModelClass = new FoodDataModelClass(fooddescname2,fooddescdetail2,fooddescprice2,fooddescquantity2,
                fooddescexpiry2,imageurl,Uploaderid,fooddescsource2,fooddescdelivery2,fooddescaddress2,fooddesccity2,FoodStatus,key,Requesterid,
                Uploadername);

        databaseReference.setValue(foodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(RequestReceived_MyFoodDetail.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RequestReceived_MyFoodDetail.this, "Request Cancellation Failure : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(RequestReceived_MyFoodDetail.this,"Request cancellation ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }


    public void openChatFoodReceiver(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",Requesterid);
        i.putExtra("hisName",requestername);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}