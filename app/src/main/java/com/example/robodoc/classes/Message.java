package com.example.robodoc.classes;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.firebase.database.DataSnapshot;

import java.util.Date;
import java.util.HashMap;

public class Message {

    private String ID;
    private String source;
    private String destination;
    private String sourceUserName;
    private Long time;
    private String messageData;

    public Message(String messageData, String destination){
        this.messageData=messageData;
        this.destination=destination;
        this.source= Globals.getCurrentUserUid();
        this.time=new Date().getTime();
    }

    public Message(DataSnapshot dataSnapshot){
        ID=dataSnapshot.getKey();
        source=dataSnapshot.child(DatabaseKeys.KEY_MESSAGE_SOURCE).getValue().toString();
        destination=dataSnapshot.child(DatabaseKeys.KEY_MESSAGE_DESTINATION).getValue().toString();
        time=(Long)dataSnapshot.child(DatabaseKeys.KEY_MESSAGE_TIME).getValue();
        messageData=dataSnapshot.child(DatabaseKeys.KEY_MESSAGE_DATA).getValue().toString();
    }

    public HashMap<String,Object> getUploadMessageHash(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(DatabaseKeys.KEY_MESSAGE_SOURCE,source);
        hashMap.put(DatabaseKeys.KEY_MESSAGE_DESTINATION,destination);
        hashMap.put(DatabaseKeys.KEY_MESSAGE_TIME,time);
        hashMap.put(DatabaseKeys.KEY_MESSAGE_DATA,messageData);
        return hashMap;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMessageData() {
        return messageData;
    }

    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }
}
