package com.example.robodoc.fragments.admin;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.firestore.UpdateUserRole;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class UserInfoFragment extends DialogFragment implements UpdateUserRole.UpdateUserRoleInterface {

    public interface UserInfoFragmentInterface{
        public void onUserUpdated(int position, boolean isAdmin, boolean isDoctor);
    }

    private static final String KEY_UID= UserKey.UID.toString();
    private static final String KEY_NAME=UserKey.NAME.toString();
    private static final String KEY_EMAIL=UserKey.EMAIL.toString();
    private static final String KEY_PHOTO= UserKey.PHOTO_URL.toString();
    private static final String KEY_GENDER=UserKey.GENDER.toString();
    private static final String KEY_ADMIN=UserKey.IS_ADMIN.toString();
    private static final String KEY_DOCTOR=UserKey.IS_DOCTOR.toString();
    private static final String KEY_DATE_REGISTERED=UserKey.DATE_REGISTERED.toString();

    private UserInfo userInfo;
    private UserInfoFragmentInterface userInterface;
    private int position;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static UserInfoFragment newInstance(UserInfo userInfo) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_UID,userInfo.getUID());
        args.putString(KEY_NAME,userInfo.getName());
        args.putString(KEY_EMAIL,userInfo.getEmail());
        args.putString(KEY_PHOTO,userInfo.getPhotoUrl().toString());
        args.putString(KEY_GENDER,userInfo.getGender().toString());
        args.putBoolean(KEY_ADMIN,userInfo.isAdmin());
        args.putBoolean(KEY_DOCTOR,userInfo.isDoctor());
        args.putLong(KEY_DATE_REGISTERED,userInfo.getDateRegistered().getTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userInfo=new UserInfo(getArguments().getString(KEY_UID));
            userInfo.setName(getArguments().getString(KEY_NAME));
            userInfo.setEmail(getArguments().getString(KEY_EMAIL));
            userInfo.setPhotoUrl(Uri.parse(getArguments().getString(KEY_PHOTO)));
            userInfo.setDateRegistered(new Date(getArguments().getLong(KEY_DATE_REGISTERED)));
            userInfo.setAdmin(getArguments().getBoolean(KEY_ADMIN));
            userInfo.setDoctor(getArguments().getBoolean(KEY_DOCTOR));
            if(getArguments().getString(KEY_GENDER).equals(Gender.MALE.toString()))
                userInfo.setGender(Gender.MALE);
            else
                userInfo.setGender(Gender.FEMALE);
        }
    }

    public void setUserInterface(int position, UserInfoFragmentInterface userInterface){
        this.position=position;
        this.userInterface=userInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    private TextView tvName, tvEmail, tvUID, tvDateRegistered, tvGender;
    private ImageView imgUser;
    private SwitchMaterial switchAdmin, switchDoctor;
    private Button btnUpdate, btnCancel;

    boolean isAdmin, isDoctor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvName=view.findViewById(R.id.tvUserInfoName);
        tvEmail=view.findViewById(R.id.tvUserInfoEmail);
        tvUID=view.findViewById(R.id.tvUserInfoUID);
        tvDateRegistered=view.findViewById(R.id.tvUserInfoDateRegistered);
        tvGender=view.findViewById(R.id.tvUserInfoGender);
        imgUser=view.findViewById(R.id.imgUserInfo);
        switchAdmin=view.findViewById(R.id.switchUserInfoAdmin);
        switchDoctor=view.findViewById(R.id.switchUserInfoDoctor);
        btnUpdate=view.findViewById(R.id.btnUserInfoSubmit);
        btnCancel=view.findViewById(R.id.btnUserInfoCancel);

        isAdmin=userInfo.isAdmin();
        isDoctor=userInfo.isDoctor();

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvName.setText(userInfo.getName());
        tvEmail.setText(userInfo.getEmail());
        tvUID.setText(userInfo.getUID());
        tvDateRegistered.setText(getDisplayDate(userInfo.getDateRegistered()));
        tvGender.setText(userInfo.getGender().toString());
        Picasso.get().load(userInfo.getPhotoUrl()).into(imgUser);
        switchAdmin.setUseMaterialThemeColors(true);
        switchAdmin.setChecked(isAdmin);
        switchDoctor.setUseMaterialThemeColors(true);
        switchDoctor.setChecked(isDoctor);

        switchAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAdmin=isChecked;
                btnUpdate.setEnabled(isStateChanged());
            }
        });

        switchDoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDoctor=isChecked;
                btnUpdate.setEnabled(isStateChanged());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateUserRole(getFragmentManager(),UserInfoFragment.this,userInfo.getUID(),isAdmin,isDoctor);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isStateChanged(){
        return (isAdmin!=userInfo.isAdmin() || isDoctor!=userInfo.isDoctor());
    }

    private String getDisplayDate(Date date){
        String result;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        result=calendar.get(Calendar.DAY_OF_MONTH)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
        return result;
    }

    private void closeWindow(){

        getDialog().dismiss();
    }

    @Override
    public void UpdateResult(boolean result) {
        if(result){
            Toast.makeText(getContext(),"User Roles Updated",Toast.LENGTH_LONG).show();
            userInterface.onUserUpdated(position,isAdmin,isDoctor);
            closeWindow();
        }
        else {
            Toast.makeText(getContext(),"Error in Updating User Role",Toast.LENGTH_LONG).show();
        }
    }
}