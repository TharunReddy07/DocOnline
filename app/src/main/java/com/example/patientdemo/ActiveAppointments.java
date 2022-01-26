package com.example.patientdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ActiveAppointments extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView nav_name;

    RecyclerView recyclerView;
    Button cancel, reschedule;

    ActiveAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_appointments);

        drawerLayout = findViewById(R.id.drawer_layout);
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText(PatientDashboard.UserName);

        cancel = findViewById(R.id.delete);
        reschedule = findViewById(R.id.reschedule);
        recyclerView = findViewById(R.id.appointments_recycler);

        Appointments();
    }

    private void Appointments() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<com.example.patientdemo.ActiveAppointmentsModel> options =
                new FirebaseRecyclerOptions.Builder<com.example.patientdemo.ActiveAppointmentsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patients").child(PatientDashboard.UserName).child("active_app"),
                                com.example.patientdemo.ActiveAppointmentsModel.class)
                        .build();

        adapter = new ActiveAppointmentsAdapter(options, this);
        recyclerView.setAdapter(adapter);
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
