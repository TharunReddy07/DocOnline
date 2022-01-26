package com.example.patientdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAppointmentsDoctor extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView nav_name;
    CircleImageView image;

    RecyclerView drCards;
    MyAppointmentsDoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments_doctor);

        drawerLayout = findViewById(R.id.drawer_layout);
        image = findViewById(R.id.nav_image);
        image.setImageResource(R.drawable.doctor_image);
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText("Dr."+MainActivity.UserName);

        drCards = findViewById(R.id.appointments_recycler_doc);
        drCards();

    }

    private void drCards() {
        drCards.setHasFixedSize(true);
        drCards.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MyAppointmentsDoctorModel> options =
                new FirebaseRecyclerOptions.Builder<MyAppointmentsDoctorModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("doctors")
                                .child(MainActivity.UserName).child("my_app"), MyAppointmentsDoctorModel.class)
                        .build();

        adapter = new MyAppointmentsDoctorAdapter(options, this);
        drCards.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



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
        MainActivity.redirectActivity(this, UpdateProfile.class);
    }

    public void ClickLogout(View view) {
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}