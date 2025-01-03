package com.example.fyp_khanabachao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fyp_khanabachao.databinding.ActivityUploadPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UploadPost extends DrawerBaseActivity {
    ActivityUploadPostBinding activityUploadPostBinding;
    TextInputLayout Postfoodname,Postfoodquantity,Postfoodaddress,Postfoodcity,PostfoodtimeLimit,Postuploadername;
    String userID;
    List<String> mFoodList;
    adminUserListAdapter charityUsersAdapterClass;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        activityUploadPostBinding = ActivityUploadPostBinding.inflate(getLayoutInflater());
        setContentView(activityUploadPostBinding.getRoot());
        mFoodList = new ArrayList<String>();

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();
        //--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.consumernavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.consumernavid:
                        startActivity(new Intent(getApplicationContext(), PostConsumerDashBoard.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.providernavid:
                        startActivity(new Intent(getApplicationContext(), PostListView.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
//--------------------------------------------------------



        Postfoodname = findViewById(R.id.Postfoodnameid);
        Postfoodquantity = findViewById(R.id.Postfoodquantityid);
        PostfoodtimeLimit = findViewById(R.id.PostfoodTimeLimit);
        Postfoodaddress= findViewById(R.id.Postfoodaddressid);
        Postfoodcity= findViewById(R.id.Postfoodcityid);
        Postuploadername = findViewById(R.id.Postuploadernameid);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFoodList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren())
                {
                    ConsumerRegisterHelperClass consumerRegisterHelperClass = itemSnapshot.getValue(ConsumerRegisterHelperClass.class);
                    mFoodList.add(itemSnapshot.getKey());
                }
                Log.d("Alluid", mFoodList.toString());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Toast.makeText(CharityUsersList.this, "Food Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }

    public void btnUploadPost(View view) {
        if(!PostvalidateName()  | !PostvalidateQuantity() | !PostvalidateTimeLimit()
                 | !PostvalidateAddress() | !PostvalidateCity()| !validateUploaderName())
        {
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();
        PostuploadFood();
    }
    public void PostuploadFood()  // to firebase using model class
    {   String PostuploaderName = Postuploadername.getEditText().getText().toString();
        String PostfoodName = Postfoodname.getEditText().getText().toString();
        String PostfoodQuantity = Postfoodquantity.getEditText().getText().toString();
        String Postfoodtime = PostfoodtimeLimit.getEditText().getText().toString();
        String PostfoodAddress = Postfoodaddress.getEditText().getText().toString();
        String PostfoodCity = Postfoodcity.getEditText().getText().toString();



        userID = firebaseUser.getUid().toString();
        Log.d("bn","value id :: "+userID);
        String PostfoodStatus  = "na"; //for request
        String PostrequesterId="na";

        String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        PostFoodDataModelClass PostfoodDataModelClass = new PostFoodDataModelClass(PostfoodName,PostfoodQuantity,Postfoodtime, userID,PostfoodAddress,PostfoodCity,PostuploaderName,myCurrentDateTime,PostfoodStatus,PostrequesterId);

        FirebaseDatabase.getInstance().getReference("Post Food").child(myCurrentDateTime).setValue(PostfoodDataModelClass) // public food
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UploadPost.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            for(int i=0 ; i<mFoodList.size();i++){
                                sendhisNotification(""+ mFoodList.get(i),""+userID,"Uploaded a new Custom Food Order");
                            }
                        }
                        else
                        {
                            Toast.makeText(UploadPost.this, "Post Uploading ERROR : "+ task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private Boolean PostvalidateName() {
        String name = Postfoodname.getEditText().getText().toString();

        if (name.isEmpty()) {
            Postfoodname.setError("Field cannot be empty");
            return false;
        } else {
            Postfoodname.setError(null);
            Postfoodname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean PostvalidateQuantity() {
        String quan = Postfoodquantity.getEditText().getText().toString();

        if (quan.isEmpty()) {
            Postfoodquantity.setError("Field cannot be empty");
            return false;
        } else {
            Postfoodquantity.setError(null);
            Postfoodquantity.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean PostvalidateTimeLimit() {
        String expiry = PostfoodtimeLimit.getEditText().getText().toString();

        if (expiry.isEmpty()) {
            PostfoodtimeLimit.setError("Field cannot be empty");
            return false;
        } else {
            PostfoodtimeLimit.setError(null);
            PostfoodtimeLimit.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean PostvalidateAddress()
    {
        String address = Postfoodaddress.getEditText().getText().toString();

        if (address.isEmpty())
        {
            Postfoodaddress.setError("Field cannot be empty");
            return false;
        }
        else
        {
            Postfoodaddress.setError(null);
            Postfoodaddress.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean PostvalidateCity() {
        String city = Postfoodcity.getEditText().getText().toString();

        if (city.isEmpty()) {
            Postfoodcity.setError("Field cannot be empty");
            return false;
        } else {
            Postfoodcity.setError(null);
            Postfoodcity.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateUploaderName() {
        String name = Postuploadername.getEditText().getText().toString();

        if (name.isEmpty()) {
            Postuploadername.setError("Field cannot be empty");
            return false;
        } else {
            Postuploadername.setError(null);
            Postuploadername.setErrorEnabled(false);
            return true;
        }
    }

    private void sendhisNotification(String hisUid,String pId,String notification) {
        String timestamp = "" + System.currentTimeMillis();
        //data to put in firebase
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("pId", pId);
        hashMap.put("timestamp", timestamp);
        hashMap.put("pUid", hisUid);
        hashMap.put("notification", notification);
        hashMap.put("sUid", userID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(hisUid).child("Notifications").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }




}
