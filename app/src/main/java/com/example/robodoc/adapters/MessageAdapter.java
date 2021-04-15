package com.example.robodoc.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.Message;
import com.example.robodoc.firebase.Globals;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<Message> messageList;
    private Context context;

    public MessageAdapter(ArrayList<Message> messageList, Context context){
        this.messageList=messageList;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvTime, tvSourceUserName, tvMessage;
        MaterialCardView cardHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate=itemView.findViewById(R.id.tvMessageDate);
            tvTime=itemView.findViewById(R.id.tvMessageTime);
            tvSourceUserName=itemView.findViewById(R.id.tvMessageSourceName);
            tvMessage=itemView.findViewById(R.id.tvMessageData);
            cardHolder=itemView.findViewById(R.id.cardHolder);
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message=messageList.get(position);
        holder.tvDate.setText(getMessageDate(message.getTime()));
        holder.tvTime.setText(getMessageTime(message.getTime()));
        holder.tvSourceUserName.setText(message.getSourceUserName());
        holder.tvMessage.setText(message.getMessageData());

        if(message.getSource().equals(Globals.getCurrentUserUid())){
            holder.cardHolder.setCardBackgroundColor(context.getResources().getColor(R.color.messageBackgroundSource));
            holder.tvSourceUserName.setGravity(Gravity.END);
            holder.tvMessage.setGravity(Gravity.END);
        }
        else {
            holder.cardHolder.setCardBackgroundColor(context.getResources().getColor(R.color.messageBackgroundDestination));
            holder.tvSourceUserName.setGravity(Gravity.START);
            holder.tvMessage.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private String getMessageTime(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringTime="";

        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);

        if(hour==0)
            stringTime+="12:";
        else{
            if(hour<10)
                stringTime+="0";
            stringTime+=hour+":";
        }

        if(minute<10)
            stringTime+="0";
        stringTime+=minute+" ";

        if(calendar.get(Calendar.AM_PM)==1)
            stringTime+="PM";
        else
            stringTime+="AM";

        return stringTime;
    }

    private String getMessageDate(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringDate="";

        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        if(day<10)
            stringDate+="0";
        stringDate+=day+"-";

        if(month<10)
            stringDate+="0";
        stringDate+=month+"-";

        stringDate+=year;

        return stringDate;
    }
}
