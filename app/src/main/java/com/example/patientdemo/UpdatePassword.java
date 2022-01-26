package com.example.patientdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePassword extends AppCompatActivity {

    TextInputLayout password, confirmPassword;
    Button update;
    ProgressBar progressBar;
    String UserName, Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        UserName = getIntent().getStringExtra("UserName");
        Client = getIntent().getStringExtra("Client");

        password = findViewById(R.id.Newpassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        update = (Button)findViewById(R.id.update_password);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePassword() || !confirmPassword()){
                    return;
                }

                String newPassword = password.getEditText().getText().toString().trim();

                //update Data in firebase and in sessions
                DatabaseReference reference;
                reference = FirebaseDatabase.getInstance().getReference(Client);
                reference.child(UserName).child("password").setValue(newPassword);

                Intent intent;
                if (Client.equals("patients"))
                    intent = new Intent(getApplicationContext(), PatientDashboard.class);
                else
                    intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.putExtra("UserName", UserName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
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
        if(!val1.equals(val2)){
            confirmPassword.setError("password doesn't match");
            return false;
        }
        else {
            confirmPassword.setError(null);
            return true;
        }
    }

}