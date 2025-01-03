package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_khanabachao.databinding.ActivityConsumerProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class consumerProfile extends DrawerBaseActivity
{
   ActivityConsumerProfileBinding activityConsumerProfileBinding;
    public static final String TAG = "MYTAG";

    TextInputLayout conretFullname,conretUsername,conretEmail, conretPhoneNo;
    String usertype;

    FirebaseDatabase rootnodee;
    DatabaseReference referencee;
    FirebaseUser firebaseUserr;
    private FirebaseAuth cauthh;
    String uidd;

    AlertDialog.Builder cons_reset_alert;
    LayoutInflater inflater;

    //rating
    float fetchRating;
    String userrating;
    TextView userratingtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_profile);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityConsumerProfileBinding = ActivityConsumerProfileBinding.inflate(getLayoutInflater());
        setContentView(activityConsumerProfileBinding.getRoot());

        cauthh = FirebaseAuth.getInstance();
        rootnodee = FirebaseDatabase.getInstance();
        referencee = rootnodee.getReference("users");

        firebaseUserr = cauthh.getCurrentUser();
        uidd = firebaseUserr.getUid();  //current uid

        cons_reset_alert=new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        userratingtxt = findViewById(R.id.userratingid);


        //--------------

        // bottom nav bar

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.consumernavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.consumernavid:
                        startActivity(new Intent(getApplicationContext(),ConsumerDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.providernavid:
                        startActivity(new Intent(getApplicationContext(),FoodUpload_01.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //-------------------------------

        conretFullname = findViewById(R.id.consumerfullnameproId);
        conretUsername = findViewById(R.id.consumerusernameproId);
        conretEmail = findViewById(R.id.consumeremailproId);
        conretPhoneNo = findViewById(R.id.consumerphoneproId);

       // conretUsertype = findViewById(R.id.consumertypeId);
        //conretAddress = findViewById(R.id.consumeraddressproId);
        //conRetCity = findViewById(R.id.consumercityproId);



       referencee.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot)
           {
               String consprofullname = snapshot.child(uidd).child("name").getValue(String.class);
               String consprousername = snapshot.child(uidd).child("username").getValue(String.class);
               String consproemail = snapshot.child(uidd).child("email").getValue(String.class);
               String consprophone = snapshot.child(uidd).child("phone").getValue(String.class);

               usertype = snapshot.child(uidd).child("usertype").getValue(String.class);
               fetchRating = snapshot.child(uidd).child("rating").getValue(Float.class);
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
               //Toast.makeText(FoodDetailPublic.this,"OLD RATING : "+fetchRating,Toast.LENGTH_SHORT).show();
               //System.out.println(consusername);

               conretFullname.getEditText().setText(consprofullname);
               conretUsername.getEditText().setText(consprousername);
               conretPhoneNo.getEditText().setText(consprophone);
               conretEmail.getEditText().setText(consproemail);
               //conretUsertype.getEditText().setText(usertype);
              // conretAddress.getEditText().setText(consproaddress);
               //conRetCity.getEditText().setText(consprocity);

           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.d(TAG,"Consumer profile view : " + error.getMessage());
           }});
    }



    public void updateConsProfileBtn(View view) //btn
    {  // called from updated button in xml
        //Toast.makeText(consumerProfile.this, "btn workingg", Toast.LENGTH_SHORT).show();
        if(!validateconfullName()  | !validateconPhoneNo() | !validateconEmail() | !validateconUsername() )
        {
            return;
        }

        //storing entered values in following variables
        String conupdatefullname =conretFullname.getEditText().getText().toString();
        String conupdateusername = conretUsername.getEditText().getText().toString();
        String conupdateemail = conretEmail.getEditText().getText().toString();
        String conupdatephoneNo = conretPhoneNo.getEditText().getText().toString();
      //  String Cuserid = firebaseUserr.getUid().toString();
        //String conupdateUsertype = conretUsertype.getEditText().getText().toString();

        //String conupdateaddress = conretAddress.getEditText().getText().toString();
        //String conupdatecity = conRetCity.getEditText().getText().toString();


        ConsumerRegisterHelperClass helperClass = new ConsumerRegisterHelperClass(conupdatefullname,conupdateusername,conupdateemail,
                conupdatephoneNo, usertype,uidd,fetchRating);

        referencee.child(firebaseUserr.getUid()).setValue(helperClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(consumerProfile.this, "User UPDATED Sucessfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(consumerProfile.this, "User Updation failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        //updating at uthentication site

        //updating email
        firebaseUserr.updateEmail(conupdateemail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(consumerProfile.this, "EMAIL UPDATED Sucessfully", Toast.LENGTH_LONG).show();
                conretEmail.getEditText().setText(conupdateemail);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(consumerProfile.this, "EMAIL updation FAILED::: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void updatePassword(View view)
    {
         View v = inflater.inflate(R.layout.conreset_pop,null);

        cons_reset_alert.setTitle("Updating password link will be sent to your login email");

        cons_reset_alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        cauthh.sendPasswordResetEmail(conretEmail.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(consumerProfile.this, "Updating password link sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(consumerProfile.this, "Updating password link sending FAILURE : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("Cancel",null)
                .create().show();
    }

    private Boolean validateconfullName() {
        String conregValue = conretFullname.getEditText().getText().toString();

        if (conregValue.isEmpty()) {
            conretFullname.setError("Field cannot be empty");
            return false;
        } else {
            conretFullname.setError(null);
            conretFullname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateconUsername() {
        String username = conretUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (username.isEmpty()) {
            conretUsername.setError("Field cannot be empty");
            return false;
        } else if (username.length() >= 10) {
            conretUsername.setError("Username too long");
            return false;
        } else if (!username.matches(noWhiteSpace)) {
            conretUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            conretUsername.setError(null);
            conretUsername.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateconPhoneNo() {
        String val = conretPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            conretPhoneNo.setError("Field cannot be empty");
            return false;
        }
        else
        {
            conretPhoneNo.setError(null);
            conretPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateconEmail() {

        String email = conretEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            conretEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            conretEmail.setError("Invalid email address");
            return false;
        } else {
            conretEmail.setError(null);
            conretEmail.setErrorEnabled(false);
            return true;
        }
    }
}