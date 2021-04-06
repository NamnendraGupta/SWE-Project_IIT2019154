package com.example.robodoc.firebase;

import android.content.Context;
import android.net.Uri;

import com.example.robodoc.classes.User;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Globals {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;
    private static FirebaseFirestore firestore;
    private static FirebaseDatabase firebaseDatabase;

    public static GoogleSignInOptions googleSignInOptions;
    public static User currentUser;

    public static void initializeGlobals(){

        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("866049601770-5gc1camgktsnhgfdbtimj5hvgepdovco.apps.googleusercontent.com")
                .requestEmail()
                .build();

        firestore=FirebaseFirestore.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        if(isUserLoggedIn()){
            currentUser=new User(firebaseUser.getUid());
        }

    }

    public static boolean isUserLoggedIn(){
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        return firebaseUser != null;
    }

    public static void updateCurrentUser(){
        if(isUserLoggedIn())
            currentUser = new User(firebaseUser.getUid());
        else
            currentUser=null;
    }

    public static void SignOut(){
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        updateCurrentUser();
    }

    public static GoogleSignInClient getGoogleSignInClient(Context context){
        return GoogleSignIn.getClient(context,googleSignInOptions);
    }

    public static FirebaseAuth getAuth(){
        firebaseAuth=FirebaseAuth.getInstance();
        return firebaseAuth;
    }

    public static String getCurrentUserUid(){
        return firebaseUser.getUid();
    }

    public static FirebaseFirestore getFirestore(){
        firestore=FirebaseFirestore.getInstance();
        return firestore;
    }

    public static FirebaseDatabase getFirebaseDatabase(){
        firebaseDatabase=FirebaseDatabase.getInstance();
        return firebaseDatabase;
    }

    public static void updateUserWithSnapshot(DocumentSnapshot snapshot){
        updateCurrentUser();
        currentUser.setName(snapshot.getString(UserKey.NAME.toString()));
        currentUser.setEmailID(snapshot.getString(UserKey.EMAIL.toString()));
        currentUser.setDOB(snapshot.getLong(UserKey.DOB.toString()));
        currentUser.setDateRegistered(snapshot.getLong(UserKey.DATE_REGISTERED.toString()));

        if(snapshot.getString(UserKey.GENDER.toString()).equals(Gender.MALE.toString()))
            currentUser.setGender(Gender.MALE);
        else
            currentUser.setGender(Gender.FEMALE);

        currentUser.setPhotoURL(Uri.parse(snapshot.getString(UserKey.PHOTO_URL.toString())));
        currentUser.setDoctor(snapshot.getBoolean(UserKey.IS_DOCTOR.toString()));
        currentUser.setAdmin(snapshot.getBoolean(UserKey.IS_ADMIN.toString()));
    }

    public static void updateUserWithHashMap(HashMap<String,Object> hashMap){
        updateCurrentUser();
        currentUser.setName((String)hashMap.get(UserKey.NAME.toString()));
        currentUser.setEmailID((String) hashMap.get(UserKey.EMAIL.toString()));
        currentUser.setDOB((Long) hashMap.get(UserKey.DOB.toString()));
        currentUser.setDateRegistered((Long)hashMap.get(UserKey.DATE_REGISTERED.toString()));

        if(hashMap.get(UserKey.GENDER.toString()).equals(Gender.MALE.toString()))
            currentUser.setGender(Gender.MALE);
        else
            currentUser.setGender(Gender.FEMALE);

        currentUser.setPhotoURL(Uri.parse((String) hashMap.get(UserKey.PHOTO_URL.toString())));
        currentUser.setDoctor((Boolean)hashMap.get(UserKey.IS_DOCTOR.toString()));
        currentUser.setAdmin((Boolean) hashMap.get(UserKey.IS_ADMIN.toString()));
    }

    public static HashMap<String,Object> getFirebaseUserInfo(){
        HashMap<String ,Object> hashMap=new HashMap<>();

        hashMap.put(UserKey.UID.toString(),firebaseUser.getUid());
        hashMap.put(UserKey.EMAIL.toString(),firebaseUser.getEmail());

        String photoUrl=firebaseUser.getPhotoUrl().toString();
        photoUrl=photoUrl.replace("s96-c","s320-c");
        hashMap.put(UserKey.PHOTO_URL.toString(),photoUrl);
        return hashMap;
    }

    public static boolean isUserDoctor(){
        return currentUser.isDoctor();
    }

    public static boolean isUserAdmin(){
        return currentUser.isAdmin();
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static String getCurrentUserDisplayName(){
        return currentUser.getName();
    }
}
