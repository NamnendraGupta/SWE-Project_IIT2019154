package com.example.robodoc.firebase.realtimeDb;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class GetDoctorInteraction {

    public interface GetDoctorInteractionInterface{
        void onResultObtained(boolean isDoctorAssigned);
    }

    private final String TAG="Getting Doctor's Interaction with User";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public GetDoctorInteraction(FragmentManager fragmentManager, GetDoctorInteractionInterface interactionInterface){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Fetching Doctor's Data with User");
        progressIndicatorFragment.show();

        Globals
                .getFirebaseDatabase()
                .getReference()
                .child("USERS")
                .child(Globals.getCurrentUserUid())
                .child("DOCTOR-ASSIGNED")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
