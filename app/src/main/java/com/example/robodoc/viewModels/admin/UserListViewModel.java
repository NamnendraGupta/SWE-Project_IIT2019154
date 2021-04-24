package com.example.robodoc.viewModels.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.firestore.GetUsersList;
import com.example.robodoc.firebase.firestore.UpdateUserRole;

import java.util.ArrayList;

public class UserListViewModel extends ViewModel implements GetUsersList.GetUsersListInterface, UpdateUserRole.UpdateUserRoleInterface {

    private String UID;

    public void setUserUId(String uid){
        UID=uid;
        fetchUsersList();
    }

    private MutableLiveData<Boolean> isUserListLoading;
    private MutableLiveData<ArrayList<UserInfo>> userList;
    private MutableLiveData<Boolean> isUserRoleUpdating;

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

    public LiveData<Boolean> CheckIsUserRoleUpdating(){
        if(isUserRoleUpdating==null){
            isUserRoleUpdating=new MutableLiveData<>();
            isUserRoleUpdating.setValue(false);
        }
        return isUserRoleUpdating;
    }

    private void fetchUsersList(){
        isUserListLoading=new MutableLiveData<>();
        userList=new MutableLiveData<>();

        isUserListLoading.setValue(true);
        userList.setValue(new ArrayList<>());

        new GetUsersList(UID,this);
    }

    @Override
    public void getUsersList(boolean result, ArrayList<UserInfo> userList) {
        isUserListLoading.setValue(false);
        if(result){
            this.userList.setValue(userList);
        }
    }

    private int CurrentPosition;
    private boolean UpdateRoleDoctor, UpdateRoleAdmin;

    public void UpdateUserRoles(int position, boolean isDoctor, boolean isAdmin){
        CurrentPosition=position;
        UpdateRoleDoctor=isDoctor;
        UpdateRoleAdmin=isAdmin;
        isUserRoleUpdating.setValue(true);

        boolean hasAdminChanged= userList.getValue().get(CurrentPosition).isAdmin() != isAdmin;
        boolean hasDoctorChanged=userList.getValue().get(CurrentPosition).isDoctor() != isDoctor;
        String uid=userList.getValue().get(CurrentPosition).getUID();

        new UpdateUserRole(this,uid,hasAdminChanged,isAdmin,hasDoctorChanged,isDoctor);
    }

    private boolean UpdateResult;

    public boolean GetUpdateResult(){
        return UpdateResult;
    }

    @Override
    public void UpdateResult(boolean result) {
        if(result){
            ArrayList<UserInfo> arrayList=userList.getValue();
            arrayList.get(CurrentPosition).setAdmin(UpdateRoleAdmin);
            arrayList.get(CurrentPosition).setDoctor(UpdateRoleDoctor);
            userList.setValue(arrayList);
            UpdateResult=true;
        }
        else {
            UpdateResult=false;
        }
        isUserRoleUpdating.setValue(false);
    }
}
