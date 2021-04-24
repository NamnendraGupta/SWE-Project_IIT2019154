package com.example.robodoc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.admin.UserInfoFragment;
import com.example.robodoc.fragments.admin.UserListFragmentDirections;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public abstract class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    protected ArrayList<UserInfo> userList;
    protected NavController navController;

    public UserListAdapter(ArrayList<UserInfo> userList, NavController navController){
        this.userList=userList;
        this.navController=navController;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView tvName, tvEmail;
        ImageButton btnShowDetails;
        MaterialCardView mcvHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser=itemView.findViewById(R.id.imgUserList);
            tvName=itemView.findViewById(R.id.tvUserListName);
            tvEmail=itemView.findViewById(R.id.tvUserListEmail);
            btnShowDetails=itemView.findViewById(R.id.imgBtnViewProfile);
            mcvHolder=itemView.findViewById(R.id.mcvUserListItem);
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
        holder.btnShowDetails.setOnClickListener(v -> onButtonClicked(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    protected void onButtonClicked(int position){
        NavDirections action= UserListFragmentDirections.ActionAdminUserInfo(position);
        navController.navigate(action);
    }
}
