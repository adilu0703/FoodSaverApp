package com.example.fyp_khanabachao;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.example.fyp_khanabachao.databinding.ActivityPostFoodDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostFoodDetail extends DrawerBaseActivity {

    ActivityPostFoodDetailBinding activityPostFoodDetailBinding;
    TextView PLfooddescname;
    TextView PLfooddescetime;
    TextView PLfooddescquantity;
    TextView PLfooddescaddress;
    TextView PLfooddesccity;

    String key="";
    String hisUID,hisName,foodStatus,Requesterid,UploaderName;
    String imageurl="";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_food_detail);
        activityPostFoodDetailBinding= ActivityPostFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(activityPostFoodDetailBinding.getRoot());

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
        progressDialog.setMessage("Loading ....");
        progressDialog.show();


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
            key=mbundle.getString("keyvalue");
            imageurl = mbundle.getString("image");
            hisUID = mbundle.getString("ppuserId");
            foodStatus=mbundle.getString("foodstatus");
            Requesterid=mbundle.getString("requesterid");
            UploaderName=mbundle.getString("uploadername");
            progressDialog.dismiss();
        }
    }


    public void btnDeletePostFood(View view) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post Food");
        reference.child(key).removeValue();
        Toast.makeText(PostFoodDetail.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),UploadPost.class));

    }

    public void btnUpdatePostFood(View view) {
        Intent intent = new Intent(PostFoodDetail.this,PostUpdate.class);

        intent.putExtra("namep",PLfooddescname.getText().toString());
        intent.putExtra("quantityp",PLfooddescquantity.getText().toString());
        intent.putExtra("timep",PLfooddescetime.getText().toString());
        intent.putExtra("addressp",PLfooddescaddress.getText().toString());
        intent.putExtra("cityp",PLfooddesccity.getText().toString());
        intent.putExtra("keyp",key);
        intent.putExtra("foodstatusp",foodStatus);
        intent.putExtra("uploadernamep",UploaderName);
        intent.putExtra("requesteridp",Requesterid);

        startActivity(intent);

    }
}
