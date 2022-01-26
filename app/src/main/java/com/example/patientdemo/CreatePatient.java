package com.example.patientdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class CreatePatient extends AppCompatActivity {

    TextInputLayout name,user_name,mobile_Number, email, password,age,confirmPassword;
    Button Register;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);

        name = findViewById(R.id.lname);
        user_name = findViewById(R.id.lusername);
        email = findViewById(R.id.lemail);
        mobile_Number = findViewById(R.id.lmobileNo);
        password = findViewById(R.id.lpassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        age = findViewById(R.id.lage);

        Register = findViewById(R.id.lgo);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


    private Boolean validateName(){
        String val = name.getEditText().getText().toString();

        if(val.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = user_name.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            user_name.setError("Field cannot be empty");
            return false;
        } else if(val.length()>=15) {
            user_name.setError("Username is too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            user_name.setError("White Spaces are not allowed");
            return false;
        }
        else{
            user_name.setError(null);
            return true;
        }
    }

    private Boolean validateMobileNo(){
        String val = mobile_Number.getEditText().getText().toString();

        if(val.isEmpty()){
            mobile_Number.setError("Field cannot be empty");
            return false;
        }
        else{
            mobile_Number.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    private Boolean confirmPassword(){
        String val1 = confirmPassword.getEditText().getText().toString();
        String val2 = password.getEditText().getText().toString();
        if(val1.equals(val2)){
            confirmPassword.setError(null);
            return true;
        }
        else {
            confirmPassword.setError("password doesn't match");
            return false;
        }
    }

    private Boolean validateAge(){
        String val = age.getEditText().getText().toString();
//        String agePattern = "[0-100]";

        if(val.isEmpty()){
            age.setError("Field cannot be empty");
            return false;}
//        else if(!val.matches(agePattern)){
//            age.setError("Invalid age");
//            return false;
//        }
        else{
            age.setError(null);
            return true;
        }
    }


    public void registerUser(){

        if(!validateName() || !validateAge() || !validateEmail() || !validateMobileNo() || !validateUsername()|| !validatePassword() || !confirmPassword()){
            return;
        }

        reference = FirebaseDatabase.getInstance().getReference("patients");

        String Name = name.getEditText().getText().toString();
        String Username = user_name.getEditText().getText().toString();
        String Email = email.getEditText().getText().toString();
        String Mobile = mobile_Number.getEditText().getText().toString();
        String Password = password.getEditText().getText().toString();
        String Age = age.getEditText().getText().toString();

        CreatePatientModel model = new CreatePatientModel(Name, Username, Email, Mobile, Password, Age);
        reference.child(Username).setValue(model);

        sendOTP(Username, Mobile);
    }

    private void sendOTP(String UserName, String mobileNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNo,
                30,
                TimeUnit.SECONDS,
                CreatePatient.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(CreatePatient.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Intent intent;
                        intent = new Intent(CreatePatient.this, VerifyOtp.class);

                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("mobile", mobileNo);
                        intent.putExtra("UserName", UserName);
                        intent.putExtra("Client", "patients");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
        );
    }

}