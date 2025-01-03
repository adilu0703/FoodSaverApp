package com.example.fyp_khanabachao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.example.fyp_khanabachao.databinding.ActivitySenderRequestDashboardBinding;
import com.example.fyp_khanabachao.databinding.ActivityUpdateMyFoodBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class updateMyFood extends DrawerBaseActivity
{
    ActivityUpdateMyFoodBinding activityUpdateMyFoodBinding;
    public static final int camera_permission_code = 101;
    public static final int camera_request_code = 102;

    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    LayoutInflater inflater;

    FoodDataModelClass foodDataModelClass;

    ImageView foodimage;
    Uri uri;
    TextInputLayout foodname,fooddescription,foodquantity,foodexpiry,foodprice,foodaddress,foodcity,foodsource,fooddelivery;

    RadioGroup foodsourcerg;
    RadioButton foodsourcerb;
    RadioGroup deliveryrg;
    RadioButton deliveryrb;

    String foodimageurl;
    String key;
    String foodStatus;
    String Requesterid="";
    String Uploadername="";
    String Requestername="";


    String foodName,foodDescription, foodSource, foodQuantity, foodDelivery, foodAddress, foodCity, foodPrice,foodExpiry;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    FirebaseUser firebaseUser;
    FirebaseAuth cauth;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_food);
        activityUpdateMyFoodBinding = ActivityUpdateMyFoodBinding.inflate(getLayoutInflater());
        setContentView(activityUpdateMyFoodBinding.getRoot());


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
                        startActivity(new Intent(getApplicationContext(),FoodUpload_01.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.providernavid:
                        startActivity(new Intent(getApplicationContext(),MyFoodUpload.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
//--------------------------------------------------------


        builder = new AlertDialog.Builder(updateMyFood.this);
        inflater = this.getLayoutInflater();


        cauth = FirebaseAuth.getInstance();
        firebaseUser = cauth.getCurrentUser();

        foodimage = findViewById(R.id.updatefoodimgid);
        foodname = findViewById(R.id.updatefoodnameid);
        fooddescription = findViewById(R.id.updatefooddescriptionid);
        foodquantity = findViewById(R.id.updatefoodquantityid);
        foodexpiry = findViewById(R.id.updatefoodexpiryid);
        foodprice = findViewById(R.id.updatefoodpriceid);

        foodsourcerg = findViewById(R.id.foodsourceid);
        deliveryrg = findViewById(R.id.fooddeliveryid);

        foodaddress = findViewById(R.id.updatefoodaddressid);
        foodcity = findViewById(R.id.updatefoodcityid);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Glide.with(this).load(bundle.getString("oldimageurl")).into(foodimage);

            foodname.getEditText().setText(bundle.getString("nameu"));
            fooddescription.getEditText().setText(bundle.getString("detailu"));
            foodquantity.getEditText().setText(bundle.getString("quantityu"));
            foodexpiry.getEditText().setText(bundle.getString("expiryu"));

           // foodsource.getEditText().setText(bundle.getString("sourceu"));
           // fooddelivery.getEditText().setText(bundle.getString("deliveryu"));

            foodprice.getEditText().setText(bundle.getString("priceu"));
            foodaddress.getEditText().setText(bundle.getString("addressu"));
            foodcity.getEditText().setText(bundle.getString("cityu"));

            foodimageurl = bundle.getString("oldimageurl");
            key = bundle.getString("keyu");
            foodStatus = bundle.getString("foodstatus");
            Requesterid = bundle.getString("requesterid");
            Uploadername = bundle.getString("uploadername");
           // Requestername=bundle.getString("requestername");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Public Food").child(key);
        //Toast.makeText(updateMyFood.this,"KEY :: "+ key,Toast.LENGTH_LONG).show();

    }

    public void btnUpdateFood(View view)
    {
        if(!validateName() | !validateDescription() | !validateQuantity() | !validateExpiry() | !validatePrice() | !validateImage() | !validateSource()
                | !validateDelivery() | !validateAddress() | !validateCity())
        {
            return;
        }

         progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating ....");
        progressDialog.show();


        foodName = foodname.getEditText().getText().toString().trim();
        foodDescription = fooddescription.getEditText().getText().toString().trim();
        foodQuantity = foodquantity.getEditText().getText().toString().trim();
        foodExpiry = foodexpiry.getEditText().getText().toString().trim();
        foodPrice = foodprice.getEditText().getText().toString().trim();

        //uploading food source using radiobtn
        int radioid = foodsourcerg.getCheckedRadioButtonId();
        foodsourcerb = findViewById(radioid);
        foodSource = foodsourcerb.getText().toString();

        // String foodDelivery = fooddelivery.getEditText().getText().toString();
        int radioidD = deliveryrg.getCheckedRadioButtonId();
        deliveryrb = findViewById(radioidD);
        foodDelivery = deliveryrb.getText().toString();

        foodAddress = foodaddress.getEditText().getText().toString().trim();
        foodCity = foodcity.getEditText().getText().toString().trim();

        if( uri != null) {
            uploadImgFb();
        }
        else
        {
            uploadFood();
        }
    }



    public void btnUpdateFoodImage(View view)  // button to open dialog to update img
    {
        View dialogView = inflater.inflate(R.layout.alert_dialog_upload_img,null);
        // builder.setCancelable(false);
        //  builder.setNegativeButton("cancel",null);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();

    }




   public void uploadImgFb()   // to firebase
    {


        storageReference = FirebaseStorage.getInstance().getReference().child("FoodImage").child(uri.getLastPathSegment());

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
        ///uploadFood();
    }



    public void uploadFood()  // to firebase using model class
    {



        String userID = firebaseUser.getUid().toString();
        Log.d("bn","value id :: "+userID);



        foodDataModelClass = new FoodDataModelClass(foodName,foodDescription,foodPrice,foodQuantity,
                foodExpiry,foodimageurl,userID,foodSource,foodDelivery,foodAddress,foodCity,foodStatus,key, Requesterid, Uploadername);


        databaseReference.setValue(foodDataModelClass).addOnCompleteListener(new OnCompleteListener<Void>()
         {
           @Override
           public void onComplete(@NonNull Task<Void> task)
           {

               if(task.isSuccessful())
               {
                   Toast.makeText(updateMyFood.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
               else
               {
                   Toast.makeText(updateMyFood.this, "Food Updatting ERROR : "+ task.getException().getMessage(),
                           Toast.LENGTH_LONG).show();

               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e)
           {
               Toast.makeText(updateMyFood.this,"Updation Failed",Toast.LENGTH_SHORT).show();
               Log.d("bn","upd error :: "+ e.getMessage());

           }
       });

     Intent intent = new Intent(updateMyFood.this, FoodUpload_01.class);
     startActivity(intent);

    }




    ////////////////// ================ IMAGE

    // using camera img

    public void btnCameraImage(View view)  // button of camera icon in alert dialog
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

        if(requestCode == camera_request_code && resultCode == RESULT_OK)
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

        else if( requestCode == 1 && resultCode == RESULT_OK)
        {
            uri = data.getData();
            foodimage.setImageURI(uri);  // img will be set on img view
            Toast.makeText(this, "Gallery image uploaded ", Toast.LENGTH_SHORT).show();
            Log.d("uri", "GALLEY URI IS :: "+uri);
        }

    }


    // ========================= VALIDATIONS

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
            Toast.makeText(updateMyFood.this,"Food source not selected",Toast.LENGTH_LONG).show();
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
            Toast.makeText(updateMyFood.this,"Delivery availability not selected",Toast.LENGTH_LONG).show();
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



}