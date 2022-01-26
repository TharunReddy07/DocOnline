package com.example.patientdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtp extends AppCompatActivity {

    Button buttonGetOTP;
    EditText inputMobile;
    ProgressBar progressBar;
    String Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        Client = getIntent().getStringExtra("Client");

        inputMobile = findViewById(R.id.inputMobile);
        buttonGetOTP = findViewById(R.id.btnGetOtp);
        progressBar = findViewById(R.id.progressBar);

        String UserName = getIntent().getStringExtra("UserName");

        buttonGetOTP.setOnClickListener((v)->{
            if (inputMobile.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            buttonGetOTP.setVisibility(View.INVISIBLE);

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + inputMobile.getText().toString(),
                    30,
                    TimeUnit.SECONDS,
                    SendOtp.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);
                            Toast.makeText(SendOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);

                            Intent intent = new Intent(SendOtp.this,VerifyOtp.class);
                            intent.putExtra("UserName", UserName);
                            intent.putExtra("mobile", inputMobile.getText().toString());
                            intent.putExtra("Client", Client);
                            intent.putExtra("verificationId", verificationId);
                            startActivity(intent);
                        }
                    }
            );
        });
    }
}