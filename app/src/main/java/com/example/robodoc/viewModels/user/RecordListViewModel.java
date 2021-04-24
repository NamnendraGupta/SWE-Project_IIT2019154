package com.example.robodoc.viewModels.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.realtimeDb.GetVitalRecord;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RecordListViewModel extends ViewModel implements GetVitalRecord.GetVitalRecordInterface {

    private String UserUId;
    private MutableLiveData<Boolean> IsListLoading;
    private MutableLiveData<ArrayList<VitalInput>> VitalInputList;
    private MutableLiveData<Integer> ListSize;

    public void setUserUId(String userUId) {
        UserUId = userUId;
        Initialize();
    }

    public LiveData<Boolean> CheckIsListLoading(){
        if(IsListLoading==null)
            Initialize();
        return IsListLoading;
    }

    public LiveData<ArrayList<VitalInput>> GetRecordList(){
        if(VitalInputList==null)
            Initialize();
        return VitalInputList;
    }

    public LiveData<Integer> GetListSize(){
        if(ListSize==null)
            Initialize();
        return ListSize;
    }

    private void Initialize(){
        if(UserUId==null)
            UserUId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(VitalInputList==null){
            IsListLoading=new MutableLiveData<>();
            VitalInputList=new MutableLiveData<>();
            ListSize=new MutableLiveData<>();
            FetchVitalRecords();
        }
    }

    private void FetchVitalRecords(){
        IsListLoading.setValue(true);
        VitalInputList.setValue(new ArrayList<>());
        ListSize.setValue(0);
        new GetVitalRecord(this,UserUId);
    }

    @Override
    public void onNewRecordObtained(boolean hasRecords, VitalInput newRecord) {
        if(IsListLoading.getValue())
            IsListLoading.setValue(false);
        if(hasRecords){
            ArrayList<VitalInput> recordList=VitalInputList.getValue();
            for(int i=0;i<recordList.size();i++){
                if(recordList.get(i).getInputID().equals(newRecord.getInputID()))
                    return;
            }
            recordList.add(newRecord);
            VitalInputList.setValue(recordList);
            ListSize.setValue(VitalInputList.getValue().size());
        }
    }

    public VitalInput GetRecordInfo(int position){
        return VitalInputList.getValue().get(position);
    }

    public int GetSize(){
        return ListSize.getValue();
    }
}