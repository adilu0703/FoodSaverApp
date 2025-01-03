package com.example.fyp_khanabachao;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapterFoodItem extends RecyclerView.Adapter<FoodViewHolder>
{
    String myUid;
    private Context mcontext;
    private List<FoodDataModelClass> myfoodlist;


    public MyAdapterFoodItem(Context mcontext, List<FoodDataModelClass> myfoodlist)
    {
        this.mcontext = mcontext;
        this.myfoodlist = myfoodlist;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_fooditem,viewGroup,false);
        return new FoodViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i)
    {

       //foodViewHolder.mimageView.setImageResource(myfoodlist.get(i).getItemImage()); //for static img

        Glide.with(mcontext).load(myfoodlist.get(i).getItemImage()).into(foodViewHolder.mimageView);

        foodViewHolder.mTitle.setText(myfoodlist.get(i).getItemName());
        foodViewHolder.mDescription.setText(myfoodlist.get(i).getItemDescription());
        foodViewHolder.mPrice.setText(myfoodlist.get(i).getItemPrice());
        foodViewHolder.mExpiry.setText(myfoodlist.get(i).getItemExpiry());
        foodViewHolder.mQuantity.setText(myfoodlist.get(i).getItemQuanity());
        foodViewHolder.mSource.setText((myfoodlist.get(i).getItemSource()));
        foodViewHolder.mDelivery.setText((myfoodlist.get(i).getItemDelivery()));
        foodViewHolder.mAddress.setText((myfoodlist.get(i).getItemAddress()));
        foodViewHolder.mCity.setText((myfoodlist.get(i).getItemCity()));
        myUid= myfoodlist.get(i).getUserId();


        //for onclick description of food item

        foodViewHolder.mcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mcontext,FoodDetail.class);
                intent.putExtra("image",myfoodlist.get(foodViewHolder.getAdapterPosition()).getItemImage());

                intent.putExtra("name",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemName());
                intent.putExtra("description",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemDescription());

                intent.putExtra("price",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemPrice());
                intent.putExtra("expiry",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemExpiry());
                intent.putExtra("quantity",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemQuanity());
                intent.putExtra("source",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemSource());
                intent.putExtra("delivery",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemDelivery());
                intent.putExtra("address",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemAddress());
                intent.putExtra("city",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getItemCity());

                intent.putExtra("keyvalue",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getKey());
               intent.putExtra("foodstatus",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getFoodStatus());
                intent.putExtra("requesterid",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getRequesterId());
                intent.putExtra("uploadername",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getUploaderName());
                //intent.putExtra("requestername",myfoodlist.get(foodViewHolder.getAbsoluteAdapterPosition()).getRequesterName());


                mcontext.startActivity(intent);
            }
        });
    }





    @Override
    public int getItemCount() {
        return myfoodlist.size();
    }


    //called from foodUpload_01 for searching data
    public void searchedList(ArrayList<FoodDataModelClass> searchlist)
    {
        myfoodlist = searchlist;
        notifyDataSetChanged();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder
{

    ImageView mimageView;
    TextView mTitle, mDescription, mPrice, mQuantity, mExpiry,mSource,mDelivery, mAddress, mCity;
    CardView mcardView;

    public FoodViewHolder(@NonNull View itemView)
    {
        super(itemView);

        mimageView = itemView.findViewById(R.id.fooditemimgid);
        mTitle=itemView.findViewById(R.id.fooditemtitleid);
        mDescription=itemView.findViewById(R.id.fooditemdescriptionid);
        mPrice=itemView.findViewById(R.id.fooditempriceid);
        mQuantity=itemView.findViewById(R.id.fooditemquantityid);
        mExpiry=itemView.findViewById(R.id.fooditemexpiryid);
        mSource=itemView.findViewById(R.id.fooditemsourceid);
        mDelivery=itemView.findViewById(R.id.fooditemdeliveryid);
        mAddress=itemView.findViewById(R.id.fooditemaddressid);
        mCity=itemView.findViewById(R.id.fooditemcityid);


        mcardView=itemView.findViewById(R.id.fooditemcardviewid);
    }
}