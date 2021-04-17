package com.example.robodoc.viewModels.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.firestore.GetUsersList;

import java.util.ArrayList;

public class UserListViewModel extends ViewModel implements GetUsersList.GetUsersListInterface {

    public UserListViewModel() {
        super();
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

        new GetUsersList(this);
    }

    @Override
    public void getUsersList(boolean result, ArrayList<UserInfo> userList) {
        isUserListLoading.setValue(false);
        if(result){
            this.userList.setValue(userList);
        }
    }

    public UserInfo GetUserInfo(int position){
        return userList.getValue().get(position);
    }
}
