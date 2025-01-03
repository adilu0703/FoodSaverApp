package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {

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
        setContentView(R.layout.activity_admin_login);
        conlogEmail = findViewById(R.id.consumerlogusernameId);
        conlogPassword = findViewById(R.id.consumerlogpwdId);

        cAuth =FirebaseAuth.getInstance();

        cons_reset_alert=new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
    }


    public void loginAdminBtn(View view)
    {

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

                        if(consEnteredEmail.equals("admin@gmail.com")&& consEnteredPassword.equals("admin123"))
                        {
                            Intent intent2 = new Intent(AdminLogin.this, dashboardAdmin.class);
                            startActivity(intent2);
                            Toast.makeText(AdminLogin.this, "Admin Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                        Toast.makeText(AdminLogin.this, "Incorrect admin email/pwd", Toast.LENGTH_SHORT).show();
                         }
                    }
                    else
                    {
                        Toast.makeText(AdminLogin.this, "Admin Incorrect Email/Password ", Toast.LENGTH_SHORT).show();
                        // Log.d(TAG,"Consumer login FAILURE ERROR : " + task.getException().getMessage());
                    }
                }
            });

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
}