package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.CharityUsersAdapterClass;
import com.example.fyp_khanabachao.ConsumerDashboard;
import com.example.fyp_khanabachao.ConsumerRegisterHelperClass;
import com.example.fyp_khanabachao.DrawerBaseActivity;
import com.example.fyp_khanabachao.FoodDataModelClass;
import com.example.fyp_khanabachao.FoodUpload_01;
import com.example.fyp_khanabachao.MyAdapterFoodItem;
import com.example.fyp_khanabachao.MyFoodUpload;
import com.example.fyp_khanabachao.R;
import com.example.fyp_khanabachao.databinding.ActivityCharityUsersListBinding;
import com.example.fyp_khanabachao.databinding.ActivityFoodUpload01Binding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharityUsersList extends DrawerBaseActivity {

    ActivityCharityUsersListBinding activityCharityUsersListBinding;
    EditText editTextsearch;
    String uidd;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    RecyclerView mRecyclerView;
    List<ConsumerRegisterHelperClass> mFoodList;
    CharityUsersAdapterClass charityUsersAdapterClass;
    ConsumerRegisterHelperClass mConsumerRegisterHelperClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_users_list);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityCharityUsersListBinding= ActivityCharityUsersListBinding.inflate(getLayoutInflater());
        setContentView(activityCharityUsersListBinding.getRoot());




//--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.consumernavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.consumernavid:
                        startActivity(new Intent(getApplicationContext(), ConsumerDashboard.class));
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
//--------------------------------------------------------


        editTextsearch = findViewById(R.id.searchcharityuserid);
        mRecyclerView = findViewById(R.id.recyclerviewcharityuserid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CharityUsersList.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


      //  progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading Charity Organizations ...");
        //progressDialog.show();

        mFoodList = new ArrayList<>();
        charityUsersAdapterClass = new CharityUsersAdapterClass(CharityUsersList.this,mFoodList);
        mRecyclerView.setAdapter(charityUsersAdapterClass);


        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");




       valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mFoodList.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren())
                {
                    if(itemSnapshot.child("usertype").getValue().toString().equals("Charity organization"))
                    {
                        ConsumerRegisterHelperClass consumerRegisterHelperClass = itemSnapshot.getValue(ConsumerRegisterHelperClass.class);
                        mFoodList.add(consumerRegisterHelperClass);
                    }
                }

                charityUsersAdapterClass.notifyDataSetChanged();
                //progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
               // Toast.makeText(CharityUsersList.this, "Food Uploaded", Toast.LENGTH_SHORT).show();
               // progressDialog.dismiss();
            }
        });




        // search food implementation

       editTextsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter(s.toString());

            }
        });

    }

    //called from MyAdapterFoodItem
    private void filter(String text)
    {
        ArrayList<ConsumerRegisterHelperClass> searchlist = new ArrayList<>();
        for(ConsumerRegisterHelperClass item : mFoodList)
        {
            if(item.getUsername().toLowerCase().contains(text.toLowerCase()) || item.getName().toLowerCase().contains(text.toLowerCase()))
            {
                searchlist.add(item);
            }
        }
        charityUsersAdapterClass.searchedList(searchlist);

    }
}