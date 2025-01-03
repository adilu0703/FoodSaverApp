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

import java.util.ArrayList;
import java.util.List;

public class RequestAccepted_AdapterClass extends RecyclerView.Adapter<RequestAcceptViewHolder>
{
    private Context mcontext;
    private List<FoodDataModelClass> myfoodlist;

    public RequestAccepted_AdapterClass(Context mcontext, List<FoodDataModelClass> myfoodlist) {
        this.mcontext = mcontext;
        this.myfoodlist = myfoodlist;
    }

    @NonNull
    @Override
    public RequestAcceptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_fooditem,viewGroup,false);
        return new RequestAcceptViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAcceptViewHolder requestViewHolder, int i)
    {
        Glide.with(mcontext).load(myfoodlist.get(i).getItemImage()).into(requestViewHolder.mimageView);

        requestViewHolder.mTitle.setText(myfoodlist.get(i).getItemName());
        requestViewHolder.mDescription.setText(myfoodlist.get(i).getItemDescription());
        requestViewHolder.mPrice.setText(myfoodlist.get(i).getItemPrice());
        requestViewHolder.mExpiry.setText(myfoodlist.get(i).getItemExpiry());
        requestViewHolder.mQuantity.setText(myfoodlist.get(i).getItemQuanity());
        requestViewHolder.mSource.setText((myfoodlist.get(i).getItemSource()));
        requestViewHolder.mDelivery.setText((myfoodlist.get(i).getItemDelivery()));
        requestViewHolder.mAddress.setText((myfoodlist.get(i).getItemAddress()));
        requestViewHolder.mCity.setText((myfoodlist.get(i).getItemCity()));


        //for onclick description of food item

        requestViewHolder.mcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mcontext,RequestAccepted_MyFoodDetail.class);
                intent.putExtra("image",myfoodlist.get(requestViewHolder.getAdapterPosition()).getItemImage());

                intent.putExtra("name",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemName());
                intent.putExtra("description",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemDescription());

                intent.putExtra("price",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemPrice());
                intent.putExtra("expiry",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemExpiry());
                intent.putExtra("quantity",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemQuanity());
                intent.putExtra("source",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemSource());
                intent.putExtra("delivery",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemDelivery());
                intent.putExtra("address",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemAddress());
                intent.putExtra("city",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getItemCity());

                intent.putExtra("keyvalue",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getKey());
                intent.putExtra("foodstatus",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getFoodStatus());
                intent.putExtra("requesterid",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getRequesterId());
                intent.putExtra("uploaderid",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getUserId());
                intent.putExtra("uploadername",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getUploaderName());
              //  intent.putExtra("requestername",myfoodlist.get(requestViewHolder.getAbsoluteAdapterPosition()).getRequesterName());
                mcontext.startActivity(intent);
            }
        });

    }
    public void searchedList(ArrayList<FoodDataModelClass> searchlist)
    {
        myfoodlist = searchlist;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return myfoodlist.size();
    }
}

class RequestAcceptViewHolder extends RecyclerView.ViewHolder
{

    ImageView mimageView;
    TextView mTitle, mDescription, mPrice, mQuantity, mExpiry,mSource,mDelivery, mAddress, mCity;
    CardView mcardView;

    public RequestAcceptViewHolder(@NonNull View itemView)
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