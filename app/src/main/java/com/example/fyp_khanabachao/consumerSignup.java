package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class consumerSignup extends AppCompatActivity {

    TextInputLayout conregFullname, conregUsername, conregEmail, conregPhoneNo, conregAddress, conRegCity, conregPassword;
    Button conregBtn;
    TextView conregToLoginBtn;
    //ProgressBar progressBar;

    RadioGroup usertyperg;
    RadioButton usertyperb;

    public static final String TAG = "MYTAG";
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    //FirebaseAuth.AuthStateListener firebaseauthlistner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // hide notif bar
        setContentView(R.layout.activity_consumer_signup);


        //Hooks to all xml elements in activity_sign_up.xml
        conregFullname = findViewById(R.id.consumerfullnameId);
        conregUsername = findViewById(R.id.consumerusernameId);
        conregEmail = findViewById(R.id.consumeremailId);
        conregPhoneNo = findViewById(R.id.consumerphoneId);
       // conregAddress = findViewById(R.id.consumeraddressId);
        //conRegCity = findViewById(R.id.consumercityId);
        conregPassword = findViewById(R.id.consumerpwdId);

        conregBtn = findViewById(R.id.consumerregisterbtnId);  //registration button names as GO
        conregToLoginBtn = findViewById(R.id.consumerregtologinbtnId); // to login page
        //progressBar = findViewById(R.id.progressbarcregId);

        usertyperg  =findViewById(R.id.usertypeid);

        cauth = FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("users");
    }


    public void registerConsumer(View view)
    {
        //If any of these function are not valid then it return to registeruser and show error, otherwise it will store and show data in firebase

        if(!validateconfullName() |!validateconPassword() | !validateconPhoneNo() | !validateconEmail() |
                !validateconUsername() | !validateUserType())
        {
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");
        progressDialog.show();
       //progressBar.setVisibility(view.VISIBLE);

        //storing entered values in following variables
        String fullname =conregFullname.getEditText().getText().toString();
        String email = conregEmail.getEditText().getText().toString();
        String phoneNo = conregPhoneNo.getEditText().getText().toString();
       // String address = conregAddress.getEditText().getText().toString();
        //String city = conRegCity.getEditText().getText().toString();
        String username = conregUsername.getEditText().getText().toString();
        String password = conregPassword.getEditText().getText().toString();
//       String Cuserid = firebaseUser.getUid().toString();

        //uploading usertype using radiobtn
        int radioid = usertyperg.getCheckedRadioButtonId();
        usertyperb = findViewById(radioid);
        String userType = usertyperb.getText().toString();


        cauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    firebaseUser = cauth.getCurrentUser();
                    String uidd = firebaseUser.getUid();

                    float initialrating = 0;

                    ConsumerRegisterHelperClass helperClass = new ConsumerRegisterHelperClass(fullname,username,email,phoneNo,userType,uidd,initialrating);

                    reference.child(firebaseUser.getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();

                                Toast.makeText(consumerSignup.this, "User Registered Sucessfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(consumerSignup.this,consumerLOGIN.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(consumerSignup.this, "User Registered errro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(consumerSignup.this, "Auth error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(consumerSignup.this, "Consumer auth FAFFFAIILLLLED", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void toLoginfromRegC(View view) {
        Intent intent = new Intent(consumerSignup.this,consumerLOGIN.class);
        finish();
    }

    private Boolean validateconfullName() {
        String conregValue = conregFullname.getEditText().getText().toString();

        if (conregValue.isEmpty()) {
            conregFullname.setError("Field cannot be empty");
            return false;
        } else {
            conregFullname.setError(null);
            conregFullname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateconUsername() {
        String username = conregUsername.getEditText().getText().toString();
        //String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (username.isEmpty()) {
            conregUsername.setError("Field cannot be empty");
            return false;
        } else {
            conregUsername.setError(null);
            conregUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateconEmail() {

        String email = conregEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            conregEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            conregEmail.setError("Invalid email address");
            return false;
        } else {
            conregEmail.setError(null);
            conregEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateconPhoneNo() {
        String val = conregPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            conregPhoneNo.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() != 11)
        {
            conregPhoneNo.setError("Invalid phone number");
            return false;
        }
        else {
            conregPhoneNo.setError(null);
            conregPhoneNo.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateconPassword() {

        String val = conregPassword.getEditText().getText().toString();

        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            conregPassword.setError("Field cannot be empty");
            return false;
        } else if (val.length() < 5) {
            conregPassword.setError("Minimum password length 5 characters");
            return false;
        } else {
            conregPassword.setError(null);
            conregPassword.setErrorEnabled(false);
            return true;
        }
    }

   private Boolean validateUserType()
    {
        int radioid = usertyperg.getCheckedRadioButtonId();;

        if ( radioid == -1 )
        {
            Toast.makeText(consumerSignup.this,"User type not selected",Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

}
