package com.example.robodoc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.admin.UserListFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminUserListAdapter extends RecyclerView.Adapter<AdminUserListAdapter.ViewHolder> {

    private final ArrayList<UserInfo> userList;
    private final NavController navController;

    public AdminUserListAdapter(ArrayList<UserInfo> userList, NavController navController) {
        this.userList = userList;
        this.navController = navController;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail;
        ImageView imgUser, imgAdminCheck, imgDoctorCheck;
        ImageButton btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvAdminUserListName);
            tvEmail=itemView.findViewById(R.id.tvAdminUserListEmail);
            imgUser=itemView.findViewById(R.id.imgAdminUserList);
            imgAdminCheck=itemView.findViewById(R.id.imgAdminUserListIsAdmin);
            imgDoctorCheck=itemView.findViewById(R.id.imgAdminUserListIsDoctor);
            btnViewDetails=itemView.findViewById(R.id.imgBtnAdminUserList);
        }
    }

    @NonNull
    @Override
    public AdminUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item_admin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserListAdapter.ViewHolder holder, int position) {
        UserInfo userInfo=userList.get(position);
        holder.tvName.setText(userInfo.getName());
        holder.tvEmail.setText(userInfo.getEmail());
        Picasso.get().load(userInfo.getPhotoUrl()).into(holder.imgUser);

        if(userInfo.isAdmin())
            holder.imgAdminCheck.setImageDrawable(ContextCompat.getDrawable(holder.imgAdminCheck.getContext(),R.drawable.ic_success));
        else
            holder.imgAdminCheck.setImageDrawable(ContextCompat.getDrawable(holder.imgAdminCheck.getContext(),R.drawable.ic_error));

        if(userInfo.isDoctor())
            holder.imgDoctorCheck.setImageDrawable(ContextCompat.getDrawable(holder.imgDoctorCheck.getContext(),R.drawable.ic_success));
        else
            holder.imgDoctorCheck.setImageDrawable(ContextCompat.getDrawable(holder.imgDoctorCheck.getContext(),R.drawable.ic_error));

        holder.btnViewDetails.setOnClickListener(v -> {
            NavDirections action= UserListFragmentDirections.ActionAdminUserInfo(position);
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
