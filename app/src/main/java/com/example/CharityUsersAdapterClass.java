package com.example;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_khanabachao.CharityUserListDetail;
import com.example.fyp_khanabachao.ConsumerRegisterHelperClass;

import com.example.fyp_khanabachao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class CharityUsersAdapterClass extends RecyclerView.Adapter<CharityViewHolder>
{

   // String uid;
    private Context mcontext;
    private List<ConsumerRegisterHelperClass> myfoodlist;


    public CharityUsersAdapterClass(Context mcontext, List<ConsumerRegisterHelperClass> myfoodlist)
    {
        this.mcontext = mcontext;
        this.myfoodlist = myfoodlist;

    }

    @NonNull
    @Override
    public CharityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowitem_charityusers,viewGroup,false);
        return new CharityViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull CharityViewHolder charityViewHolder, int i)
    {
        charityViewHolder.mUsername.setText(myfoodlist.get(i).getUsername());
        charityViewHolder.mName.setText(myfoodlist.get(i).getName());
        charityViewHolder.mContact.setText(myfoodlist.get(i).getPhone());
        charityViewHolder.mEmail.setText(myfoodlist.get(i).getEmail());


        //for onclick description of food item

        charityViewHolder.mcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mcontext, CharityUserListDetail.class);

                intent.putExtra("name",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getName());
                intent.putExtra("username",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getUsername());
                intent.putExtra("contact",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getPhone());
                intent.putExtra("email",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getEmail());
                intent.putExtra("uid",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getUid());


                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myfoodlist.size();
    }

        //called from CharityUsersList for searching data
   public void searchedList(ArrayList<ConsumerRegisterHelperClass> searchlist)
    {
        myfoodlist = searchlist;
        notifyDataSetChanged();
    }

}

class CharityViewHolder extends RecyclerView.ViewHolder
{

    TextView mName, mUsername, mContact, mEmail;
    CardView mcardView;

    public CharityViewHolder(@NonNull View itemView)
    {
        super(itemView);

        mUsername = itemView.findViewById(R.id.charitusernameid);
        mName = itemView.findViewById(R.id.charitynameid);
        mContact=itemView.findViewById(R.id.charitycontactid);
        mEmail=itemView.findViewById(R.id.charityemailid);

        mcardView=itemView.findViewById(R.id.charityusercardviewid);
    }
}