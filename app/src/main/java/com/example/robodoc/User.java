package com.example.robodoc;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.security.Key;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;

public class User implements FetchUserInfo.FetchedUserInfoCallback {

    @Override
    public void onUserDataFetched(HashMap<String, Object> UserData) {
        this.EmailID=UserData.get(Keys.EMAIL).toString();
        this.Name=UserData.get(Keys.NAME).toString();
        if(UserData.get(Keys.PHOTO_URL).toString()==null)
            this.PhotoURL=null;
        else
            this.PhotoURL=Uri.parse(UserData.get(Keys.PHOTO_URL).toString());
        this.DOB=Long.parseLong(UserData.get(Keys.DOB).toString());
        this.DateRegistered=Long.parseLong(UserData.get(Keys.DATE_REGISTERED).toString());
        this.gender=(Gender) UserData.get(Keys.GENDER);
        this.isAdmin=(Boolean) UserData.get(Keys.IS_ADMIN);
        this.isDoctor=(Boolean) UserData.get(Keys.IS_DOCTOR);
    }

    public enum Gender{
        MALE,FEMALE
    }

    enum Keys{
        UID,EMAIL,NAME,PHOTO_URL,DOB,DATE_REGISTERED,GENDER,IS_DOCTOR,IS_ADMIN
    }

    private String UID;
    private String EmailID;
    private String Name;
    private Uri PhotoURL;
    private long DOB;
    private long DateRegistered;
    private Gender gender;
    private boolean isDoctor;
    private boolean isAdmin;

    User(){

    }

    public User(HashMap<String ,Object> hashMap){

    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhotoURL(Uri photoURL) {
        PhotoURL = photoURL;
    }

    public void setDOB(long DOB) {
        this.DOB = DOB;
    }

    public void setDateRegistered(long dateRegistered) {
        DateRegistered = dateRegistered;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUID() {
        return UID;
    }

    public String getEmailID() {
        return EmailID;
    }

    public String getName() {
        return Name;
    }

    public Uri getPhotoURL() {
        return PhotoURL;
    }

    public long getDOB() {
        return DOB;
    }

    public long getDateRegistered() {
        return DateRegistered;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
