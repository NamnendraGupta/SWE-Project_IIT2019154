package com.example.robodoc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.admin.UserInfoFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> implements UserInfoFragment.UserInfoFragmentInterface {

    private Context context;
    private ArrayList<UserInfo> userList;
    private FragmentManager manager;

    public UserListAdapter(Context context, ArrayList<UserInfo> userList, FragmentManager manager){
        this.context=context;
        this.userList=userList;
        this.manager=manager;
        Log.d("ADAPTER","Adapter Initialized");
    }

    @Override
    public void onUserUpdated(int position, boolean isAdmin, boolean isDoctor) {
        userList.get(position).setAdmin(isAdmin);
        userList.get(position).setDoctor(isDoctor);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView tvName, tvEmail;
        Button btnShowDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser=itemView.findViewById(R.id.imgUserList);
            tvName=itemView.findViewById(R.id.tvUserListName);
            tvEmail=itemView.findViewById(R.id.tvUserListEmail);
            btnShowDetails=itemView.findViewById(R.id.btnShowDetails);
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
        holder.btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoFragment userInfoFragment=UserInfoFragment.newInstance(userList.get(position));
                userInfoFragment.setUserInterface(position,UserListAdapter.this);
                userInfoFragment.show(manager,"USER INFO");
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("LIST SIZE","Size is "+userList.size());
        return userList.size();
    }
}
