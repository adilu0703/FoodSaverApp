package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.fyp_khanabachao.databinding.ActivityAdminAllUserListBinding;
import com.example.fyp_khanabachao.databinding.ActivityAdminWarningUserListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminWarningUserList extends DrawerBaseActivityAdmin
{
    ActivityAdminWarningUserListBinding activityAdminWarningUserListBinding;
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
        setContentView(R.layout.activity_admin_warning_user_list);

        activityAdminWarningUserListBinding= ActivityAdminWarningUserListBinding.inflate(getLayoutInflater());
        setContentView(activityAdminWarningUserListBinding.getRoot());
        editTextsearch = findViewById(R.id.allusersadminsearch);
        mRecyclerView = findViewById(R.id.recyclerviewallusersid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminWarningUserList.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


         progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Charity Organizations ...");
        progressDialog.show();

        mFoodList = new ArrayList<>();
        charityUsersAdapterClass = new adminUserListAdapter(AdminWarningUserList.this,mFoodList);
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
                    float r = itemSnapshot.child("rating").getValue(Float.class);
                    if(r>0 && r<3)
                    {
                        ConsumerRegisterHelperClass consumerRegisterHelperClass = itemSnapshot.getValue(ConsumerRegisterHelperClass.class);
                        mFoodList.add(consumerRegisterHelperClass);
                    }
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