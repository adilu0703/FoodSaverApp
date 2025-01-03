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


public class adminUserListAdapter extends RecyclerView.Adapter<AdminViewHolder>
{
    // String uid;
    private Context mcontext;
    private List<ConsumerRegisterHelperClass> myfoodlist;

    public adminUserListAdapter(Context mcontext, List<ConsumerRegisterHelperClass> myfoodlist) {
        this.mcontext = mcontext;
        this.myfoodlist = myfoodlist;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_userlist_row_item,viewGroup,false);
        return new AdminViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder charityViewHolder, int i)
    {
        charityViewHolder.mUsername.setText(myfoodlist.get(i).getUsername());
        charityViewHolder.mName.setText(myfoodlist.get(i).getName());
        charityViewHolder.mContact.setText(myfoodlist.get(i).getPhone());
        charityViewHolder.mEmail.setText(myfoodlist.get(i).getEmail());
        charityViewHolder.mUsertype.setText(myfoodlist.get(i).getUsertype());

        float r = myfoodlist.get(i).getRating();
        String urating;
        if(r==0)
        {
            urating = "Not Rated";
        }
        else
        {
            urating = Float.toString(r);
        }

        charityViewHolder.mRating.setText(urating);


        //for onclick description of food item

         charityViewHolder.mcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mcontext, AdminUserListDetail.class);

                intent.putExtra("name",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getName());
                intent.putExtra("username",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getUsername());
                intent.putExtra("contact",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getPhone());
                intent.putExtra("email",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getEmail());
                intent.putExtra("uid",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getUid());
                intent.putExtra("usertype",myfoodlist.get(charityViewHolder.getAbsoluteAdapterPosition()).getUsertype());


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

class AdminViewHolder extends RecyclerView.ViewHolder
{

    TextView mName, mUsername, mContact, mEmail, mUsertype, mRating;
    CardView mcardView;

    public AdminViewHolder(@NonNull View itemView)
    {
        super(itemView);

        mUsername = itemView.findViewById(R.id.usernameid);
        mName = itemView.findViewById(R.id.nameid);
        mContact=itemView.findViewById(R.id.contactid);
        mEmail=itemView.findViewById(R.id.emailid);
        mUsertype=itemView.findViewById(R.id.usertypeid);
        mRating=itemView.findViewById(R.id.userratingid);

        mcardView=itemView.findViewById(R.id.charityusercardviewid);
    }
}