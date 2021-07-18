package com.example.doctordashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfile extends AppCompatActivity {

    DrawerLayout drawerLayout;
    DatabaseReference reference;

    String userName, fullName, email, mobile, exp, hospital, price, password;
    TextInputEditText up_name, up_mobile, up_email, up_exp, up_hospital, up_price, up_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        reference = FirebaseDatabase.getInstance().getReference("doctors");

        drawerLayout = findViewById(R.id.drawer_layout);

        up_name = findViewById(R.id.up_name);
        up_mobile = findViewById(R.id.up_mobile);
        up_email = findViewById(R.id.up_email);
        up_exp = findViewById(R.id.up_exp);
        up_hospital = findViewById(R.id.up_hospital);
        up_price = findViewById(R.id.up_price);
        up_password = findViewById(R.id.up_password);

        ShowUserData();
    }

    public void ShowUserData() {
        Intent intent = getIntent();

        userName = "Kakashi";

        fullName = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        mobile = intent.getStringExtra("mobile");
        exp = intent.getStringExtra("exp");
        hospital = intent.getStringExtra("hospital");
        price = intent.getStringExtra("price");
        password = intent.getStringExtra("password");

        up_name.setText(fullName);
        up_mobile.setText(mobile);
        up_email.setText(email);
        up_exp.setText(exp);
        up_hospital.setText(hospital);
        up_price.setText(price);
        up_password.setText(password);
    }

    public void UpdateButton(View view) {
        reference.child(userName).child("fullname").setValue(up_name.getText().toString());
        reference.child(userName).child("mobile").setValue(up_mobile.getText().toString());
        reference.child(userName).child("email").setValue(up_email.getText().toString());
        reference.child(userName).child("exp").setValue(up_exp.getText().toString());
        reference.child(userName).child("hospital").setValue(up_hospital.getText().toString());
        reference.child(userName).child("price").setValue(up_price.getText().toString());
        reference.child(userName).child("password").setValue(up_password.getText().toString());

        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
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
