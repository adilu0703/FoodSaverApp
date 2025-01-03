package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyp_khanabachao.databinding.ActivityPostListViewBinding;
import com.example.fyp_khanabachao.databinding.ActivitySenderAcceptedDetailPublicBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SenderAccepted_DetailPublic extends DrawerBaseActivity {
ActivitySenderAcceptedDetailPublicBinding activitySenderAcceptedDetailPublicBinding;
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

    String Foodstatus="";
    String key="";
    String imageurl="";
    String Requesterid="";
    String Uploaderid="";
    String Uploadername="";

    FirebaseDatabase rootnode;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    RatingBar ratingbar;
    float oldrating;
    float avgrating;
    String consprofullname,consprousername,consproemail,getConsproemail,consprophone,usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_accepted_detail_public);
        activitySenderAcceptedDetailPublicBinding = ActivitySenderAcceptedDetailPublicBinding.inflate(getLayoutInflater());
        setContentView(activitySenderAcceptedDetailPublicBinding.getRoot());
        cauth = FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        ratingbar = findViewById(R.id.ratingbarid);

        databaseReference = rootnode.getReference("users");

        //to get old rating
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                 consprofullname = snapshot.child(Uploaderid).child("name").getValue(String.class);
                 consprousername = snapshot.child(Uploaderid).child("username").getValue(String.class);
                 consproemail = snapshot.child(Uploaderid).child("email").getValue(String.class);
                 consprophone = snapshot.child(Uploaderid).child("phone").getValue(String.class);
                 usertype = snapshot.child(Uploaderid).child("usertype").getValue(String.class);
                oldrating = snapshot.child(Uploaderid).child("rating").getValue(Float.class);
                // Toast.makeText(SenderAccepted_DetailPublic.this,"OLD RATING : "+oldrating,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

            //for update and foodrequest
            Uploaderid=mbundle.getString("uploaderid");
            key=mbundle.getString("keyvalue");
            imageurl = mbundle.getString("image");
            Requesterid=mbundle.getString("requesterid");
            Uploadername=mbundle.getString("uploadername");

        }
    }

    public void btnUserRating(View view)
    {


        String a = String.valueOf(ratingbar.getRating());
        float rating= Float.parseFloat(a);

        //Toast.makeText(SenderAccepted_DetailPublic.this,"stars : "+rating,Toast.LENGTH_SHORT).show();

        if(oldrating==0)
        {
            avgrating = rating;
            // Toast.makeText(SenderAccepted_DetailPublic.this,"checking working if cond", Toast.LENGTH_LONG).show();
        }
        else
        {
            avgrating = (oldrating + rating)/2;
        }

        //uploading rating

        ConsumerRegisterHelperClass helperClass = new ConsumerRegisterHelperClass(consprofullname,consprousername,consproemail,
                consprophone, usertype,Uploaderid,avgrating);

        FirebaseDatabase.getInstance().getReference("users").child(Uploaderid).setValue(helperClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SenderAccepted_DetailPublic.this,rating +" Stars Rated", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                                    Toast.makeText(SenderAccepted_DetailPublic.this, "Rating ERROR : "+ task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void openChatFoodAcceptedRequest(View view) {
        String add="";
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("hisUid",Uploaderid);
        i.putExtra("hisName",Uploadername);
        i.putExtra("add",add);

        activity.startActivity(i);
    }

    public void generateReceipt(View view)
    {
        Intent intent = new Intent(SenderAccepted_DetailPublic.this, paymentReceipt.class);
        intent.putExtra("keyv" , key );
        startActivity(intent);
    }
}