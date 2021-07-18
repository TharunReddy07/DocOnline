package com.example.doctordashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Specialist extends AppCompatActivity {

    DrawerLayout drawerLayout;

    RecyclerView docCards;
    RecyclerView.Adapter adapter;
    SpecialistAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist);

        drawerLayout = findViewById(R.id.drawer_layout);

        docCards = findViewById(R.id.docCards);
        docCards();
    }

    private void docCards() {
        docCards.setHasFixedSize(true);
        docCards.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<SpecialistModel> CardLocations = new ArrayList<>();

        CardLocations.add(new SpecialistModel(R.drawable.cold_fever, "Cold, Cough and Fever",
                "For common health concerns", "Fever, Eye Infection, Stomach Ache, Headache"));
        CardLocations.add(new SpecialistModel(R.drawable.covid, "Covid Consultation",
                "Treatment of COVID-19", "Cough, Fever and Breathlessness"));
        CardLocations.add(new SpecialistModel(R.drawable.cardiology, "Cardiology",
                "For Heart and Blood pressure problems", "Chest pain, Heart pain, Cholesterol"));
        CardLocations.add(new SpecialistModel(R.drawable.child_development, "Child Development",
                "For development disorder in children", "Learning disability, Development delay"));

        listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), DoctorList.class);
            intent.putExtra("title", CardLocations.get(position).getTitle());
            startActivity(intent);
        };
        adapter = new SpecialistAdapter(CardLocations, listener);
        docCards.setAdapter(adapter);
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