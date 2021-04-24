package com.example.robodoc.viewModels.doctor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.firestore.FetchAssignedUsersList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserListViewModel extends ViewModel implements FetchAssignedUsersList.FetchInterface {

    private String DoctorUID;
    private String DoctorName;

    public void setDoctorDetails(String doctorUID, String doctorName) {
        DoctorUID=doctorUID;
        DoctorName=doctorName;
        fetchUsersList();
    }

    private MutableLiveData<Boolean> isUserListLoading;
    private MutableLiveData<ArrayList<UserInfo>> userList;

    public LiveData<Boolean> CheckUserListLoading(){
        if(isUserListLoading==null)
            fetchUsersList();
        return isUserListLoading;
    }

    public LiveData<ArrayList<UserInfo>> getUsersList(){
        if(userList==null)
            fetchUsersList();
        return userList;
    }

    private void fetchUsersList(){
        isUserListLoading=new MutableLiveData<>();
        userList=new MutableLiveData<>();

        isUserListLoading.setValue(true);
        userList.setValue(new ArrayList<>());

        new FetchAssignedUsersList(this,DoctorUID);
    }

    @Override
    public void onUserListFetchCallback(boolean result, ArrayList<UserInfo> userInfoList) {
        isUserListLoading.setValue(false);
        if(result){
            userList.setValue(userInfoList);
        }
    }

    public UserInfo GetUserInfo(int position){
        return userList.getValue().get(position);
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public String getDoctorUID(){
        return DoctorUID;
    }
}
