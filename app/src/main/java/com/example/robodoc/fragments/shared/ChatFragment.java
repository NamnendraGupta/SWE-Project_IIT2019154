package com.example.robodoc.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.adapters.MessageAdapter;
import com.example.robodoc.classes.Message;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatFragment extends Fragment {

    private boolean isRoleDoctor;
    private String destinationUID;
    private String destinationUserName;
    private ArrayList<Message> messagesList;

    private RecyclerView rcvMessages;
    private MessageAdapter rcvAdapter;
    private TextInputLayout inputLayoutSendMessage;

    DatabaseReference dbRef;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChatFragmentArgs args=ChatFragmentArgs.fromBundle(getArguments());
        isRoleDoctor=args.getIsRoleDoctor();
        destinationUID=args.getDestinationUID();
        destinationUserName=args.getDestinationUserName();

        TextView tvHeading = view.findViewById(R.id.tvMessageListHeading);
        rcvMessages=view.findViewById(R.id.rcvMessages);
        inputLayoutSendMessage=view.findViewById(R.id.messageInputLayout);
        Button btnSendMessage = view.findViewById(R.id.btnMessageSend);

        messagesList=new ArrayList<>();

        btnSendMessage.setOnClickListener(v -> {
            String message=inputLayoutSendMessage.getEditText().getText().toString();
            if(message.isEmpty()){
                Snackbar.make(requireView(),"Please Enter Some Message",3000).show();
            }
            else {
                sendNewMessage(message);
                inputLayoutSendMessage.getEditText().setText("");
            }
        });


        String heading;
        if(isRoleDoctor)
            heading="Your Interaction with User - "+destinationUserName;
        else
            heading="Your Interaction with Doctor - "+destinationUserName;
        tvHeading.setText(heading);

        rcvMessages.setLayoutManager(new LinearLayoutManager(requireActivity()));

        rcvAdapter=new MessageAdapter(messagesList,requireActivity());
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