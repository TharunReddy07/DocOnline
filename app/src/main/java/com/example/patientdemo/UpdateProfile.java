package com.example.patientdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    TextView nav_name;
    CircleImageView image;

    Spinner special;
    DatabaseReference rootRef, mRef;

    String selected_spe;
    TextInputEditText up_name, up_mobile, up_email, up_exp, up_hospital, up_price, up_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        image = findViewById(R.id.nav_image);
        image.setImageResource(R.drawable.doctor_image);
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText("Dr."+MainActivity.UserName);

        up_name = findViewById(R.id.up_name);
        up_mobile = findViewById(R.id.up_mobile);
        up_email = findViewById(R.id.up_email);
        up_exp = findViewById(R.id.up_exp);
        up_hospital = findViewById(R.id.up_hospital);
        up_price = findViewById(R.id.up_price);
        up_password = findViewById(R.id.up_password);
        special = findViewById(R.id.spinner);

        ShowUserData();
    }

    public void ShowUserData() {

        rootRef = FirebaseDatabase.getInstance().getReference();

        mRef = rootRef.child("doctors").child(MainActivity.UserName);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                up_name.setText(snapshot.child("name").getValue(String.class));
                up_mobile.setText(snapshot.child("mobile").getValue(String.class));
                up_email.setText(snapshot.child("email").getValue(String.class));
                up_exp.setText(snapshot.child("experience").getValue(String.class));
                up_hospital.setText(snapshot.child("hospital").getValue(String.class));
                up_price.setText(snapshot.child("price").getValue(String.class));
                up_password.setText(snapshot.child("password").getValue(String.class));
                selected_spe = snapshot.child("specialization").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        };
        mRef.addListenerForSingleValueEvent(eventListener);
    }

    public void UpdateButton(View view) {
        mRef.child("name").setValue(up_name.getText().toString());
        mRef.child("mobile").setValue(up_mobile.getText().toString());
        mRef.child("email").setValue(up_email.getText().toString());
        mRef.child("experience").setValue(up_exp.getText().toString());
        mRef.child("hospital").setValue(up_hospital.getText().toString());
        mRef.child("price").setValue(up_price.getText().toString());
        mRef.child("password").setValue(up_password.getText().toString());
        mRef.child("specialization").setValue(selected_spe);

        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_spe = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    // --------------------- NAVIGATION BAR ---------------------------------//
    public void ClickMenu(View view) {
        MainActivity.openDrawer(drawerLayout);
    }
    
    public void ClickLogo(View view) {
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickUpdateProfile(View view) {
        recreate();
    }

    public void ClickLogout(View view) {
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
    //----------------------------------------------------------------------//
}
