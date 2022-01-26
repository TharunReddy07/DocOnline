package com.example.patientdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputLayout fullName, username, mobileNo, edu, exp, hospital, price;
    String  selected_spe;
    Button btn;
    String UserName = "";
    String Email = "";
    Spinner special;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference ref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        mobileNo = findViewById(R.id.mobileNo);
        edu = findViewById(R.id.education);
        special = findViewById(R.id.spinner);
        exp = findViewById(R.id.exp);
        hospital = findViewById(R.id.hospital);
        price = findViewById(R.id.price);
        btn = findViewById(R.id.confirm);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            UserName = firebaseUser.getDisplayName();
            Email = firebaseUser.getEmail();
        }

        special.setOnItemSelectedListener(this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName = fullName.getEditText().getText().toString();
                UserName = username.getEditText().getText().toString();
                String MobileNo = mobileNo.getEditText().getText().toString();
                String Edu = edu.getEditText().getText().toString();
                String Exp = exp.getEditText().getText().toString();
                String Hospital = hospital.getEditText().getText().toString();
                String Price = price.getEditText().getText().toString();

                DoctorDetailsModel model = new DoctorDetailsModel(FullName, UserName, Email, MobileNo, Edu, selected_spe, Exp, Hospital, Price);

                DatabaseReference dataEntry;
                dataEntry = FirebaseDatabase.getInstance().getReference("doctors");
                dataEntry.child(UserName).setValue(model);

                ref1 = FirebaseDatabase.getInstance().getReference("specialists").child(selected_spe).child(UserName);

                ref1.child("dr_name").setValue(UserName);
                ref1.child("degree").setValue(Edu);
                ref1.child("exp").setValue(Exp);
                ref1.child("place").setValue(Hospital);
                ref1.child("price").setValue(Price);

                Intent intent = new Intent(DoctorDetails.this, VerifyOtp.class);
                intent.putExtra("UserName", UserName);
                intent.putExtra("mobile", MobileNo);
                intent.putExtra("Client", "doctors");

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_spe = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}