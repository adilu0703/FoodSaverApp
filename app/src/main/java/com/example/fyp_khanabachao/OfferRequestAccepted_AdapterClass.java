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

public class OfferRequestAccepted_AdapterClass extends RecyclerView.Adapter<OfferAcceptViewHolder>{
    private Context PLcontext;
    private List<PostFoodDataModelClass> PLfoodlist;

    public OfferRequestAccepted_AdapterClass(Context PLcontext, List<PostFoodDataModelClass> PLfoodlist) {
        this.PLcontext = PLcontext;
        this.PLfoodlist = PLfoodlist;
    }

    @NonNull
    @Override
    public OfferAcceptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View PLmview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_postfooditem,viewGroup,false);
        return new OfferAcceptViewHolder(PLmview);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAcceptViewHolder offerAcceptViewHolder, int i) {
        offerAcceptViewHolder.plTitle.setText(PLfoodlist.get(i).getPPitemName());
        offerAcceptViewHolder.plQuantity.setText(PLfoodlist.get(i).getPPitemQuanity());
        offerAcceptViewHolder.plTime.setText(PLfoodlist.get(i).getPPitemTime());
        offerAcceptViewHolder.plAddress.setText((PLfoodlist.get(i).getPPitemAddress()));
        offerAcceptViewHolder.plCity.setText((PLfoodlist.get(i).getPPitemCity()));


        //for onclick description of food item

        offerAcceptViewHolder.plcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PLcontext,OfferRequestAccepted_MyFoodDetail.class);
                intent.putExtra("name",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemName());
                intent.putExtra("time",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemTime());
                intent.putExtra("quantity",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemQuanity());
                intent.putExtra("address",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemAddress());
                intent.putExtra("city",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemCity());
                intent.putExtra("ppuserId",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPpuserId());
                intent.putExtra("keyvalue",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPkey());
                intent.putExtra("foodstatus",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPfoodStatus());
                intent.putExtra("requesterid",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPrequesterId());
                intent.putExtra("uploadername",PLfoodlist.get(offerAcceptViewHolder.getAbsoluteAdapterPosition()).getPPuserName());

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

class OfferAcceptViewHolder extends RecyclerView.ViewHolder
{

    TextView plTitle , plQuantity, plTime, plAddress, plCity;
    CardView plcardView;

    public OfferAcceptViewHolder(@NonNull View itemView)
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
