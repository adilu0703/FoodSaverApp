package com.example.fyp_khanabachao;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fyp_khanabachao.databinding.ActivityCharityUsersListBinding;
import com.example.fyp_khanabachao.databinding.ActivityChatBinding;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    Toolbar Chattoolbar;
    RecyclerView chatRecyclerView;
    ImageView chatImageview , imageView1;
    private String fullScreenInd;
    TextView nameTv;
    EditText messageEt;
    ImageButton sendBtn ,sendImg;


    boolean notify=false;
    String hisUid , hisName,Locmsg;
    String myUid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference UsersDbRef;
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
    AdapterChat adapterChat;
    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;
    Uri image_uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Chattoolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(Chattoolbar);
        //Chattoolbar.setTitle("");
        chatRecyclerView = findViewById(R.id.chatRecyclerview);
        chatImageview = findViewById(R.id.profileTv);
        imageView1 = findViewById(R.id.messageIv);
        nameTv = findViewById(R.id.PnameTv);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);
        sendImg = findViewById(R.id.sendImg);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        //create api service



        Intent intent1=getIntent();
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        hisName = intent.getExtras().get("hisName").toString();
        Locmsg = intent1.getExtras().get("add").toString();
        nameTv.setText(hisName);
        messageEt.setText(Locmsg);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        UsersDbRef = firebaseDatabase.getReference("Users");


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify=true;
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "Cannot send empty message", Toast.LENGTH_SHORT).show();

                } else {
                    sendMessage(message);
                }
                messageEt.setText("");
            }

        });
        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickDialog();
            }
        });

        readMessages();
        seenMessage();
    }
    private void ImagePickDialog(){
        String[] options = {"Camera" , "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int opt) {
            if(opt==0)  {
             if(!CheckCameraPersmission()){
                 RequestCameraPermission();
             }
             else{
                 PickFromCamera();
             }
            }
                if(opt==1)  {
                    if(!CheckStoragePersmission()){
                        RequestStoragePermission();
                    }
                    else{
                        PickFromGallery();
                    }

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private  void PickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }
    private void PickFromCamera(){
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }
    private boolean CheckStoragePersmission(){
        boolean result1= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result1;
    }
    private void RequestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);

    }
    private boolean CheckCameraPersmission(){
        boolean result1= ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result2= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result1 && result2;
    }
    private void RequestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }


    private void seenMessage(){
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot ds: snapshot.getChildren()){
                ModelChat chat = ds.getValue(ModelChat.class);
                if(chat.getReceiver().equals(myUid)&& chat.getSender().equals(hisUid)){
                    HashMap<String,Object> hasSeenHashmap = new HashMap<>();
                    hasSeenHashmap.put("isSeen",true);
                    ds.getRef().updateChildren(hasSeenHashmap);

                }
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void readMessages(){
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid)&& chat.getSender().equals(hisUid)||
                    chat.getReceiver().equals(hisUid)&& chat.getSender().equals(myUid)){
                    chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this,chatList);
                adapterChat.notifyDataSetChanged();
                chatRecyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("sender",myUid);
        hashmap.put("receiver",hisUid);
        hashmap.put("message",message);
        hashmap.put("type","text");
        hashmap.put("timestamp",timestamp);
        hashmap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashmap);



       DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("CharityChatlist")
                .child(myUid).child(hisUid);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(!snapshot.exists()){
                 chatRef1.child("id").setValue(hisUid);
             }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("CharityChatlist")
                .child(hisUid).child(myUid);
        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef2.child("id").setValue(myUid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendhisNotification(""+hisUid,""+myUid,"Sent you a new message");

    }



    private void sendImg(Uri image_uri) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending image...");
        progressDialog.show();

        String timeStamp = ""+System.currentTimeMillis();
        String fileNameAndPath = "ChatImages/"+"post_"+timeStamp;

        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),image_uri);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] data = bos.toByteArray();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
            ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    String downloadUri = uriTask.getResult().toString();
                    if(uriTask.isSuccessful()){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        HashMap<String , Object> hashmap = new HashMap<>();
                        hashmap.put("sender",myUid);
                        hashmap.put("receiver",hisUid);
                        hashmap.put("message",downloadUri);
                        hashmap.put("type","image");
                        hashmap.put("timestamp",timeStamp);
                        hashmap.put("isSeen",false);

                        databaseReference.child("Chats").push().setValue(hashmap);
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
   private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
      myUid = user.getUid();
        }
        else{
            startActivity(new Intent(this ,MainActivity.class));
            finish();
        }


    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted && storageAccepted){
                    PickFromCamera();
                }
                else{
                    Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show();

                }
                }else{

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{

                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        PickFromGallery();
                    }
                    else{
                        Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show();

                    }
                }else{

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                sendImg(image_uri);
            }
           else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                sendImg(image_uri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    public void ImageinfullScreen(View view) {
        Intent fullscreenintent = new Intent(this, FullScreenImage.class);
        fullscreenintent.setData((image_uri));
        startActivity(fullscreenintent);
    }
    private void sendhisNotification(String hisUid,String pId,String notification) {
        String timestamp = "" + System.currentTimeMillis();
        //data to put in firebase
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("pId", pId);
        hashMap.put("timestamp", timestamp);
        hashMap.put("pUid", hisUid);
        hashMap.put("notification", notification);
        hashMap.put("sUid", myUid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(hisUid).child("Notifications").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}