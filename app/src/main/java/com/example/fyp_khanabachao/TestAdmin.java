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
import com.example.fyp_khanabachao.databinding.ActivityTestAdminBinding;
import com.example.fyp_khanabachao.databinding.ActivityTestBinding;
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

public class TestAdmin extends DrawerBaseActivityAdmin {
    ActivityTestAdminBinding activityTestAdminBinding;

    EditText editTextsearch;
    String uidd;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;
    List<ModelChatlist> chatlistList;
    RecyclerView mRecyclerView;
    List<ConsumerRegisterHelperClass> mFoodList;
    TestAdapter charityUsersAdapterClass;
    ConsumerRegisterHelperClass mConsumerRegisterHelperClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_admin);
        activityTestAdminBinding= ActivityTestAdminBinding.inflate(getLayoutInflater());
        setContentView(activityTestAdminBinding.getRoot());
        mFoodList = new ArrayList<>();

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);






        editTextsearch = findViewById(R.id.searchcharityuserid);
        mRecyclerView = findViewById(R.id.recyclerviewcharityuserid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(TestAdmin.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        //  progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading Charity Organizations ...");
        //progressDialog.show();
        chatlistList = new ArrayList<>();
        charityUsersAdapterClass = new TestAdapter(TestAdmin.this,mFoodList);
        mRecyclerView.setAdapter(charityUsersAdapterClass);


        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("CharityChatlist").child(firebaseUser.getUid());




        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chatlistList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                    chatlistList.add(chatlist);
                }
                LoadChats();

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
    private void LoadChats() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.d("Code", databaseReference.toString());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //  Log.d("Tag",snapshot.toString());
                for(DataSnapshot ds: snapshot.getChildren()){
                    ConsumerRegisterHelperClass user = ds.getValue(ConsumerRegisterHelperClass.class);
                    for(ModelChatlist chatlist: chatlistList){
                        //Log.d("Ma aa gaya", chatlistList.toString());
                        if(user.getUid()!=null && user.getUid().equals(chatlist.getId())){
                            // Log.d("Ma aa gaya","Hello");
                            mFoodList.add(user);
                            break;

                        }
                    }
                    charityUsersAdapterClass.notifyDataSetChanged();
                    Log.d("HELLO", mFoodList.toString());
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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