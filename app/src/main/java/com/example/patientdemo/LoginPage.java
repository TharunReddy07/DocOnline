package com.example.patientdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    SignInButton google_btn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    TextInputLayout username, password;
    Button login, forgetPassword, createAccount;

    String UserName, PassWord, Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Client = getIntent().getStringExtra("Client");


        // ---------------------------------- LOGIN ------------------------------------------ //
        username = findViewById(R.id.login_username);
        password =  findViewById(R.id.login_password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = username.getEditText().getText().toString();
                PassWord = password.getEditText().getText().toString();
                loginUser(v);
            }
        });
        // ----------------------------------------------------------------------------------- //


        // ------------------------------ FORGET_PASSWORD ------------------------------------ //
        forgetPassword = findViewById(R.id.forget_password);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = username.getEditText().getText().toString();
                ForgetPassword();
            }
        });
        // ----------------------------------------------------------------------------------- //


        // ------------------------------ GOOGLE SIGN IN -------------------------------------- //
        google_btn = findViewById(R.id.google_sign_in);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("348292897435-nlafd948i1eq9civshhbr7odjlcie6d6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(LoginPage.this, googleSignInOptions);
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        // ------------------------------------------------------------------------------------//

        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Client.equals("patients"))
                    intent = new Intent(LoginPage.this, CreatePatient.class);
                else
                    intent = new Intent(LoginPage.this, DoctorDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


    // ----------------------------------- LOGIN ------------------------------------------ //
    private Boolean validateUsername(){
        if(UserName.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        } else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        if(PassWord.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        } else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view){
        if(validateUsername() && validatePassword()){
            Toast.makeText(this, Client, Toast.LENGTH_SHORT).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Client);
            Query getData = reference.orderByChild("username").equalTo(UserName);

            getData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        username.setError(null);
                        username.setErrorEnabled(false);

                        String passwordFromDB = snapshot.child(UserName).child("password").getValue(String.class);
                        if(passwordFromDB != null && passwordFromDB.equals(PassWord)){
                            password.setError(null);
                            password.setErrorEnabled(false);

                            String MobileNo = snapshot.child(UserName).child("mobile").getValue(String.class);
                            if (MobileNo != null) {
                                DirectSendOTP(MobileNo, false);
                            }
                            else {
                                Intent intent = new Intent(LoginPage.this, SendOtp.class);
                                intent.putExtra("UserName", UserName);
                                intent.putExtra("Client", Client);
                                startActivity(intent);
                            }
                        }
                        else{
                            password.setError("Wrong Password");
                            password.requestFocus();
                        }
                    }
                    else{
                        username.setError("No such User exist");
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    // ------------------------------------------------------------------------------------ //


    // ------------------------------ FORGET_PASSWORD ------------------------------------ //
    private void ForgetPassword() {
        if(validateUsername()){
            DatabaseReference getData = FirebaseDatabase.getInstance().getReference(Client).child(UserName);
            //Query getData = reference.orderByChild("username").equalTo(UserName);

            getData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        username.setError(null);
                        username.setErrorEnabled(false);

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String MobileNo = snapshot.child("mobile").getValue(String.class);
                        if (MobileNo != null) {
                            DirectSendOTP(MobileNo, true);
                        }
                        else {
                            username.setError("Your mobile number is not registered");
                            username.requestFocus();
                        }
                    }
                    else{
                        username.setError("No such User exist");
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    // ----------------------------------------------------------------------------------- //


    // ------------------------------ GOOGLE SIGN IN -------------------------------------- //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(
                                googleSignInAccount.getIdToken(), null);

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(
                                this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPage.this, "Success", Toast.LENGTH_SHORT).show();
                                    checkUser();
                                }
                            }
                        });
                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkUser() {
        Query userData;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String UserName = firebaseUser.getDisplayName();
            userData = FirebaseDatabase.getInstance().getReference(Client).child(UserName);

            userData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        String mobileNo = snapshot.child("mobile").getValue(String.class);

                        if (mobileNo != null) {
                            DirectSendOTP(mobileNo, false);
                        }
                        else {
                            Intent intent = new Intent(LoginPage.this, SendOtp.class);
                            intent.putExtra("UserName", UserName);
                            intent.putExtra("Client", Client);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                    else {
                        Intent intent;
                        if (Client.equals("patients")) {
                            CreatePatientModel model = new CreatePatientModel(UserName, UserName,
                                    firebaseUser.getEmail(), null, "", "");
                            reference = FirebaseDatabase.getInstance().getReference("patients");
                            reference.child(UserName).setValue(model);
                            intent = new Intent(LoginPage.this, SendOtp.class);
                        }
                        else
                            intent = new Intent(LoginPage.this, DoctorDetails.class);

                        intent.putExtra("UserName", UserName);
                        intent.putExtra("Client", Client);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    // ------------------------------------------------------------------------------------ //


    public void DirectSendOTP(String mobileNo, boolean NewPassword) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNo,
                30,
                TimeUnit.SECONDS,
                LoginPage.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Intent intent;
                        if (NewPassword)
                            intent = new Intent(LoginPage.this, VerifyOtpNewPassword.class);
                        else
                            intent = new Intent(LoginPage.this, VerifyOtp.class);

                        intent.putExtra("UserName", UserName);
                        intent.putExtra("mobile", mobileNo);
                        intent.putExtra("Client", Client);
                        intent.putExtra("verificationId", verificationId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
        );

    }
}