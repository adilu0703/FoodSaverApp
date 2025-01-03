package com.example.fyp_khanabachao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_khanabachao.databinding.ActivityOfferRequestAcceptedDashboardBinding;
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

public class OfferRequestAcceptedDashboard extends DrawerBaseActivity {
    EditText PLeditTextsearch;
    ActivityOfferRequestAcceptedDashboardBinding activityOfferRequestAcceptedDashboardBinding;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    RecyclerView PLmRecyclerView;
    List<PostFoodDataModelClass> PLmFoodList;
    OfferRequestAccepted_AdapterClass PLmyAdapterFoodItem;
    PostFoodDataModelClass PLmFoodDataModelClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_request_accepted_dashboard);
        activityOfferRequestAcceptedDashboardBinding = ActivityOfferRequestAcceptedDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityOfferRequestAcceptedDashboardBinding.getRoot());

        PLeditTextsearch = findViewById(R.id.PLsearchfoodid);

        PLmRecyclerView = findViewById(R.id.PLrecyclerviewfoodid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(OfferRequestAcceptedDashboard.this, 1);
        PLmRecyclerView.setLayoutManager(gridLayoutManager);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading food items ...");
        progressDialog.show();

        PLmFoodList = new ArrayList<>();
        //--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.postreceiverreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.postreceiverreqnavid:
                        startActivity(new Intent(getApplicationContext(), OfferRequestReceivedDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.postreceiveraccnavid:
                        startActivity(new Intent(getApplicationContext(), OfferRequestAcceptedDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
//--------------------------------------------------------

        PLmyAdapterFoodItem = new OfferRequestAccepted_AdapterClass(OfferRequestAcceptedDashboard.this, PLmFoodList);
        PLmRecyclerView.setAdapter(PLmyAdapterFoodItem);

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Post Food");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PLmFoodList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                    String st = itemSnapshot.child("ppuserId").getValue().toString();

                    Log.d("KEY", " key :: " + itemSnapshot);
                    Log.d("UID", " uid is :: " + st);

                    if (itemSnapshot.child("ppuserId").getValue().toString().equals(firebaseUser.getUid()) && itemSnapshot.child("ppfoodStatus").getValue().toString().toLowerCase().equals("accepted")) {
                        PostFoodDataModelClass postfoodDataModelClass = itemSnapshot.getValue(PostFoodDataModelClass.class);

                        postfoodDataModelClass.setPPkey(itemSnapshot.getKey());  //for key

                        PLmFoodList.add(postfoodDataModelClass);
                    }
                }
                Collections.reverse(PLmFoodList);
                PLmyAdapterFoodItem.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });


        // search food implementation

        PLeditTextsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
    }

    private void filter(String text) {
        ArrayList<PostFoodDataModelClass> searchlist = new ArrayList<>();
        for (PostFoodDataModelClass item : PLmFoodList) {
            if (item.getPPitemName().toLowerCase().contains(text.toLowerCase())) {
                searchlist.add(item);
            }
        }
        PLmyAdapterFoodItem.searchedList(searchlist);

    }



}