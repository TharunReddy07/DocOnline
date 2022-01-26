package com.example.patientdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class Payment extends AppCompatActivity {

    Button Sbtn, Fbtn;
    private DatabaseReference ref1, ref2;

    Timer timer;
    String sItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        sItem = getIntent().getExtras().getString("slot");
        String msg = getIntent().getExtras().getString("msg");
        String dr_name = getIntent().getExtras().getString("dr_name");
        String dr_price = getIntent().getExtras().getString("dr_price");

        Sbtn = findViewById(R.id.complete_payment);
        Fbtn = findViewById(R.id.cancel_payment);

        ref1 = FirebaseDatabase.getInstance().getReference("doctors").child(dr_name);

        ref2 = FirebaseDatabase.getInstance().getReference("patients").child(PatientDashboard.UserName).child("active_app");

        Sbtn.setOnClickListener(v -> {
            ref1.child("slots").child(sItem).setValue("Booked");

            ref1.child("my_app").child(sItem).child("patient").setValue(PatientDashboard.UserName);
            ref1.child("my_app").child(sItem).child("remarks").setValue(msg);
            ref1.child("my_app").child(sItem).child("slot").setValue(sItem);

            ref2.child(dr_name + sItem).child("name").setValue(dr_name);
            ref2.child(dr_name + sItem).child("time").setValue(sItem);
            ref2.child(dr_name + sItem).child("price").setValue(dr_price);
            ref2.child(dr_name + sItem).child("remarks").setValue(msg);
            //ref2.child(dr_name + sItem).child("image").setValue("https://firebasestorage.googleapis.com/v0/b/my-application-6d359.appspot.com/o/images%2Fkakashi.jpg?alt=media&token=348a2050-641e-44f8-9c3f-80c740c917a3");

            timer.cancel();
            Toast.makeText(this, "Payment Done", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ActiveAppointments.class);
            startActivity(intent);
            finish();
        });

        Fbtn.setOnClickListener(v -> {
            onBackPressed();
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), PatientDashboard.class);
                intent.putExtra("UserName", PatientDashboard.UserName);
                ref1.child("slots").child(sItem).setValue("Available");
                startActivity(intent);
                finish();
            }
        }, 5000*60);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cancel Payment");
        dialog.setMessage("Do you want to cancel Payment?");

        sItem = getIntent().getExtras().getString("slot");
        dialog.setPositiveButton("Yes", (dialog1, which) -> {
            ref1.child("slots").child(sItem).setValue("Available");
            Intent intent = new Intent(getApplicationContext(), PatientDashboard.class);
            intent.putExtra("UserName", PatientDashboard.UserName);
            startActivity(intent);
            finish();
        });

        dialog.setNegativeButton("No", (dialog12, which) -> {
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}