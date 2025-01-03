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

import com.example.fyp_khanabachao.CharityUserListDetail;
import com.example.fyp_khanabachao.ConsumerRegisterHelperClass;

import com.example.fyp_khanabachao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class TestAdapter extends RecyclerView.Adapter<TestViewHolder>
{

    // String uid;
    private Context mcontext;
    private List<ConsumerRegisterHelperClass> myfoodlist;


    public TestAdapter(Context mcontext, List<ConsumerRegisterHelperClass> myfoodlist)
    {
        this.mcontext = mcontext;
        this.myfoodlist = myfoodlist;

    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowitem_test,viewGroup,false);
        return new TestViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder testViewHolder, int i)
    {
        testViewHolder.mName.setText(myfoodlist.get(i).getName());
        testViewHolder.mEmail.setText(myfoodlist.get(i).getEmail());
        String hisUid = myfoodlist.get(i).getUid();
        String userName = myfoodlist.get(i).getName();

        //for onclick description of food item

        testViewHolder.mmcardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String add ="";
                Intent intent = new Intent(mcontext, ChatActivity.class);
                intent.putExtra("hisName",userName);
                intent.putExtra("hisUid",hisUid);
                intent.putExtra("add",add);

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

class TestViewHolder extends RecyclerView.ViewHolder
{

    TextView mName, mEmail;
    CardView mmcardView;

    public TestViewHolder(@NonNull View itemView)
    {
        super(itemView);


        mName = itemView.findViewById(R.id.charitynameid);
        mEmail=itemView.findViewById(R.id.charityemailid);

        mmcardView=itemView.findViewById(R.id.charityusercardviewid);
    }
}