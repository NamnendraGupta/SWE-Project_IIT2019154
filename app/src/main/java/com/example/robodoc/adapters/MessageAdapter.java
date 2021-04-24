package com.example.robodoc.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.Message;
import com.example.robodoc.utils.DateTimeUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final ArrayList<Message> messageList;
    private final Context context;

    public MessageAdapter(ArrayList<Message> messageList, Context context){
        this.messageList=messageList;
        this.context=context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
        holder.tvDate.setText(DateTimeUtils.getDisplayDate(message.getTime()));
        holder.tvTime.setText(DateTimeUtils.getDisplayTime(message.getTime()));
        holder.tvSourceUserName.setText(message.getSourceUserName());
        holder.tvMessage.setText(message.getMessageData());

        if(message.getSource().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.cardHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonSecondary));
            holder.tvSourceUserName.setGravity(Gravity.END);
            holder.tvMessage.setGravity(Gravity.END);
            String sourceName="You";
            holder.tvSourceUserName.setText(sourceName);
        }
        else {
            holder.cardHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonRipple));
            holder.tvSourceUserName.setGravity(Gravity.START);
            holder.tvMessage.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
