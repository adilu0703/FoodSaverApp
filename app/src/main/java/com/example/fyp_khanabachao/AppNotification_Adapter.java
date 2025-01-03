package com.example.fyp_khanabachao;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppNotification_Adapter extends RecyclerView.Adapter<AppNotifViewHolder>
{


    private Context mcontext;
    private List<AppNotification_Model> Notiflist;


    public AppNotification_Adapter(Context mcontext, List<AppNotification_Model> myfoodlist)
    {
        this.mcontext = mcontext;
        this.Notiflist = myfoodlist;

    }

    @NonNull
    @Override
    public AppNotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowitem_appnotification,viewGroup,false);
        return new AppNotifViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull AppNotifViewHolder appnotifViewHolder, int i)
    {   final AppNotification_Model model = Notiflist.get(i);

        String timestamp = Notiflist.get(i).getTimestamp();

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime = (String) DateFormat.format("dd/MM/yyyy hh:mm aa" , calendar);
        appnotifViewHolder.mTime.setText(dateTime);
       String SenderUid = Notiflist.get(i).getsUid();
        //String userName = Notiflist.get(i).getName();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.orderByChild("uid").equalTo(SenderUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    model.setsName(name);
                    appnotifViewHolder.mName.setText(model.getsName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        appnotifViewHolder.mNotif.setText(Notiflist.get(i).getNotification());
        appnotifViewHolder.mTime.setText(dateTime);
        //for onclick description of food item

        /*appnotifViewHolder.mmcardView.setOnClickListener(new View.OnClickListener()
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
        });*/
    }

    @Override
    public int getItemCount() {
        return Notiflist.size();
    }

    //called from CharityUsersList for searching data
    public void searchedList(ArrayList<AppNotification_Model> searchlist)
    {
        Notiflist = searchlist;
        notifyDataSetChanged();
    }

}

class AppNotifViewHolder extends RecyclerView.ViewHolder
{

    TextView mName, mNotif, mTime;
    CardView mmcardView;

    public AppNotifViewHolder(@NonNull View itemView)
    {
        super(itemView);


        mName = itemView.findViewById(R.id.notifnameid);
        mNotif=itemView.findViewById(R.id.notifid);
        mTime=itemView.findViewById(R.id.notiftimeid);

        mmcardView=itemView.findViewById(R.id.charityusercardviewid);
    }
}
