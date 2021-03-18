package com.example.robodoc;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class RegisterUser {

    public interface RegisterUserInterface{
        public void onUserRegistration(boolean task);
    }

    private String name;
    private String UID;
    private String email;
    private String dob;
    private Date dateRegistered;
    private String photoUrl;
    private User.Gender gender;

    private FragmentManager fragmentManager;
    private ProgressIndicatorFragment progressIndicatorFragment;
    private RegisterUserInterface registerUserInterface;

    public RegisterUser(String name, String dob, User.Gender gender, FragmentManager fragmentManager, RegisterUserInterface registerUserInterface){
        this.name=name;
        this.dob=dob;
        this.gender=gender;
        this.fragmentManager=fragmentManager;
        this.registerUserInterface=registerUserInterface;
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        this.UID=user.getUid();
        this.email=user.getEmail();
        this.dateRegistered=new Date();
        this.photoUrl=user.getPhotoUrl().toString();
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Registering","Adding User Details to the Server");

        register();
    }

    private void register(){
        CollectionReference reference=FirebaseFirestore.getInstance().collection("USERS");
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put(User.Keys.UID.toString(),UID);
        hashMap.put(User.Keys.EMAIL.toString(),email);
        hashMap.put(User.Keys.NAME.toString(),name);
        hashMap.put(User.Keys.PHOTO_URL.toString(),photoUrl);
        hashMap.put(User.Keys.DOB.toString(),dob);
        hashMap.put(User.Keys.DATE_REGISTERED.toString(),dateRegistered);
        hashMap.put(User.Keys.GENDER.toString(),gender);
        hashMap.put(User.Keys.IS_ADMIN.toString(), false);
        hashMap.put(User.Keys.IS_DOCTOR.toString(),false);

        progressIndicatorFragment.show(fragmentManager,"Registering");

        reference.document(UID).set(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressIndicatorFragment.dismiss();
                        registerUserInterface.onUserRegistration(task.isSuccessful());
                    }
                });

    }

}
