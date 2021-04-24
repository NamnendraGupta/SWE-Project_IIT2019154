package com.example.robodoc.viewModels.user;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robodoc.classes.User;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.firestore.GetCurrentUserInfo;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserInfoViewModel extends ViewModel implements GetCurrentUserInfo.GetUserInfoInterface {

    private final String UId;
    private MutableLiveData<Boolean> IsUserInfoLoading;
    private MutableLiveData<User> CurrentUser;

    public UserInfoViewModel() {
        super();
        UId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchUserInfo();
    }

    public LiveData<Boolean> CheckIsUserInfoLoading(){
        if(IsUserInfoLoading==null)
            fetchUserInfo();
        return IsUserInfoLoading;
    }

    public LiveData<User> GetCurrentUser(){
        if(CurrentUser==null)
            fetchUserInfo();
        return CurrentUser;
    }

    private void fetchUserInfo(){
        if((IsUserInfoLoading==null || !IsUserInfoLoading.getValue()) && UId!=null){
            IsUserInfoLoading=new MutableLiveData<>();
            CurrentUser=new MutableLiveData<>();

            IsUserInfoLoading.setValue(true);

            new GetCurrentUserInfo(UId,this);
        }
    }

    public String getUId(){
        return UId;
    }

    public String getDisplayName(){
        return CurrentUser.getValue().getName();
    }

    public boolean IsUserAdmin(){
        return CurrentUser.getValue().isAdmin();
    }

    public boolean IsUserDoctor(){
        return CurrentUser.getValue().isDoctor();
    }

    public boolean IsDoctorAssigned(){
        return CurrentUser.getValue().isDoctorAssigned();
    }

    public String GetAssignedDoctorUID(){
        return CurrentUser.getValue().getAssignedDoctorUID();
    }

    public String GetAssignedDoctorName(){
        return CurrentUser.getValue().getAssignedDoctorName();
    }

    public void setDoctorAssigned(boolean assigned, String doctorUId, String doctorName){
        User user=CurrentUser.getValue();
        user.setDoctorAssigned(assigned);
        user.setAssignedDoctorUID(doctorUId);
        user.setAssignedDoctorName(doctorName);
        CurrentUser.setValue(user);
    }

    @Override
    public void onResultCallback(boolean result, DocumentSnapshot snapshot, boolean isDoctorAssigned, String doctorUId, String doctorName) {
        if(result){
            User currentUser=new User(UId);
            currentUser.setName(snapshot.getString(UserKey.NAME.toString()));
            currentUser.setEmailID(snapshot.getString(UserKey.EMAIL.toString()));
            currentUser.setDOB(snapshot.getLong(UserKey.DOB.toString()));
            currentUser.setDateRegistered(snapshot.getLong(UserKey.DATE_REGISTERED.toString()));

            if(snapshot.getString(UserKey.GENDER.toString()).equals(Gender.MALE.toString()))
                currentUser.setGender(Gender.MALE);
            else
                currentUser.setGender(Gender.FEMALE);

            currentUser.setPhotoURL(Uri.parse(snapshot.getString(UserKey.PHOTO_URL.toString())));
            currentUser.setDoctor(snapshot.getBoolean(UserKey.IS_DOCTOR.toString()));
            currentUser.setAdmin(snapshot.getBoolean(UserKey.IS_ADMIN.toString()));
            currentUser.setDoctorAssigned(isDoctorAssigned);
            if(isDoctorAssigned){
                currentUser.setAssignedDoctorUID(doctorUId);
                currentUser.setAssignedDoctorName(doctorName);
            }

            CurrentUser.setValue(currentUser);
        }
        IsUserInfoLoading.setValue(false);
    }
}
