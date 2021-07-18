package com.example.doctordashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAppointment extends AppCompatActivity {

    DrawerLayout drawerLayout;

    ArrayList<String> slotTimes = new ArrayList<>();
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();

    HashMap<String, ArrayList<String>> itemList = new HashMap<>();
    BookAppointmentAdapter adapter;

    RecyclerView slotCards;
    CircleImageView img;
    TextView name, exp, degree, place;
    Button btn;
    String dr_name;
    EditText remarks;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        drawerLayout = findViewById(R.id.drawer_layout);

        dr_name = getIntent().getExtras().getString("dr_app_name");
        slotCards = findViewById(R.id.dr_app_place2);
        btn = findViewById(R.id.dr_app_btn);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("slots");

        img = findViewById(R.id.dr_app_img);
        name = findViewById(R.id.dr_app_name);
        exp = findViewById(R.id.dr_app_exp);
        degree = findViewById(R.id.dr_app_degree);
        place = findViewById(R.id.dr_app_place);
        remarks = findViewById(R.id.remarks);

        name.setText(getIntent().getExtras().getString("dr_app_name"));
        exp.setText(getIntent().getExtras().getString("dr_app_exp"));
        degree.setText(getIntent().getExtras().getString("dr_app_degree"));
        place.setText(getIntent().getExtras().getString("dr_app_place"));
        Glide.with(this).load(getIntent().getExtras().getString("dr_app_img"))
                .error(R.drawable.doctor_image).into(img);

        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slotTimes.clear();
                docTimings.clear();
                docReserved.clear();
                for (DataSnapshot slot: snapshot.getChildren()) {
                    String stand = slot.getValue(String.class);

                    slotTimes.add(slot.getKey());
                    if (stand.equals("Available"))
                        docTimings.add(slot.getKey());
                    else if (stand.equals("Reserved"))
                        docReserved.add(slot.getKey());
                }
                Collections.sort(slotTimes, new Comparator<String>() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public int compare(String o1, String o2) {
                        try {
                            return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
                        } catch (ParseException e) {
                            return 0;
                        }
                    }
                });
                slotCards(slotTimes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef.child("Kakashi").addValueEventListener(mListener);

    }

    private void slotCards(ArrayList<String> slotTimes) {
        slotCards.setHasFixedSize(true);
        slotCards.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> sectionList = new ArrayList<>();
        sectionList.add("Today's Slots");
        itemList.put(sectionList.get(0), slotTimes);
        adapter = new BookAppointmentAdapter(this, sectionList, itemList, docTimings, docReserved);
        GridLayoutManager manager = new GridLayoutManager(this, 3);

        slotCards.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        slotCards.setAdapter(adapter);

        btn.setOnClickListener(v -> {
            String sItem = adapter.getSelected();
            if (sItem != null) {
                Intent intent = new Intent(getApplicationContext(), Payment.class);

                intent.putExtra("slot", sItem);
                intent.putExtra("msg", remarks.getText().toString());

                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(this, "Please select a Slot", Toast.LENGTH_SHORT).show();
        });
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