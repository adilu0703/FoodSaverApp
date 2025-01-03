package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.fyp_khanabachao.databinding.ActivityAppNotificationBinding;
import com.example.fyp_khanabachao.databinding.ActivityTestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppNotification extends DrawerBaseActivity {

    ActivityAppNotificationBinding activityAppNotificationBinding;

    EditText editTextsearch;
    String uidd;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;
    RecyclerView mRecyclerView;
    List<AppNotification_Model> notiflist;
    AppNotification_Adapter appNotification_adapter;
    AppNotification_Model mConsumerRegisterHelperClass;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notification);
        activityAppNotificationBinding= ActivityAppNotificationBinding.inflate(getLayoutInflater());
        setContentView(activityAppNotificationBinding.getRoot());
        notiflist = new ArrayList<>();

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);






        editTextsearch = findViewById(R.id.searchcharityuserid);
        mRecyclerView = findViewById(R.id.recyclerviewcharityuserid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AppNotification.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        //  progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading Charity Organizations ...");
        //progressDialog.show();

        appNotification_adapter = new AppNotification_Adapter(AppNotification.this,notiflist);
        mRecyclerView.setAdapter(appNotification_adapter);


        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();




       LoadNotifications();






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
    private void LoadNotifications() {

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("Notifications");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notiflist.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    AppNotification_Model model = ds.getValue(AppNotification_Model.class);
                    notiflist.add(model);

                }


                appNotification_adapter.notifyDataSetChanged();
                //progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Toast.makeText(CharityUsersList.this, "Food Uploaded", Toast.LENGTH_SHORT).show();
                // progressDialog.dismiss();
            }
        });
    }

    //called from MyAdapterFoodItem
    private void filter(String text)
    {
        ArrayList<AppNotification_Model> searchlist = new ArrayList<>();
        for(AppNotification_Model item : notiflist)
        {
            if(item.getsName().toLowerCase().contains(text.toLowerCase()))
            {
                searchlist.add(item);
            }
        }
        appNotification_adapter.searchedList(searchlist);

    }
}