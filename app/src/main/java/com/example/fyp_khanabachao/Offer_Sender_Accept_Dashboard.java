package com.example.fyp_khanabachao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_khanabachao.databinding.ActivityOfferSenderAcceptDashboardBinding;
import com.example.fyp_khanabachao.databinding.ActivityOfferSenderRequestDashboardBinding;
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

public class Offer_Sender_Accept_Dashboard extends DrawerBaseActivity {
    ActivityOfferSenderAcceptDashboardBinding activityOfferSenderAcceptDashboardBinding;
    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    EditText PLeditTextsearch;

    RecyclerView PLmRecyclerView;
    List<PostFoodDataModelClass> PLmFoodList;
    PostFoodDataModelClass PLmFoodDataModelClass;
    OfferSenderAccept_Adapter PLmyAdapterFoodItem;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_sender_accept_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityOfferSenderAcceptDashboardBinding= ActivityOfferSenderAcceptDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityOfferSenderAcceptDashboardBinding.getRoot());

        // bottom nav bar

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.postsenderreqnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.postsenderreqnavid:
                        startActivity(new Intent(getApplicationContext(),OfferSenderRequestDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.postsenderaccnavid:
                        startActivity(new Intent(getApplicationContext(),Offer_Sender_Accept_Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });
        //-------------------------------

        PLmRecyclerView = findViewById(R.id.recyclerviewpostconsumerdashboardid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Offer_Sender_Accept_Dashboard.this,1);
        PLmRecyclerView.setLayoutManager(gridLayoutManager);

        PLeditTextsearch = findViewById(R.id.PLsearchfoodid);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading food items ...");
        progressDialog.show();

        PLmFoodList = new ArrayList<>();

        PLmyAdapterFoodItem = new OfferSenderAccept_Adapter(Offer_Sender_Accept_Dashboard.this,PLmFoodList);
        PLmRecyclerView.setAdapter(PLmyAdapterFoodItem);

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();
        String userID = firebaseUser.getUid().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference("Post Food");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                PLmFoodList.clear();

                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren())
                {
                    if((itemSnapshot.child("pprequesterId").getValue().toString().equals(userID)) //foodprovider
                            && (itemSnapshot.child("ppfoodStatus").getValue().toString().toLowerCase().equals("accepted")))
                    {
                        PostFoodDataModelClass postfoodDataModelClass = itemSnapshot.getValue(PostFoodDataModelClass.class);
                        postfoodDataModelClass.setPPkey(itemSnapshot.getKey());  //for key
                        PLmFoodList.add(postfoodDataModelClass);
                    }}
                Collections.reverse(PLmFoodList);
                PLmyAdapterFoodItem.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
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
            public void afterTextChanged(Editable s)
            {
                searchFood(s.toString());

            }
        });

    }

    private void searchFood(String text)
    {
        ArrayList<PostFoodDataModelClass> searchlist = new ArrayList<>();
        for(PostFoodDataModelClass item : PLmFoodList)
        {
            if(item.getPPitemName().toLowerCase().contains(text.toLowerCase()))
            {
                searchlist.add(item);
            }
        }
        PLmyAdapterFoodItem.searchedList(searchlist);

    }

}

