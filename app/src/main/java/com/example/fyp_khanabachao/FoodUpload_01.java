package com.example.fyp_khanabachao;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

public class FoodUpload_01 extends DrawerBaseActivity
{
   ActivityFoodUpload01Binding activityFoodUpload01Binding;
    EditText editTextsearch;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    RecyclerView mRecyclerView;
    List<FoodDataModelClass> mFoodList;
    MyAdapterFoodItem myAdapterFoodItem;
    FoodDataModelClass mFoodDataModelClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_upload01);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityFoodUpload01Binding = ActivityFoodUpload01Binding.inflate(getLayoutInflater());
        setContentView(activityFoodUpload01Binding.getRoot());

        editTextsearch = findViewById(R.id.searchfoodid);
        mRecyclerView = findViewById(R.id.recyclerviewfoodid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FoodUpload_01.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading food items ...");
        progressDialog.show();

        mFoodList = new ArrayList<>();


//--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.receiverreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.receiverreqnavid:
                        startActivity(new Intent(getApplicationContext(),RequestReceivedDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.receiveraccnavid:
                        startActivity(new Intent(getApplicationContext(),RequestAcceptedDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
//--------------------------------------------------------

       /** mFoodDataModelClass = new FoodDataModelClass("Kabab","Mild spicy 10 pcs kabab cooked today",
                "Rs.200","1","1 week if frozen",R.drawable.fpic2);
        mFoodList.add(mFoodDataModelClass);

        mFoodDataModelClass = new FoodDataModelClass("Pizza","Half large pizza",
                "Rs.600","1","3 days",R.drawable.fpic3);
        mFoodList.add(mFoodDataModelClass);

        mFoodDataModelClass = new FoodDataModelClass("Baryani","1 plate biryani",
                "Rs.500","1","5 days",R.drawable.fpic1);
        mFoodList.add(mFoodDataModelClass);*/

        myAdapterFoodItem = new MyAdapterFoodItem(FoodUpload_01.this,mFoodList);
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
                   String st = itemSnapshot.child("userId").getValue().toString();

                  // Log.d("KEY"," key :: "+itemSnapshot);
                   //key :: DataSnapshot { key = 23 Mar 2022 12:13:24 am, value = {foodStatus=na, itemAddress=jsms, itemExpiry=3 days, myCurrentDateTime=23 Mar 2022 12:13:24 am, itemDelivery=Yes, userId=IgQp2GPpXePlisBkBPMU7urxfei1, itemImage=https://firebasestorage.googleapis.com/v0/b/
                   //Log.d("UID"," uid is :: "+st);
                   //Log.d("CHILD"," key :: "+ dataSnapshot);
                   //key :: DataSnapshot { key = Public Food, value = {23 Mar 2022 12:13:24 am={foodStatus=na, itemAddress=jsms, itemExpiry=3 days, myCurrentDateTime=23 Mar 2022 12:13:24 am, itemDelivery=Yes, userId=IgQp2GPpXePlisBkBPMU7urxfei1, itemImage=https:/

                   if(itemSnapshot.child("userId").getValue().toString().equals(firebaseUser.getUid()))
                   {
                       FoodDataModelClass foodDataModelClass = itemSnapshot.getValue(FoodDataModelClass.class);

                       foodDataModelClass.setKey(itemSnapshot.getKey());  //for key -- time

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

    //called from MyAdapterFoodItem
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

    public void btnFloatUploadFood(View view)
    {
        Intent intent = new Intent(FoodUpload_01.this,MyFoodUpload.class);

        startActivity(intent);
    }
}