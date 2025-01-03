package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.example.fyp_khanabachao.databinding.ActivityFoodDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityFoodDetailPublicBinding;
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

public class FoodDetailPublic extends DrawerBaseActivity {

ActivityFoodDetailPublicBinding activityFoodDetailPublicBinding;

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
    String hisUID;

    String Foodstatus="";
    String key="";
    String imageurl="";
    String Requesterid="";
    String Uploaderid="";
    String Uploadername="";
  //  String Requestername="";

    DatabaseReference databaseReference,referencee;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;
    ProgressDialog progressDialog;

    //rating
    float fetchRating;
    String userrating;
    TextView userratingtxt;
    String uidd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_public);
        activityFoodDetailPublicBinding= ActivityFoodDetailPublicBinding.inflate(getLayoutInflater());
        setContentView(activityFoodDetailPublicBinding.getRoot());

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();
        uidd = firebaseUser.getUid();



        //------------------------------- bottom nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.senderreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.senderreqnavid:
                        startActivity(new Intent(getApplicationContext(),SenderRequestDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.senderaccnavid:
                        startActivity(new Intent(getApplicationContext(),SenderAcceptDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //-------------------------------

        progressDialog = new ProgressDialog(this);
       // progressDialog.setMessage("Uploading ....");
        progressDialog.show();



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

        if(mbundle!=null)
        {
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

            // for sender request
            fooddescname2=mbundle.getString("name");
            fooddescdetail2=mbundle.getString("description");
            fooddescquantity2=mbundle.getString("quantity");
            fooddescexpiry2=mbundle.getString("expiry");
            fooddescsource2=mbundle.getString("source");
            fooddescdelivery2=mbundle.getString("delivery");
            fooddescprice2=mbundle.getString("price");
            fooddescaddress2=mbundle.getString("address");
            fooddesccity2=mbundle.getString("city");
            Foodstatus=mbundle.getString("foodstatus");

            //for update and foodrequest
            Uploaderid=mbundle.getString("uploaderid");
            key=mbundle.getString("keyvalue");
            imageurl = mbundle.getString("image");
            hisUID = mbundle.getString("userId");
            Requesterid=mbundle.getString("requesterid");
            Uploadername=mbundle.getString("uploadername");
           // Requestername = mbundle.getString("requestername");
            progressDialog.dismiss();
        }

        //id of food requester
        Requesterid = firebaseUser.getUid().toString();
       // Requestername = firebaseUser.getDisplayName().toString();
       // Log.d("bn","value id :: "+Requesterid);
        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food").child(key);

        // -------------rating -------------------------
        userratingtxt = findViewById(R.id.userratingid);
        referencee = FirebaseDatabase.getInstance().getReference("users");

        // Toast.makeText(FoodDetailPublic.this,"id: "+ uidd,Toast.LENGTH_SHORT).show();

        referencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                fetchRating = snapshot.child(Uploaderid).child("rating").getValue(Float.class);
                //float rating= Float.parseFloat(a);
                if(fetchRating == 0)
                {
                    userrating = "Not Rated";
                }
                else
                {
                    userrating = Float.toString(fetchRating);
                }
                userratingtxt.setText(userrating);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  Log.d(TAG,"Consumer profile view : " + error.getMessage());
            }});
    }
    /*public void OpenChat(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",Uploaderid);
        i.putExtra("hisName",Uploadername);

        activity.startActivity(i);
    }*/


    public void btnMoveSendLocationPg(View view)
    {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        Intent i = new Intent(activity, SendNeedyLocationActivity.class);
        i.putExtra("upUID",Uploaderid);
        i.putExtra("upName",Uploadername);

        activity.startActivity(i);
    }

    public void btnRequestFood(View view)
    {
        Foodstatus = "requested";

        FoodDataModelClass foodDataModelClass = new FoodDataModelClass(fooddescname2,fooddescdetail2,fooddescprice2,
                fooddescquantity2,fooddescexpiry2,imageurl,Uploaderid,fooddescsource2,fooddescdelivery2,fooddescaddress2,fooddesccity2,
                Foodstatus,key,Requesterid,Uploadername);


        databaseReference.setValue(foodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(FoodDetailPublic.this, "Food Request Sent", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(FoodDetailPublic.this, "Food Request ERROR : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(FoodDetailPublic.this,"Food Request ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }

    public void btnCancelFoodRequest(View view)
    {
        Foodstatus = "na";

        FoodDataModelClass foodDataModelClass = new FoodDataModelClass(fooddescname2,fooddescdetail2,fooddescprice2,fooddescquantity2,
                fooddescexpiry2,imageurl,Uploaderid,fooddescsource2,fooddescdelivery2,fooddescaddress2,fooddesccity2,Foodstatus,key,Requesterid,
                Uploadername);


        databaseReference.setValue(foodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(FoodDetailPublic.this, "Food Request Cancelled", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(FoodDetailPublic.this, "Food Cancellation ERROR : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(FoodDetailPublic.this,"Food Cancellation ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }


    public void openChatFoodSender(View view) {
        String add="";
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",Uploaderid);
        i.putExtra("hisName",Uploadername);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}