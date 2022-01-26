package com.example.patientdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorList extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView nav_name;
    public static String UserName;

    RecyclerView drCards;
    DoctorListAdapter adapter;
    public String specialist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        UserName = getIntent().getStringExtra("UserName");
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText(UserName);

        Bundle extras = getIntent().getExtras();
        specialist = extras.getString("title");

        drCards = findViewById(R.id.drCards);
        drCards();
    }

    private void drCards() {
        drCards.setHasFixedSize(true);
        drCards.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<DoctorListModel> options =
                new FirebaseRecyclerOptions.Builder<DoctorListModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("specialists").child(specialist), DoctorListModel.class)
                        .build();

        adapter = new DoctorListAdapter(options, this);
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