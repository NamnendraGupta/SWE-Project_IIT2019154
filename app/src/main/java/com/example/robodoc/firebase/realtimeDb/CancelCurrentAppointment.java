package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.shared.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class CancelCurrentAppointment {

    public interface CancelCurrentAppointmentInterface{
        void onAppointmentCanceled(boolean result);
    }

    private final String TAG="Cancel Appointment";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public CancelCurrentAppointment(FragmentManager manager, CancelCurrentAppointmentInterface cancelInterface, String Uid, String doctorUid){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Canceling Current Doctor Appointment");
        progressIndicatorFragment.show(manager,TAG);

        DatabaseReference dbRef= Globals.getFirebaseDatabase().getReference();
        dbRef
                .child(DatabaseKeys.KEY_USERS)
                .child(Uid)
                .child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dbRef
                                .child(DatabaseKeys.KEY_DOCTORS)
                                .child(doctorUid)
                                .child(DatabaseKeys.KEY_DOCTORS_ASSIGNED_LIST)
                                .child(Uid)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressIndicatorFragment.dismiss();
                                        if(task.isSuccessful()){
                                            Log.d(TAG,"Appointment Cancel Successful");
                                            cancelInterface.onAppointmentCanceled(true);
                                        }
                                        else {
                                            Log.d(TAG,"Error in Canceling Appointment");
                                            cancelInterface.onAppointmentCanceled(false);
                                        }
                                    }
                                });
                    }
                    else {
                        Log.d(TAG,"Error in Canceling Appointment");
                        progressIndicatorFragment.dismiss();
                        cancelInterface.onAppointmentCanceled(false);
                    }
                });

    }

}
