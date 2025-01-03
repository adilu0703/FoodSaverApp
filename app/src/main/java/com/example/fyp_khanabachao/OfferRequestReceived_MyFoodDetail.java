package com.example.fyp_khanabachao;

import static android.content.ContentValues.TAG;

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

import com.example.fyp_khanabachao.databinding.ActivityOfferRequestReceivedMyFoodDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OfferRequestReceived_MyFoodDetail extends DrawerBaseActivity {
  ActivityOfferRequestReceivedMyFoodDetailBinding activityOfferRequestReceivedMyFoodDetailBinding;
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
        setContentView(R.layout.activity_offer_request_received_my_food_detail);
        activityOfferRequestReceivedMyFoodDetailBinding= ActivityOfferRequestReceivedMyFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(activityOfferRequestReceivedMyFoodDetailBinding.getRoot());

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

            // for cancel/accept request
            PLfooddescname2=mbundle.getString("name");
            PLfooddescquantity2=mbundle.getString("quantity");
            PLfooddescexpiry2=mbundle.getString("time");
           PLfooddescaddress2=mbundle.getString("address");
            PLfooddesccity2=mbundle.getString("city");

            //for update
            PLkey = mbundle.getString("keyvalue");


            PLUploaderid = mbundle.getString("ppuserId");
            PLFoodStatus = mbundle.getString("foodstatus");
            PLRequesterid = mbundle.getString("requesterid");
            PLUploadername = mbundle.getString("uploadername");
            progressDialog.dismiss();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Post Food").child(PLkey);
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



    public void btnofferAcceptRequest(View view) {
        PLFoodStatus = "accepted";

        PostFoodDataModelClass postfoodDataModelClass = new PostFoodDataModelClass(PLfooddescname2,PLfooddescquantity2,
                PLfooddescexpiry2,PLUploaderid,PLfooddescaddress2,PLfooddesccity2,PLUploadername,PLkey,PLFoodStatus,PLRequesterid
        );

        databaseReference.setValue(postfoodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(OfferRequestReceived_MyFoodDetail.this, "Offer Accepted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(OfferRequestReceived_MyFoodDetail.this, "Offer Acceptance Failure : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(OfferRequestReceived_MyFoodDetail.this,"Offer Accepted ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }

    public void btnofferCancelRequest(View view) {
        PLFoodStatus = "na";

        PostFoodDataModelClass postfoodDataModelClass = new PostFoodDataModelClass(PLfooddescname2,PLfooddescquantity2,
                PLfooddescexpiry2,PLUploaderid,PLfooddescaddress2,PLfooddesccity2,PLUploadername,PLkey,PLFoodStatus,PLRequesterid
        );

        databaseReference.setValue(postfoodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(OfferRequestReceived_MyFoodDetail.this, "Offer Cancelled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(OfferRequestReceived_MyFoodDetail.this, "Offer Cancel Failure : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(OfferRequestReceived_MyFoodDetail.this,"Offer cancel ERROR",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });
    }


    public void openChatPostReceiver(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        String add="";
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",PLRequesterid);
        i.putExtra("hisName",PLRequesterName);
        i.putExtra("add",add);

        activity.startActivity(i);
    }
}
