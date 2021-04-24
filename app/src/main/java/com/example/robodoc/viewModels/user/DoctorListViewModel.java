package com.example.robodoc.viewModels.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.firestore.GetDoctorsList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DoctorListViewModel extends ViewModel implements GetDoctorsList.GetDoctorsListInterface {

    public DoctorListViewModel() {
        super();
        UserUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchDoctorsList();
    }

    private final String UserUID;
    private MutableLiveData<Boolean> IsDoctorListLoading;
    private MutableLiveData<ArrayList<UserInfo>> DoctorList;

    public LiveData<Boolean> CheckDoctorListLoading(){
        if(IsDoctorListLoading==null)
            fetchDoctorsList();
        return IsDoctorListLoading;
    }

    public LiveData<ArrayList<UserInfo>> GetDoctorsList(){
        if(DoctorList==null)
            fetchDoctorsList();
        return DoctorList;
    }

    private void fetchDoctorsList(){
        IsDoctorListLoading=new MutableLiveData<>();
        DoctorList=new MutableLiveData<>();

        IsDoctorListLoading.setValue(true);
        DoctorList.setValue(new ArrayList<>());

        new GetDoctorsList(UserUID,this);
    }

    @Override
    public void getDoctorsList(boolean result, ArrayList<UserInfo> userList) {
        IsDoctorListLoading.setValue(false);
        if(result){
            DoctorList.setValue(userList);
        }
    }

    public UserInfo getDoctorInfo(int position){
        return DoctorList.getValue().get(position);
    }
}
