package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class consumerLOGIN extends AppCompatActivity {

    public static final String TAG = "MYTAG";

    TextInputLayout conlogEmail, conlogPassword;
    Button conlogBtn;
    TextView logforgetPwd;
    ProgressBar progressBar;
    FirebaseAuth cAuth;
    AlertDialog.Builder cons_reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // hide notif bar
        setContentView(R.layout.activity_consumer_login);

        conlogEmail = findViewById(R.id.consumerlogusernameId);
        conlogPassword = findViewById(R.id.consumerlogpwdId);

        cAuth =FirebaseAuth.getInstance();

        cons_reset_alert=new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

         /*if(cAuth.getCurrentUser()!=null)  // if user already loggedin
        {
            startActivity(new Intent(getApplicationContext(),MainActivity2chooseuser.class));
            finish();
        }*/

        if(cAuth.getCurrentUser()!=null)  // if user already loggedin
        {
            if(cAuth.getUid().equals("nmahRSb4vKedibYjwJv9iQ5KgQy2"))
            {
                startActivity(new Intent(getApplicationContext(), dashboardAdmin.class));
                finish();
            }
            else
            {
                startActivity(new Intent(getApplicationContext(), MainActivity2chooseuser.class));
                finish();
            }

        }
    }

    private Boolean validateconEmail() {

        String email = conlogEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            conlogEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            conlogEmail.setError("Invalid email address");
            return false;
        } else {
            conlogEmail.setError(null);
            conlogEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateconlogPassword() {

        String logval = conlogPassword.getEditText().getText().toString();


        if (logval.isEmpty()) {
            conlogPassword.setError("Field cannot be empty");
            return false;
        }

        else if (logval.length()<5) {
            conlogPassword.setError("Minimum password length 5 characters");
            return false;
        } else {
            conlogPassword.setError(null);
            conlogPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void loginConsumer(View view) {   //login buton


        String consEnteredEmail = conlogEmail.getEditText().getText().toString().trim();
        String consEnteredPassword = conlogPassword.getEditText().getText().toString().trim();


        //Validate Login Info 
        if (!validateconEmail() | !validateconlogPassword()) {
            return;
        }
           else
        {
            //islogConsumer();

           // String consEnteredEmail = conlogEmail.getEditText().getText().toString().trim();
            //String consEnteredPassword = conlogPassword.getEditText().getText().toString().trim();

           cAuth.signInWithEmailAndPassword(consEnteredEmail,consEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
/*
                        if(consEnteredEmail.equals("admin@gmail.com")&& consEnteredPassword.equals("admin123"))
                        {
                            Toast.makeText(consumerLOGIN.this, "Admin Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(consumerLOGIN.this, AdminDashboardd.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            {*/

                            Toast.makeText(consumerLOGIN.this, "Consumer Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(consumerLOGIN.this, MainActivity2chooseuser.class);
                            startActivity(intent);
                            finish();
                           }
                   // }
                    else
                    {
                        Toast.makeText(consumerLOGIN.this, "Incorrect Email/Password ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Consumer login FAILURE ERROR : " + task.getException().getMessage());
                    }
                }
            });

        }
    }

    public void signupConsumer(View view) {  // made on consumer login xml on signup click

        Intent intent=new Intent(consumerLOGIN.this,consumerSignup.class);
        startActivity(intent);
    }

    public void forgetPassword(View view) {      // onclick on forget password using email

        View v = inflater.inflate(R.layout.conreset_pop,null);  // layout for forgot password

        cons_reset_alert.setTitle("Reset Forgot password ?")    //alert dialog
                .setMessage("Enter your email to get password reset link")
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //validate the email addres and then send the reset link
                        EditText con_email_forg = v.findViewById(R.id.conresetemailId); //getting email

                        if(con_email_forg.getText().toString().isEmpty())
                        {
                            con_email_forg.setError("Required field");
                            return;
                        }
                        // send reset link

                        cAuth.sendPasswordResetEmail(con_email_forg.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(consumerLOGIN.this, "Reset email sent", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(consumerLOGIN.this, "Reset email sending FAILURE", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("Cancel",null)
                .setView(v).
                create().show();
    }

    public void adminLogin(View view)
    {
        Intent intent=new Intent(consumerLOGIN.this,AdminLogin.class);
        startActivity(intent);
    }
}