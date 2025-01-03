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

public class AdapterPostFood extends RecyclerView.Adapter<PostFoodViewHolder>{
    private Context PLcontext;
    private List<PostFoodDataModelClass> PLfoodlist;

    public AdapterPostFood(Context PLcontext, List<PostFoodDataModelClass> PLfoodlist) {
        this.PLcontext = PLcontext;
        this.PLfoodlist = PLfoodlist;
    }

    @NonNull
    @Override
    public PostFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View PLmview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row_postfooditem,viewGroup,false);
        return new PostFoodViewHolder(PLmview);
    }

    @Override
    public void onBindViewHolder(@NonNull PostFoodViewHolder postfoodViewHolder, int i) {
        postfoodViewHolder.plTitle.setText(PLfoodlist.get(i).getPPitemName());
        postfoodViewHolder.plQuantity.setText(PLfoodlist.get(i).getPPitemQuanity());
        postfoodViewHolder.plTime.setText(PLfoodlist.get(i).getPPitemTime());
        postfoodViewHolder.plAddress.setText((PLfoodlist.get(i).getPPitemAddress()));
        postfoodViewHolder.plCity.setText((PLfoodlist.get(i).getPPitemCity()));


        //for onclick description of food item

        postfoodViewHolder.plcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PLcontext,PostFoodDetail.class);
                intent.putExtra("name",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPitemName());
                intent.putExtra("time",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPitemTime());
                intent.putExtra("quantity",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPitemQuanity());
                intent.putExtra("address",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPitemAddress());
                intent.putExtra("city",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPitemCity());
                intent.putExtra("ppuserId",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPpuserId());
                intent.putExtra("keyvalue",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPkey());
                intent.putExtra("requesterid",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPrequesterId());
                intent.putExtra("foodstatus",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPfoodStatus());
                intent.putExtra("uploadername",PLfoodlist.get(postfoodViewHolder.getAbsoluteAdapterPosition()).getPPuserName());

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

class PostFoodViewHolder extends RecyclerView.ViewHolder
{

    TextView plTitle , plQuantity, plTime,  plAddress, plCity;
    CardView plcardView;

    public PostFoodViewHolder(@NonNull View itemView)
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
