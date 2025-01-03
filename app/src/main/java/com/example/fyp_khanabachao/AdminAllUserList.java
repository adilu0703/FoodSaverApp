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
import android.widget.EditText;

import com.CharityUsersList;
import com.example.CharityUsersAdapterClass;
import com.example.fyp_khanabachao.databinding.ActivityAdminAllUserListBinding;
import com.example.fyp_khanabachao.databinding.ActivityCharityUsersListBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminAllUserList extends DrawerBaseActivityAdmin {
    ActivityAdminAllUserListBinding activityAdminAllUserListBinding;
    EditText editTextsearch;
    String uidd;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    RecyclerView mRecyclerView;
    List<ConsumerRegisterHelperClass> mFoodList;
    adminUserListAdapter charityUsersAdapterClass;
    ConsumerRegisterHelperClass mConsumerRegisterHelperClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_user_list);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityAdminAllUserListBinding= ActivityAdminAllUserListBinding.inflate(getLayoutInflater());
        setContentView(activityAdminAllUserListBinding.getRoot());


        editTextsearch = findViewById(R.id.allusersadminsearch);

        mRecyclerView = findViewById(R.id.recyclerviewallusersid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminAllUserList.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mFoodList = new ArrayList<>();

        charityUsersAdapterClass = new adminUserListAdapter(AdminAllUserList.this,mFoodList);
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

                        ConsumerRegisterHelperClass consumerRegisterHelperClass = itemSnapshot.getValue(ConsumerRegisterHelperClass.class);
                        mFoodList.add(consumerRegisterHelperClass);

                }


                charityUsersAdapterClass.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Toast.makeText(CharityUsersList.this, "Food Uploaded", Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
            }
        });




        // search food implementation

        editTextsearch.addTextChangedListener(new TextWatcher() {   // get entered string
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