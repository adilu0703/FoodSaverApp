package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fyp_khanabachao.databinding.ActivityMyFoodUploadBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MyFoodUpload extends DrawerBaseActivity
{
    ActivityMyFoodUploadBinding activityMyFoodUploadBinding;
    public static final int camera_permission_code = 101;
     public static final int camera_request_code = 102;

     AlertDialog alertDialog;
    AlertDialog.Builder builder;
    LayoutInflater inflater;

    ImageView foodimage;
    Uri uri;
    TextInputLayout foodname,fooddescription,foodquantity,foodexpiry,foodprice,foodaddress,foodcity,uploadername;

    RadioGroup foodsourcerg;
    RadioButton foodsourcerb;
    RadioGroup deliveryrg;
    RadioButton deliveryrb;
    String userID,hisuid;
    String foodimageurl;

    List<String> mFoodList;
    adminUserListAdapter charityUsersAdapterClass;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_upload);

        activityMyFoodUploadBinding = ActivityMyFoodUploadBinding.inflate(getLayoutInflater());
        setContentView(activityMyFoodUploadBinding.getRoot());

        builder = new AlertDialog.Builder(MyFoodUpload.this);
        inflater = this.getLayoutInflater();
        mFoodList = new ArrayList<String>();

        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();


        foodimage = findViewById(R.id.uploadfoodimgid);
        foodname = findViewById(R.id.foodnameid);
        fooddescription =findViewById(R.id.fooddescriptionid);
        foodquantity = findViewById(R.id.foodquantityid);
        foodexpiry = findViewById(R.id.foodexpiryid);
        foodprice = findViewById(R.id.foodpriceid);
        uploadername = findViewById(R.id.uploadernameid);

        //foodsource = findViewById(R.id.foodsourceid);
        foodsourcerg = findViewById(R.id.foodsourceid);

       // fooddelivery = findViewById(R.id.fooddeliveryid);
        deliveryrg = findViewById(R.id.fooddeliveryid);

         foodaddress= findViewById(R.id.foodaddressid);
          foodcity= findViewById(R.id.foodcityid);


        //--------------------------------------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationid);
        bottomNavigationView.setSelectedItemId(R.id.consumernavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.consumernavid:
                        startActivity(new Intent(getApplicationContext(),ConsumerDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.providernavid:
                        startActivity(new Intent(getApplicationContext(),FoodUpload_01.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
//--------------------------------------------------------
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFoodList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren())
                {
                    ConsumerRegisterHelperClass consumerRegisterHelperClass = itemSnapshot.getValue(ConsumerRegisterHelperClass.class);
                    mFoodList.add(itemSnapshot.getKey());
                }
                Log.d("Alluid", mFoodList.toString());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Toast.makeText(CharityUsersList.this, "Food Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });




    }






// using camera img

    public void btnCameraImage(View view)  // button of camera icon in alert_dialog_upload_img
    {
        askCameraPermission();
    }

    private void askCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},camera_permission_code);
        }
        else
        {
            openCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == camera_permission_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openCamera();

            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera()
    {
        //Toast.makeText(this, "Camera open request", Toast.LENGTH_SHORT).show();

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,camera_request_code);

    }



// -------------------------------------using gallery img

    public void btnGalleryImage(View view)  // button uploading gallery img
    {
       Intent photopicker = new Intent(Intent.ACTION_PICK);
        photopicker.setType("image/*"); //all types of imges can be chosen
        alertDialog.cancel();
        startActivityForResult(photopicker,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)  //set imageview using pic and will get uri
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == camera_request_code && resultCode == RESULT_OK)  //for cam
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);
            uri = Uri.parse(path);
            foodimage.setImageURI(uri);
            alertDialog.cancel();



            Toast.makeText(this, "Camera image uploaded ", Toast.LENGTH_SHORT).show();
            Log.d("uri", "camera URI IS :: "+uri);
        }

        else if( requestCode == 1 && resultCode == RESULT_OK)  //for gallery
        {
            uri = data.getData();
            foodimage.setImageURI(uri);  // img will be set on img view
            Toast.makeText(this, "Gallery image uploaded ", Toast.LENGTH_SHORT).show();
            Log.d("uri", "GALLEY URI IS :: "+uri);
        }

    }

    // ---------------------


    public void btnUploadFood(View view)   //button uploading food
    {
        if(!validateName() | !validateDescription() | !validateQuantity() | !validateExpiry() | !validatePrice() | !validateImage() | !validateDelivery()
           | !validateSource() | !validateAddress() | !validateCity() | !validateUploaderName())
        {
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();
        uploadImgFb();
    }

    public void uploadImgFb()   // to firebase
    {
       StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("FoodImage").child(uri.getLastPathSegment());
       storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
             Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
             while(!uriTask.isComplete());
             Uri urlImage = uriTask.getResult();
             foodimageurl = urlImage.toString();
             uploadFood();
            }
        });
    }

    public void uploadFood()  // to firebase using model class
    {
        String foodName = foodname.getEditText().getText().toString();
        String foodDescription = fooddescription.getEditText().getText().toString();
        String foodQuantity = foodquantity.getEditText().getText().toString();
        String foodExpiry = foodexpiry.getEditText().getText().toString();
        String foodPrice = foodprice.getEditText().getText().toString();
        String uploaderName = uploadername.getEditText().getText().toString();


        //uploading food source using radiobtn
        int radioid = foodsourcerg.getCheckedRadioButtonId();
        foodsourcerb = findViewById(radioid);
        String foodSource = foodsourcerb.getText().toString();

       // String foodDelivery = fooddelivery.getEditText().getText().toString();
        int radioidD = deliveryrg.getCheckedRadioButtonId();
        deliveryrb = findViewById(radioidD);
        String foodDelivery = deliveryrb.getText().toString();

        String foodAddress = foodaddress.getEditText().getText().toString();
        String foodCity = foodcity.getEditText().getText().toString();


    userID = firebaseUser.getUid().toString();
        Log.d("myuid", userID.toString());


        String foodStatus  = "na"; //for request
        String requesterId="na";
        //String requesterName="na";


        String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());  //used as main parent id

        FoodDataModelClass foodDataModelClass = new FoodDataModelClass(foodName,foodDescription,foodPrice,foodQuantity,foodExpiry,foodimageurl,userID
        ,foodSource, foodDelivery, foodAddress, foodCity, foodStatus, myCurrentDateTime, requesterId, uploaderName);

        FirebaseDatabase.getInstance().getReference("Public Food").child(myCurrentDateTime).setValue(foodDataModelClass) // public food
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MyFoodUpload.this, "Food Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            for(int i=0 ; i<mFoodList.size();i++){
                                sendhisNotification(""+ mFoodList.get(i),""+userID,"Uploaded new Food item");
                            }


                        }
                        else
                        {
                            Toast.makeText(MyFoodUpload.this, "Food Uploading ERROR : "+ task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    private Boolean validateName() {
        String name = foodname.getEditText().getText().toString();

        if (name.isEmpty()) {
            foodname.setError("Field cannot be empty");
            return false;
        } else {
            foodname.setError(null);
            foodname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUploaderName() {
        String name = uploadername.getEditText().getText().toString();

        if (name.isEmpty()) {
            uploadername.setError("Field cannot be empty");
            return false;
        } else {
            uploadername.setError(null);
            uploadername.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateDescription() {
        String desc = fooddescription.getEditText().getText().toString();

        if (desc.isEmpty()) {
            fooddescription.setError("Field cannot be empty");
            return false;
        } else {
            fooddescription.setError(null);
            fooddescription.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateQuantity() {
        String quan = foodquantity.getEditText().getText().toString();

        if (quan.isEmpty()) {
            foodquantity.setError("Field cannot be empty");
            return false;
        } else {
            foodquantity.setError(null);
            foodquantity.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateExpiry() {
        String expiry = foodexpiry.getEditText().getText().toString();

        if (expiry.isEmpty()) {
            foodexpiry.setError("Field cannot be empty");
            return false;
        } else {
            foodexpiry.setError(null);
            foodexpiry.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePrice() {
        String price = foodprice.getEditText().getText().toString();

        if (price.isEmpty()) {
            foodprice.setError("Field cannot be empty");
            return false;
        }

        else {
            foodprice.setError(null);
            foodprice.setErrorEnabled(false);
            return true;
        }

    }

  /*  private Boolean validateSource(TextInputLayout Text, String[] targetValue)
    {
        String src = Text.getEditText().getText().toString();
        src = src.toLowerCase();

        for (String s : targetValue)
        {
            if (src.equals(s))
            {
                Text.setError(null);
                Text.setErrorEnabled(false);
                return true;
            }
        }
        if (src.isEmpty())
        {
            Text.setError("Field cannot be empty");
            return false;
        }
        Text.setError("Enter valid input YES or NO");
        return false;
    }*/

       private Boolean validateSource()
    {
        int radioid = foodsourcerg.getCheckedRadioButtonId();;

        if ( radioid == -1 )
        {
            Toast.makeText(MyFoodUpload.this,"Food source not selected",Toast.LENGTH_LONG).show();
            return false;
        }
       else
           return true;
    }


    private Boolean validateDelivery()
    {
        int radioidD = deliveryrg.getCheckedRadioButtonId();;

        if ( radioidD == -1 )
        {
            Toast.makeText(MyFoodUpload.this,"Delivery availability not selected",Toast.LENGTH_LONG).show();
            return false;
        }
       else
           return true;
    }

    private Boolean validateAddress()
    {
        String address = foodaddress.getEditText().getText().toString();

        if (address.isEmpty())
        {
            foodaddress.setError("Field cannot be empty");
            return false;
        }
        else
            {
            foodaddress.setError(null);
            foodaddress.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateCity() {
        String city = foodcity.getEditText().getText().toString();

        if (city.isEmpty()) {
            foodcity.setError("Field cannot be empty");
            return false;
        } else {
            foodcity.setError(null);
            foodcity.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateImage()
    {
        if (foodimage.getDrawable()==null)
        {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    public void btnMyFoodListing(View view)
    {
        Intent intent = new Intent(MyFoodUpload.this,FoodUpload_01.class);
        startActivity(intent);
    }

    public void uploadingImage(View view)  // btn will open alert dialog showing 2 opitions cam and gallery
    {
        View dialogView = inflater.inflate(R.layout.alert_dialog_upload_img,null);
       // builder.setCancelable(false);
      //  builder.setNegativeButton("cancel",null);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();

    }
    private void sendhisNotification(String hisUid,String pId,String notification) {
        String timestamp = "" + System.currentTimeMillis();
        //data to put in firebase
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("pId", pId);
        hashMap.put("timestamp", timestamp);
        hashMap.put("pUid", hisUid);
        hashMap.put("notification", notification);
        hashMap.put("sUid", userID);

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