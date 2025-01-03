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

public class OfferSenderAccept_Adapter extends RecyclerView.Adapter<SenderofferAcceptViewHolder>{
    private Context PLcontext;
    private List<PostFoodDataModelClass> PLfoodlist;

    public OfferSenderAccept_Adapter(Context PLcontext, List<PostFoodDataModelClass> PLfoodlist) {
        this.PLcontext = PLcontext;
        this.PLfoodlist = PLfoodlist;
    }

    @NonNull
    @Override
    public SenderofferAcceptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View PLmview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_public_postfooditem,viewGroup,false);
        return new SenderofferAcceptViewHolder(PLmview);
    }

    @Override
    public void onBindViewHolder(@NonNull SenderofferAcceptViewHolder senderofferAcceptViewHolder, int i) {
        senderofferAcceptViewHolder.plTitle.setText(PLfoodlist.get(i).getPPitemName());
        senderofferAcceptViewHolder.plQuantity.setText(PLfoodlist.get(i).getPPitemQuanity());
        senderofferAcceptViewHolder.plTime.setText(PLfoodlist.get(i).getPPitemTime());
        senderofferAcceptViewHolder.plAddress.setText((PLfoodlist.get(i).getPPitemAddress()));
        senderofferAcceptViewHolder.plCity.setText((PLfoodlist.get(i).getPPitemCity()));


        //for onclick description of food item

        senderofferAcceptViewHolder.plcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PLcontext,OfferSenderAccept_PostDetail.class);
                intent.putExtra("name",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemName());
                intent.putExtra("time",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemTime());
                intent.putExtra("quantity",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemQuanity());
                intent.putExtra("address",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemAddress());
                intent.putExtra("city",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPitemCity());
                intent.putExtra("ppuserId",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPpuserId());
                intent.putExtra("keyvalue",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPkey());
                intent.putExtra("requesterid",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPrequesterId());
                intent.putExtra("foodstatus",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPfoodStatus());
                intent.putExtra("uploadername",PLfoodlist.get(senderofferAcceptViewHolder.getAbsoluteAdapterPosition()).getPPuserName());
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

class SenderofferAcceptViewHolder extends RecyclerView.ViewHolder
{

    TextView plTitle , plQuantity, plTime, plAddress, plCity;
    CardView plcardView;

    public SenderofferAcceptViewHolder(@NonNull View itemView)
    {
        super(itemView);

        plTitle = itemView.findViewById(R.id.PLfooditemtitleid);
        plQuantity=itemView.findViewById(R.id.PLfooditemquantityid);
        plTime=itemView.findViewById(R.id.PLfooditemtime);
        plAddress=itemView.findViewById(R.id.PLfooditemaddressid);
        plCity=itemView.findViewById(R.id.PLfooditemcityid);


        plcardView=itemView.findViewById(R.id.PublicPLfooditemcardviewid);
    }
}
