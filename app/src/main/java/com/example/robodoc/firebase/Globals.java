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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Globals {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;
    private static FirebaseFirestore firestore;

    public static GoogleSignInOptions googleSignInOptions;
    public static User currentUser;

    public static void initializeGlobals(){

        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("866049601770-5gc1camgktsnhgfdbtimj5hvgepdovco.apps.googleusercontent.com")
                .requestEmail()
                .build();

        firestore=FirebaseFirestore.getInstance();

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

    public static void updateUserWithSnapshot(DocumentSnapshot snapshot){
        updateCurrentUser();
        currentUser.setName(snapshot.getString(UserKey.NAME.toString()));
        currentUser.setEmailID(snapshot.getString(UserKey.EMAIL.toString()));
        currentUser.setDOB(snapshot.getLong(UserKey.DOB.toString()));
        currentUser.setDateRegistered(snapshot.getLong(UserKey.DATE_REGISTERED.toString()));
        currentUser.setGender((Gender) snapshot.get(UserKey.GENDER.toString()));
        currentUser.setPhotoURL(Uri.parse(snapshot.getString(UserKey.PHOTO_URL.toString())));
        currentUser.setDoctor(snapshot.getBoolean(UserKey.IS_DOCTOR.toString()));
        currentUser.setAdmin(snapshot.getBoolean(UserKey.IS_ADMIN.toString()));
    }
}
