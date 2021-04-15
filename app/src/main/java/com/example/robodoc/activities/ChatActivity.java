package com.example.robodoc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.adapters.MessageAdapter;
import com.example.robodoc.classes.Message;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.internal.cache.DiskLruCache;

public class ChatActivity extends AppCompatActivity {

    private boolean isRoleDoctor;
    private String sourceUID;
    private String destinationUID;
    private String destinationUserName;
    private ArrayList<Message> messagesList;

    private TextView tvHeading;
    private RecyclerView rcvMessages;
    private RecyclerView.Adapter rcvAdapter;
    private TextInputLayout inputLayoutSendMessage;
    private Button btnSendMessage, btnClose;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tvHeading=findViewById(R.id.tvMessageListHeading);
        rcvMessages=findViewById(R.id.rcvMessages);
        inputLayoutSendMessage=findViewById(R.id.messageInputLayout);
        btnSendMessage=findViewById(R.id.btnMessageSend);
        btnClose=findViewById(R.id.btnMessageClose);

        Intent intent=getIntent();
        isRoleDoctor=intent.getBooleanExtra("IsDoctor",false);
        sourceUID= Globals.getCurrentUserUid();
        destinationUID=intent.getStringExtra("DestinationUID");
        destinationUserName=intent.getStringExtra("DestinationUserName");

        messagesList=new ArrayList<>();

        btnSendMessage.setOnClickListener(v -> {
            String message=inputLayoutSendMessage.getEditText().getText().toString();
            if(message.isEmpty()){
                Snackbar.make(getWindow().getDecorView().getRootView(),"Please Enter Some Message",3000).show();
            }
            else {
                sendNewMessage(message);
                inputLayoutSendMessage.getEditText().setText("");
            }
        });

        btnClose.setOnClickListener(v -> {
            this.finish();
        });

        String heading;
        if(isRoleDoctor)
            heading="Your Interaction with User - "+destinationUserName;
        else
            heading="Your Interaction with Doctor - "+destinationUserName;
        tvHeading.setText(heading);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvMessages.setLayoutManager(linearLayoutManager);

        rcvAdapter=new MessageAdapter(messagesList,this);
        rcvMessages.setAdapter(rcvAdapter);

        dbRef=Globals.getFirebaseDatabase().getReference();
        if(isRoleDoctor)
            dbRef=dbRef.child(DatabaseKeys.KEY_DOCTORS).child(Globals.getCurrentUserUid()).child(DatabaseKeys.KEY_DOCTOR_MESSAGES);
        else
            dbRef=dbRef.child(DatabaseKeys.KEY_USERS).child(Globals.getCurrentUserUid()).child(DatabaseKeys.KEY_USER_MESSAGES);

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                onNewMessageReceived(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNewMessage(String messageData){
        Message message=new Message(messageData,destinationUID);
        HashMap<String,Object> hash=message.getUploadMessageHash();
        if(isRoleDoctor){
            dbRef.push().setValue(hash);
            Globals.getFirebaseDatabase().getReference().child(DatabaseKeys.KEY_USERS).child(destinationUID).child(DatabaseKeys.KEY_USER_MESSAGES)
            .push().setValue(hash);
        }
        else {
            dbRef.push().setValue(hash);
            Globals.getFirebaseDatabase().getReference().child(DatabaseKeys.KEY_DOCTORS).child(destinationUID).child(DatabaseKeys.KEY_DOCTOR_MESSAGES)
                    .push().setValue(hash);
        }
    }

    private void onNewMessageReceived(DataSnapshot snapshot){
        Message message=new Message(snapshot);
        if(message.getSource().equals(Globals.getCurrentUserUid()))
            message.setSourceUserName(Globals.getCurrentUserDisplayName());
        else
            message.setSourceUserName(destinationUserName);

        messagesList.add(message);
        rcvAdapter.notifyDataSetChanged();
        rcvMessages.scrollToPosition(messagesList.size()-1);
    }

}