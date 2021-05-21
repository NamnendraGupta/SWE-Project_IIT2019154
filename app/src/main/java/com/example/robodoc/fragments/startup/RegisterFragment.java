package com.example.robodoc.fragments.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.activities.MainActivity;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.firestore.GetNumberOfUsers;
import com.example.robodoc.firebase.firestore.RegisterNewUser;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.utils.DateTimeUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.HashMap;

public class RegisterFragment extends Fragment implements RegisterNewUser.NewUserInterface, GetNumberOfUsers.CallbackInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private TextInputLayout inputLayoutName;
    private TextView tvDOB;
    private RadioButton rbMale;
    private Date dob;
    private MaterialDatePicker datePicker;

    private ProgressIndicatorFragment progressIndicatorFragment;
    private HashMap<String,Object> userData;
    private String UID;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputLayoutName=view.findViewById(R.id.inputLayoutRegisterName);
        tvDOB=view.findViewById(R.id.tvDisplayDOB);
        rbMale=view.findViewById(R.id.rbMale);
        Button btnSelectDOB = view.findViewById(R.id.btnSelectDOB);
        Button btnSubmit = view.findViewById(R.id.btnRegisterUser);

        dob=new Date();
        tvDOB.setText(DateTimeUtils.getDisplayDate(dob.getTime()));

        datePicker=MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Select Your DOB")
                .setSelection(dob.getTime())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            dob=new Date((Long)selection);
            tvDOB.setText(DateTimeUtils.getDisplayDate(dob.getTime()));
        });

        btnSelectDOB.setOnClickListener(v -> datePicker.show(getParentFragmentManager(),"DOB"));

        btnSubmit.setOnClickListener(v -> {
            String name;
            long dateOfBirth,dateRegistered;
            Gender gender;

            name=inputLayoutName.getEditText().getText().toString();
            if(name.isEmpty()){
                Snackbar.make(view,"Please Enter Your Name",2000).show();
                return;
            }

            dateOfBirth=dob.getTime();
            dateRegistered=new Date().getTime();

            if(rbMale.isChecked())
                gender=Gender.MALE;
            else
                gender=Gender.FEMALE;

            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

            userData=new HashMap<>();
            userData.put(UserKey.UID.toString(),user.getUid());
            userData.put(UserKey.EMAIL.toString(),user.getEmail());
            userData.put(UserKey.PHOTO_URL.toString(),user.getPhotoUrl().toString().replace("s96-c","s320-c"));
            userData.put(UserKey.NAME.toString(),name);
            userData.put(UserKey.DOB.toString(),dateOfBirth);
            userData.put(UserKey.DATE_REGISTERED.toString(),dateRegistered);
            userData.put(UserKey.GENDER.toString(),gender);
            userData.put(UserKey.IS_ADMIN.toString(),false);
            userData.put(UserKey.IS_DOCTOR.toString(),false);
            UID=user.getUid();

            progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Registering User");
            progressIndicatorFragment.show(getParentFragmentManager(),"RegisterUserProgress");
            new GetNumberOfUsers(this);

        });

    }

    @Override
    public void OnUserRegister(boolean result) {
        progressIndicatorFragment.dismiss();
        if(result){
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        }
        else {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.loginFragment);
        }
    }

    @Override
    public void OnResultCallback(boolean result, int num) {
        if(result){
            if(num==0)
                userData.put(UserKey.IS_ADMIN.toString(),true);
            new RegisterNewUser(this,UID,userData);
        }
        else {
            progressIndicatorFragment.dismiss();
            Toast.makeText(requireActivity(), "Error in Registering User! Please Try Again", Toast.LENGTH_SHORT).show();
        }
    }
}