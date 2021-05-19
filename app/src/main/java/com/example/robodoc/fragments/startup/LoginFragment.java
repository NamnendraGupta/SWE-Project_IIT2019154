package com.example.robodoc.fragments.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.activities.MainActivity;
import com.example.robodoc.firebase.firestore.CheckIfUserExists;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment implements CheckIfUserExists.UserExistsInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private ProgressIndicatorFragment progressIndicatorFragment;
    private GoogleSignInOptions googleSignInOptions;
    private final int RC_SIGN_IN=1001;
    private FirebaseAuth firebaseAuth;
    private NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("866049601770-5gc1camgktsnhgfdbtimj5hvgepdovco.apps.googleusercontent.com")
                .requestEmail()
                .build();

        firebaseAuth=FirebaseAuth.getInstance();
        navController=Navigation.findNavController(view);
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Signing In with Google");

        firebaseAuth.signOut();

        Button btnLogin=view.findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(v -> startLoginProcess());
    }

    private void startLoginProcess(){
        progressIndicatorFragment.show(getParentFragmentManager(),"SigningIn");
        GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(requireActivity(),googleSignInOptions);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent intent=googleSignInClient.getSignInIntent();
            startActivityForResult(intent,RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                assert account != null;
                signInWithFirebase(account.getIdToken());
            }
            catch (ApiException e){
                Log.w("AUTH","Google Sign In Failed, Error:"+e.getLocalizedMessage());
                progressIndicatorFragment.dismiss();
            }
        }
    }

    private void signInWithFirebase(String idToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                new CheckIfUserExists(firebaseAuth.getCurrentUser().getUid(),this);
            }
            else {
                firebaseAuth.signOut();
                Toast.makeText(requireActivity(),"Sign In Failed",Toast.LENGTH_SHORT).show();
                progressIndicatorFragment.dismiss();
            }
        });
    }

    @Override
    public void onResultCallback(boolean task, boolean result) {
        progressIndicatorFragment.dismiss();
        if(task){
            if(result){
                startActivity(new Intent(requireActivity(),MainActivity.class));
                requireActivity().finish();
            }
            else {
                navController.navigate(R.id.ActionStartRegister);
            }
        }
        else {
            Toast.makeText(requireActivity(),"Sign In Failed",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}