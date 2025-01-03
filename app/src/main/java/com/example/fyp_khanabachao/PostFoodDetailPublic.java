package com.example.fyp_khanabachao;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.example.fyp_khanabachao.databinding.ActivityFoodDetailPublicBinding;
import com.example.fyp_khanabachao.databinding.ActivityPostFoodDetailBinding;
import com.example.fyp_khanabachao.databinding.ActivityPostFoodDetailPublicBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostFoodDetailPublic extends DrawerBaseActivity {
    ActivityPostFoodDetailPublicBinding activityPostFoodDetailPublicBinding;

    TextView PLfooddescname;
    TextView PLfooddescetime;
    TextView PLfooddescquantity;
    TextView PLfooddescaddress;
    TextView PLfooddesccity;


    String PLfooddescname2;
    String PLfooddescprice2;
    String PLfooddescsource2;
    String PLfooddescexpiry2;
    String PLfooddescquantity2;
    String PLfooddescaddress2;
    String PLfooddesccity2;
    String PLfooddescdetail2;

    String key="";
    String PLFoodStatus="";
    String PLRequesterid="";
    String PLRequesterName="";
    String PLUploaderid="";
    String PLUploadername="";
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase rootnodee;
    FirebaseAuth cauth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_food_detail_public);
        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();
        activityPostFoodDetailPublicBinding= ActivityPostFoodDetailPublicBinding.inflate(getLayoutInflater());
        setContentView(activityPostFoodDetailPublicBinding.getRoot());

        //--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.postsenderreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.postsenderreqnavid:
                        startActivity(new Intent(getApplicationContext(),OfferSenderRequestDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.postsenderaccnavid:
                        startActivity(new Intent(getApplicationContext(),Offer_Sender_Accept_Dashboard.class));
                        overridePendingTransition(0,0);
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

            // for cancel/accept request
            PLfooddescname2=mbundle.getString("name");
            PLfooddescquantity2=mbundle.getString("quantity");
            PLfooddescexpiry2=mbundle.getString("time");
            PLfooddescaddress2=mbundle.getString("address");
            PLfooddesccity2=mbundle.getString("city");

            //for update
            key=mbundle.getString("keyvalue");


            PLUploaderid = mbundle.getString("ppuserId");
            PLFoodStatus = mbundle.getString("foodstatus");
            PLRequesterid = mbundle.getString("requesterid");
            PLUploadername = mbundle.getString("uploadername");
            progressDialog.dismiss();
        }
        PLRequesterid = firebaseUser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post Food").child(key);
    }



    public void btnRequestPostFood(View view) {
        PLFoodStatus = "requested";

        PostFoodDataModelClass postfoodDataModelClass = new PostFoodDataModelClass(PLfooddescname2,PLfooddescquantity2,
                PLfooddescexpiry2,PLUploaderid,PLfooddescaddress2,PLfooddesccity2,PLUploadername,key,PLFoodStatus,PLRequesterid
        );



        databaseReference.setValue(postfoodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(PostFoodDetailPublic.this, "Offer Sent", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(PostFoodDetailPublic.this, " ERROR : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(PostFoodDetailPublic.this,"offer sending ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }

    public void btnCancelPostFoodRequest(View view) {
        PLFoodStatus = "na";

        PostFoodDataModelClass postfoodDataModelClass = new PostFoodDataModelClass(PLfooddescname2,PLfooddescquantity2,
                PLfooddescexpiry2,PLUploaderid,PLfooddescaddress2,PLfooddesccity2,PLUploadername,key,PLFoodStatus,PLRequesterid
        );



        databaseReference.setValue(postfoodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(PostFoodDetailPublic.this, "Offer Cancelled", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(PostFoodDetailPublic.this, " ERROR : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(PostFoodDetailPublic.this,"offer canceling ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }

    public void openChatPostSender(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",PLUploaderid);
        i.putExtra("hisName",PLUploadername);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}
