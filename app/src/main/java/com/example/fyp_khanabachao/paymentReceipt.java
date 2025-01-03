package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_khanabachao.databinding.ActivityConsumerProfileBinding;
import com.example.fyp_khanabachao.databinding.ActivityPaymentReceiptBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class paymentReceipt extends DrawerBaseActivity
{

    ActivityPaymentReceiptBinding activityPaymentReceiptBinding;

    FirebaseDatabase rootnodee;
    DatabaseReference databaseReference;
    DatabaseReference referencee;
    FirebaseUser firebaseUserr;
    FirebaseAuth cauthh;
    String uidd;
    TextView uploadernametxt, consumernametxt, foodnametxt,  citytxt, addresstxt, pricetxt;
    String key;
    String reqid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);

        activityPaymentReceiptBinding = ActivityPaymentReceiptBinding.inflate(getLayoutInflater());
        setContentView(activityPaymentReceiptBinding.getRoot());

        uploadernametxt = findViewById(R.id.uplodernamer);
        consumernametxt = findViewById(R.id.consumernamer);
        foodnametxt =findViewById(R.id.foodnamer);
        pricetxt = findViewById(R.id.foodpricer);
        addresstxt = findViewById(R.id.addresser);
        citytxt = findViewById(R.id.cityr);



        // intent
        Bundle mbundle = getIntent().getExtras();

        if(mbundle!=null)
        {
            key = mbundle.getString("keyv");
            //uploadername.setText(key);
        }


        referencee = FirebaseDatabase.getInstance().getReference("users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food");


        cauthh = FirebaseAuth.getInstance();
        firebaseUserr = cauthh.getCurrentUser();
        uidd = firebaseUserr.getUid();

        //Toast.makeText(paymentReceipt.this,"uid is--  : "+uidd,Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
               // String uname = snapshot.child(uidd).child("name").getValue(String.class);

                uploadernametxt.setText( snapshot.child(key).child("uploaderName").getValue(String.class)  );
                consumernametxt.setText( snapshot.child(uidd).child("name").getValue(String.class)  );
                foodnametxt.setText( snapshot.child(key).child("itemName").getValue(String.class)  );
                pricetxt.setText( snapshot.child(key).child("itemPrice").getValue(String.class)  );
                addresstxt.setText( snapshot.child(key).child("itemAddress").getValue(String.class)  );
                citytxt.setText( snapshot.child(key).child("itemCity").getValue(String.class)  );

                reqid = snapshot.child(key).child("requesterId").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        referencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                consumernametxt.setText( snapshot.child(reqid).child("name").getValue(String.class)  );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}