package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.example.fyp_khanabachao.databinding.ActivityFoodDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FoodDetail extends DrawerBaseActivity {
ActivityFoodDetailBinding activityFoodDetailBinding;
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
    String foodStatus="";
    String Requesterid="";
    String UploaderName="";
   // String RequesterName="";

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityFoodDetailBinding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(activityFoodDetailBinding.getRoot());
        // -----------------------------------------------
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
//--------------------------------------------

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ....");
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

            //for update
            key=mbundle.getString("keyvalue");
            imageurl = mbundle.getString("image");

            foodStatus=mbundle.getString("foodstatus");
            Requesterid=mbundle.getString("requesterid");
            UploaderName=mbundle.getString("uploadername");
            //RequesterName=mbundle.getString("requestername");

             progressDialog.dismiss();
        }

    }

    public void btnDeleteFood(View view)    //btn
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Public Food");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(imageurl);
         // to delete at firebase storage too
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                reference.child(key).removeValue();
                Toast.makeText(FoodDetail.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyFoodUpload.class));
            }
        });
    }

    public void btnUpdateFood(View view)   //btn
    {
        Intent intent = new Intent(FoodDetail.this,updateMyFood.class);

        intent.putExtra("nameu",fooddescname.getText().toString());
        intent.putExtra("detailu",fooddescdetail.getText().toString());
        intent.putExtra("quantityu",fooddescquantity.getText().toString());
        intent.putExtra("expiryu",fooddescexpiry.getText().toString());
        intent.putExtra("sourceu",fooddescsource.getText().toString());
        intent.putExtra("deliveryu",fooddescdelivery.getText().toString());
        intent.putExtra("priceu",fooddescprice.getText().toString());
        intent.putExtra("addressu",fooddescaddress.getText().toString());
        intent.putExtra("cityu",fooddesccity.getText().toString());
        intent.putExtra("oldimageurl",imageurl);
        intent.putExtra("keyu",key);

        intent.putExtra("foodstatus",foodStatus);
        intent.putExtra("requesterid",Requesterid);
        intent.putExtra("uploadername",UploaderName);

        startActivity(intent);

    }
}