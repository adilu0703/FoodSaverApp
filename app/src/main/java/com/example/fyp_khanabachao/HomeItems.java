package com.example.fyp_khanabachao;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.fyp_khanabachao.databinding.ActivityHomeItemsBinding;
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

public class HomeItems extends DrawerBaseActivity
{

    ActivityHomeItemsBinding activityHomeItemsBinding;
    EditText editTextsearch;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    RecyclerView mRecyclerView;
    List<FoodDataModelClass> mFoodList;
    PublicAdapterFoodItem myAdapterFoodItem;
    FoodDataModelClass mFoodDataModelClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_items);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       activityHomeItemsBinding = ActivityHomeItemsBinding.inflate(getLayoutInflater());
        setContentView(activityHomeItemsBinding.getRoot());

        editTextsearch = findViewById(R.id.searchfoodid);

        mRecyclerView = findViewById(R.id.recyclerviewfoodid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeItems.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading food items ...");
        progressDialog.show();

        mFoodList = new ArrayList<>();




        // bottom nav bar--------------
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

        /** mFoodDataModelClass = new FoodDataModelClass("Kabab","Mild spicy 10 pcs kabab cooked today",
         "Rs.200","1","1 week if frozen",R.drawable.fpic2);
         mFoodList.add(mFoodDataModelClass);

         mFoodDataModelClass = new FoodDataModelClass("Pizza","Half large pizza",
         "Rs.600","1","3 days",R.drawable.fpic3);
         mFoodList.add(mFoodDataModelClass);

         mFoodDataModelClass = new FoodDataModelClass("Baryani","1 plate biryani",
         "Rs.500","1","5 days",R.drawable.fpic1);
         mFoodList.add(mFoodDataModelClass);*/

        myAdapterFoodItem = new PublicAdapterFoodItem(HomeItems.this,mFoodList);
        mRecyclerView.setAdapter(myAdapterFoodItem);

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mFoodList.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren())
                {
                    //String st = itemSnapshot.child("userId").getValue().toString();
                    //Log.d("KEY"," key :: "+itemSnapshot);
                    //Log.d("UID"," uid is :: "+st);

                    if(itemSnapshot.child("itemSource").getValue().toString().toLowerCase().equals("home") &&
                    itemSnapshot.child("foodStatus").getValue().toString().toLowerCase().equals("na"))
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
                filter(s.toString());

            }
        });
    }

    private void filter(String text)
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


    }
