package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;


import com.example.fyp_khanabachao.databinding.ActivityConsumerDashboardBinding;
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

public class ConsumerDashboard extends DrawerBaseActivity
{
    // all items

    ActivityConsumerDashboardBinding activityConsumerDashboardBinding;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    EditText editTextsearch;

    RecyclerView mRecyclerView;
    List<FoodDataModelClass> mFoodList;
    FoodDataModelClass mFoodDataModelClass;
    PublicAdapterFoodItem myAdapterFoodItem;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_dashboard);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
         activityConsumerDashboardBinding = ActivityConsumerDashboardBinding.inflate(getLayoutInflater());
         setContentView(activityConsumerDashboardBinding.getRoot());

        // bottom nav bar
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

        mRecyclerView = findViewById(R.id.recyclerviewconsumerdashboardid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ConsumerDashboard.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        editTextsearch = findViewById(R.id.searchfoodid);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading food items ...");
        progressDialog.show();

        mFoodList = new ArrayList<>();

        myAdapterFoodItem = new PublicAdapterFoodItem(ConsumerDashboard.this,mFoodList);
        mRecyclerView.setAdapter(myAdapterFoodItem);

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                mFoodList.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren())
                {
                    if(itemSnapshot.child("foodStatus").getValue().toString().toLowerCase().equals("na"))
                    {
                   FoodDataModelClass foodDataModelClass = itemSnapshot.getValue(FoodDataModelClass.class);
                   foodDataModelClass.setKey(itemSnapshot.getKey());  //for key
                   mFoodList.add(foodDataModelClass);
                }
                }

                Collections.reverse(mFoodList);
                myAdapterFoodItem.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
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
                searchFood(s.toString());

            }
        });

    }

    private void searchFood(String text)
    {
        ArrayList<FoodDataModelClass> searchlist = new ArrayList<>();
        for(FoodDataModelClass item : mFoodList)
        {
            if(item.getItemName().toLowerCase().contains(text.toLowerCase()))
            {
                searchlist.add(item);
            }
        }
        myAdapterFoodItem.searchedList(searchlist);

    }

    public void consumerLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),consumerLOGIN.class));
        finish();
    }
}