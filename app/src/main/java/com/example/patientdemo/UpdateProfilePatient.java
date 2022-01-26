package com.example.patientdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfilePatient extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView nav_name;

    DatabaseReference rootRef2, mRef2;

    TextInputEditText up_name2, up_mobile2, up_email2, up_age2, up_password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_patient);

        drawerLayout = findViewById(R.id.drawer_layout);
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText(PatientDashboard.UserName);

        up_name2 = findViewById(R.id.up_name2);
        up_mobile2 = findViewById(R.id.up_mobile2);
        up_email2 = findViewById(R.id.up_email2);
        up_age2 = findViewById(R.id.up_age2);
        up_password2 = findViewById(R.id.up_password2);

        ShowUserData();
    }

    public void ShowUserData() {

        rootRef2 = FirebaseDatabase.getInstance().getReference();

        mRef2 = rootRef2.child("patients").child(PatientDashboard.UserName);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                up_name2.setText(snapshot.child("name").getValue(String.class));
                up_mobile2.setText(snapshot.child("mobile").getValue(String.class));
                up_email2.setText(snapshot.child("email").getValue(String.class));
                up_age2.setText(snapshot.child("age").getValue(String.class));
                up_password2.setText(snapshot.child("password").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef2.addListenerForSingleValueEvent(eventListener);
    }

    public void UpdatePatient(View view) {
        mRef2.child("name").setValue(up_name2.getText().toString());
        mRef2.child("mobile").setValue(up_mobile2.getText().toString());
        mRef2.child("email").setValue(up_email2.getText().toString());
        mRef2.child("age").setValue(up_age2.getText().toString());
        mRef2.child("password").setValue(up_password2.getText().toString());

        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    // ---------------------------- NAVIGATION BAR ---------------------------- //
    public void ClickMenu(View view) {
        PatientDashboard.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        PatientDashboard.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        PatientDashboard.redirectActivity(this, PatientDashboard.class);
    }

    public void ClickUpdateProfile(View view) {
        PatientDashboard.redirectActivity(this, UpdateProfilePatient.class);
    }

    public void ClickLogout(View view) {
        PatientDashboard.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PatientDashboard.closeDrawer(drawerLayout);
    }
    // ----------------------------------------------------------------------- //

}