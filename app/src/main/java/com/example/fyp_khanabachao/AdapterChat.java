package com.example.fyp_khanabachao;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
private static  final int MSG_TYPE_LEFT = 0;
private static final int MSG_TYPE_RIGHT = 1;
Context context ;
List<ModelChat> chatList;
FirebaseUser fUser;
    private String fullScreenInd;
    boolean isImageFitToScreen;

    public AdapterChat(Context context, List<ModelChat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       if(i==MSG_TYPE_RIGHT){
           View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right,viewGroup , false);
           return  new MyHolder(view);

       }
else{
           View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,viewGroup , false);
           return  new MyHolder(view);

       }


    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
   String message = chatList.get(i).getMessage();
   String timeStamp = chatList.get(i).getTimestamp();
   String type = chatList.get(i).getType();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = (String) DateFormat.format("dd/MM/yyyy hh:mm aa" , cal);
        holder.messageIv.setVisibility(View.GONE);
        holder.messageTv.setVisibility(View.GONE);
       if(type.equals("text")){
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageTv.setText(message);
        }
        else{
            holder.messageIv.setVisibility(View.VISIBLE);
            Picasso.get().load(message).placeholder(R.drawable.ic_imagemessage1).into(holder.messageIv);
        }

        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);



        if (i==chatList.size()-1){
            if (chatList.get(i).isSeen()){
                holder.isSeenTv.setVisibility(View.GONE);
               // holder.isSeenTv.setText("Seen");
            }
            else {
               holder.isSeenTv.setVisibility(View.GONE);
               // holder.isSeenTv.setText("Delivered");
            }
        }
        else {
            holder.isSeenTv.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
         ImageView profileTV,messageIv;
         TextView messageTv , timeTv , isSeenTv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileTV = itemView.findViewById(R.id.profileIv);
            messageIv = itemView.findViewById(R.id.messageIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);

        }
    }
}
