package com.example.patientdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPage extends AppCompatActivity {

    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        b1 = findViewById(R.id.patient);
        b2 = findViewById(R.id.doctor);

        Intent intent = new Intent(StartPage.this, LoginPage.class);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Client", "patients");
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Client", "doctors");
                startActivity(intent);
            }
        });
    }
}