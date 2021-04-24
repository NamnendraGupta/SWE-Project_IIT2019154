package com.example.robodoc.classes;

import android.net.Uri;

import com.example.robodoc.enums.Gender;

public class User {

    private String UID;
    private String EmailID;
    private String Name;
    private Uri PhotoURL;
    private long DOB;
    private long DateRegistered;
    private Gender gender;
    private boolean isDoctor;
    private boolean isAdmin;
    private boolean isDoctorAssigned;
    private String AssignedDoctorUID;
    private String AssignedDoctorName;

    public User(String UID){
        this.UID=UID;
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

    public String getName() {
        return Name;
    }

    public Uri getPhotoURL() {
        return PhotoURL;
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

    public String getAssignedDoctorUID() {
        return AssignedDoctorUID;
    }

    public void setAssignedDoctorUID(String assignedDoctorUID) {
        AssignedDoctorUID = assignedDoctorUID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmailID() {
        return EmailID;
    }

    public long getDOB() {
        return DOB;
    }

    public long getDateRegistered() {
        return DateRegistered;
    }

    public boolean isDoctorAssigned() {
        return isDoctorAssigned;
    }

    public void setDoctorAssigned(boolean doctorAssigned) {
        isDoctorAssigned = doctorAssigned;
    }

    public String getAssignedDoctorName() {
        return AssignedDoctorName;
    }

    public void setAssignedDoctorName(String assignedDoctorName) {
        AssignedDoctorName = assignedDoctorName;
    }
}
