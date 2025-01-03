package com.example.fyp_khanabachao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.example.fyp_khanabachao.databinding.ActivityPostUpdateBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class PostUpdate extends DrawerBaseActivity {
    ActivityPostUpdateBinding activityPostUpdateBinding;
    LayoutInflater inflater;

    PostFoodDataModelClass PostfoodDataModelClass;

    TextInputLayout Puploadernameid,Pfoodname,Pfoodtime,Pfoodquantity,Pfoodaddress,Pfoodcity;
    ProgressDialog progressDialog;

    String PuploaderNameId,PfoodName, PfoodQuantity,  PfoodAddress, PfoodCity,PfoodTime;
    String Puploadername="";
    String PfoodStatus;
    String PRequesterid="";

    DatabaseReference databaseReference;
    StorageReference storageReference;
    String key;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);
        activityPostUpdateBinding= ActivityPostUpdateBinding.inflate(getLayoutInflater());
        setContentView(activityPostUpdateBinding.getRoot());
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

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();


        Pfoodname = findViewById(R.id.Postfoodnameidu);
        Pfoodquantity = findViewById(R.id.Postfoodquantityidu);
        Pfoodtime = findViewById(R.id.PostfoodTimeLimitu);
        Pfoodaddress = findViewById(R.id.Postfoodaddressidu);
        Pfoodcity = findViewById(R.id.Postfoodcityidu);
        Puploadernameid=findViewById(R.id.Postuploadernameidu);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Puploadernameid.getEditText().setText(bundle.getString("uploadernamep"));
            Pfoodname.getEditText().setText(bundle.getString("namep"));
            Pfoodquantity.getEditText().setText(bundle.getString("quantityp"));
            Pfoodtime.getEditText().setText(bundle.getString("timep"));
            Pfoodaddress.getEditText().setText(bundle.getString("addressp"));
            Pfoodcity.getEditText().setText(bundle.getString("cityp"));
            key = bundle.getString("keyp");
            PfoodStatus = bundle.getString("foodstatusp");
            PRequesterid = bundle.getString("requesteridp");
            Puploadername = bundle.getString("uploadernamep");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Post Food").child(key);
        //Toast.makeText(updateMyFood.this,"KEY :: "+ key,Toast.LENGTH_LONG).show();

    }

    public void btnUpdatePost(View view)
    {
        if(!validateUName()|!validateName()  | !validateQuantity() | !validateExpiry()
                | !validateAddress() | !validateCity())
        {
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating ....");
        progressDialog.show();

        PuploaderNameId= Puploadernameid.getEditText().getText().toString().trim();
        PfoodName = Pfoodname.getEditText().getText().toString().trim();
        PfoodQuantity = Pfoodquantity.getEditText().getText().toString().trim();
        PfoodTime = Pfoodtime.getEditText().getText().toString().trim();
        PfoodAddress = Pfoodaddress.getEditText().getText().toString().trim();
        PfoodCity = Pfoodcity.getEditText().getText().toString().trim();
        uploadPostFood();
    }
    public void uploadPostFood()  // to firebase using model class
    {



        String userID = firebaseUser.getUid().toString();
        Log.d("bn","value id :: "+userID);

        PostfoodDataModelClass = new PostFoodDataModelClass(PfoodName,PfoodQuantity,PfoodTime,userID
                ,PfoodAddress,PfoodCity,Puploadername,key,PfoodStatus,PRequesterid);


        databaseReference.setValue(PostfoodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Toast.makeText(PostUpdate.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(PostUpdate.this, "Updatting ERROR : "+ task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(PostUpdate.this,"Updation Failed",Toast.LENGTH_SHORT).show();
                Log.d("bn","upd error :: "+ e.getMessage());

            }
        });

        Intent intent = new Intent(PostUpdate.this, PostListView.class);
        startActivity(intent);

    }


    // ========================= VALIDATIONS
    private Boolean validateUName() {
        String name = Puploadernameid.getEditText().getText().toString();

        if (name.isEmpty()) {
            Puploadernameid.setError("Field cannot be empty");
            return false;
        } else {
            Puploadernameid.setError(null);
            Puploadernameid.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateName() {
        String name = Pfoodname.getEditText().getText().toString();

        if (name.isEmpty()) {
            Pfoodname.setError("Field cannot be empty");
            return false;
        } else {
            Pfoodname.setError(null);
            Pfoodname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateQuantity() {
        String quan = Pfoodquantity.getEditText().getText().toString();

        if (quan.isEmpty()) {
            Pfoodquantity.setError("Field cannot be empty");
            return false;
        } else {
            Pfoodquantity.setError(null);
            Pfoodquantity.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateExpiry() {
        String expiry =  Pfoodtime.getEditText().getText().toString();

        if (expiry.isEmpty()) {
            Pfoodtime.setError("Field cannot be empty");
            return false;
        } else {
            Pfoodtime.setError(null);
            Pfoodtime.setErrorEnabled(false);
            return true;
        }
    }








    private Boolean validateAddress()
    {
        String address = Pfoodaddress.getEditText().getText().toString();

        if (address.isEmpty())
        {
            Pfoodaddress.setError("Field cannot be empty");
            return false;
        }
        else
        {
            Pfoodaddress.setError(null);
            Pfoodaddress.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateCity() {
        String city = Pfoodcity.getEditText().getText().toString();

        if (city.isEmpty()) {
            Pfoodcity.setError("Field cannot be empty");
            return false;
        } else {
            Pfoodcity.setError(null);
            Pfoodcity.setErrorEnabled(false);
            return true;
        }
    }
}