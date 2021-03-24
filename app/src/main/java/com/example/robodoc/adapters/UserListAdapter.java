package com.example.robodoc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.classes.UserInfo;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<UserInfo> userList;

    public UserListAdapter(Context context, ArrayList<UserInfo> userList){
        this.context=context;
        this.userList=userList;
        Log.d("ADAPTER","Adapter Initialized");
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView tvName, tvEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser=itemView.findViewById(R.id.imgUserList);
            tvName=itemView.findViewById(R.id.tvUserListName);
            tvEmail=itemView.findViewById(R.id.tvUserListEmail);
        }
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        UserInfo info=userList.get(position);
        holder.tvName.setText(info.getName());
        holder.tvEmail.setText(info.getEmail());
        Picasso.get().load(info.getPhotoUrl()).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        Log.d("LIST SIZE","Size is "+userList.size());
        return userList.size();
    }
}
