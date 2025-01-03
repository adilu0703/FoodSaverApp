package com.example.fyp_khanabachao;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OfferRequestReceived_AdapterClass extends RecyclerView.Adapter<OfferRequestViewHolder>{
    private Context PLcontext;
    private List<PostFoodDataModelClass> PLfoodlist;

    public OfferRequestReceived_AdapterClass(Context PLcontext, List<PostFoodDataModelClass> PLfoodlist) {
        this.PLcontext = PLcontext;
        this.PLfoodlist = PLfoodlist;
    }

    @NonNull
    @Override
    public OfferRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View PLmview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_postfooditem,viewGroup,false);
        return new OfferRequestViewHolder(PLmview);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferRequestViewHolder offerRequestViewHolder, int i) {
        offerRequestViewHolder.plTitle.setText(PLfoodlist.get(i).getPPitemName());
        offerRequestViewHolder.plQuantity.setText(PLfoodlist.get(i).getPPitemQuanity());
        offerRequestViewHolder.plTime.setText(PLfoodlist.get(i).getPPitemTime());
        offerRequestViewHolder.plAddress.setText((PLfoodlist.get(i).getPPitemAddress()));
        offerRequestViewHolder.plCity.setText((PLfoodlist.get(i).getPPitemCity()));


        //for onclick description of food item

        offerRequestViewHolder.plcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PLcontext,OfferRequestReceived_MyFoodDetail.class);
                intent.putExtra("name",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPitemName());
                intent.putExtra("time",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPitemTime());
                intent.putExtra("quantity",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPitemQuanity());
                intent.putExtra("address",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPitemAddress());
                intent.putExtra("city",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPitemCity());
                intent.putExtra("ppuserId",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPpuserId());
                intent.putExtra("keyvalue",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPkey());
                intent.putExtra("foodstatus",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPfoodStatus());
                intent.putExtra("requesterid",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPrequesterId());
                intent.putExtra("uploadername",PLfoodlist.get(offerRequestViewHolder.getAbsoluteAdapterPosition()).getPPuserName());

                PLcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PLfoodlist.size();
    }

    //called from foodUpload_01 for searching data
    public void searchedList(ArrayList<PostFoodDataModelClass> searchlist)
    {
        PLfoodlist = searchlist;
        notifyDataSetChanged();
    }
}

class OfferRequestViewHolder extends RecyclerView.ViewHolder
{

    TextView plTitle , plQuantity, plTime, plAddress, plCity;
    CardView plcardView;

    public OfferRequestViewHolder(@NonNull View itemView)
    {
        super(itemView);

        plTitle = itemView.findViewById(R.id.PLfooditemtitleid);
        plQuantity=itemView.findViewById(R.id.PLfooditemquantityid);
        plTime=itemView.findViewById(R.id.PLfooditemtime);
        plAddress=itemView.findViewById(R.id.PLfooditemaddressid);
        plCity=itemView.findViewById(R.id.PLfooditemcityid);


        plcardView=itemView.findViewById(R.id.PLfooditemcardviewid);
    }
}
