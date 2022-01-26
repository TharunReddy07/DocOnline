package com.example.patientdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtpNewPassword extends AppCompatActivity {

    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private String verificationId, Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp_new_password);


        String UserName = getIntent().getStringExtra("UserName");
        Client = getIntent().getStringExtra("Client");

        TextView textMobile = findViewById(R.id.textmobile);

        textMobile.setText(String.format(
                "+91-%s",getIntent().getStringExtra("mobile")
        ));

        inputCode1 = findViewById(R.id.inputcode1);
        inputCode2 = findViewById(R.id.inputcode2);
        inputCode3 = findViewById(R.id.inputcode3);
        inputCode4 = findViewById(R.id.inputcode4);
        inputCode5 = findViewById(R.id.inputcode5);
        inputCode6 = findViewById(R.id.inputcode6);

        setupOTPInputs();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button buttonVerify = findViewById(R.id.buttonVerify);

        verificationId = getIntent().getStringExtra("verificationId");

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerifyOtpNewPassword.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputCode1.getText().toString()+
                        inputCode2.getText().toString()+
                        inputCode3.getText().toString()+
                        inputCode4.getText().toString()+
                        inputCode5.getText().toString()+
                        inputCode6.getText().toString();

                if (verificationId != null){
                    progressBar.setVisibility(View.VISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonVerify.setVisibility(View.VISIBLE);

                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(VerifyOtpNewPassword.this, UpdatePassword.class);
                                        intent.putExtra("UserName", UserName);
                                        intent.putExtra("Client", Client);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(VerifyOtpNewPassword.this, "The verifiaction code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        findViewById(R.id.textResendOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        30,
                        TimeUnit.SECONDS,
                        VerifyOtpNewPassword.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(VerifyOtpNewPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(VerifyOtpNewPassword.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }


    private void setupOTPInputs() {

        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputCode2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputCode3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputCode4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputCode5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputCode6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}