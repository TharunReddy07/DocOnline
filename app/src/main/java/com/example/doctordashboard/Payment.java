package com.example.doctordashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class Payment extends AppCompatActivity {

    Button Sbtn, Fbtn;
    private DatabaseReference ref1, ref2;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        String sItem = getIntent().getExtras().getString("slot");
        String msg = getIntent().getExtras().getString("msg");

        Sbtn = findViewById(R.id.complete_payment);
        Fbtn = findViewById(R.id.cancel_payment);
        ref1 = FirebaseDatabase.getInstance().getReference("slots").child("Kakashi");
        ref2 = FirebaseDatabase.getInstance().getReference("patients").child("tharun").child("active_app");

        Sbtn.setOnClickListener(v -> {
            ref1.child(sItem).setValue("Booked");

            ref2.child("Kakashi" + sItem).child("name").setValue("Dr. Kakashi");
            ref2.child("Kakashi" + sItem).child("time").setValue(sItem);
            ref2.child("Kakashi" + sItem).child("price").setValue("800");
            ref2.child("Kakashi" + sItem).child("remarks").setValue(msg);
            ref2.child("Kakashi" + sItem).child("image").setValue("https://firebasestorage.googleapis.com/v0/b/my-application-6d359.appspot.com/o/images%2Fkakashi.jpg?alt=media&token=348a2050-641e-44f8-9c3f-80c740c917a3");

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                ref1.child(sItem).setValue("Available");
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cancel Payment");
        dialog.setMessage("Do you want to cancel Payment?");

        String sItem = getIntent().getExtras().getString("slot");
        dialog.setPositiveButton("Yes", (dialog1, which) -> {
            ref1.child(sItem).setValue("Available");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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