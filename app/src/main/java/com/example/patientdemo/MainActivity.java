package com.example.patientdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    CardView my_schedule, my_app;
    GoogleSignInClient googleSignInClient;
    public static String UserName;
    TextView nav_name;
    CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        nav_name = findViewById(R.id.nav_name);
        nav_name.setText("Dr."+UserName);
        image = findViewById(R.id.nav_image);
        image.setImageResource(R.drawable.doctor_image);

        my_schedule = findViewById(R.id.my_schedule);
        my_app = findViewById(R.id.my_app);

        UserName = getIntent().getStringExtra("UserName");

        my_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorSchedule.class);
                intent.putExtra("UserName", UserName);
                startActivity(intent);
            }
        });

        my_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyAppointmentsDoctor.class);
                intent.putExtra("UserName", UserName);
                startActivity(intent);
            }
        });

    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void ClickHome(View view)  {
        recreate();
    }

    public void ClickUpdateProfile(View view) {
        redirectActivity(this, com.example.patientdemo.UpdateProfile.class);
    }

    public void ClickLogout(View view) {
        logout(this);
    }

    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(activity, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(activity, "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, StartPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        System.exit(100);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Signout Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aclass) {
        Intent intent = new Intent(activity, aclass);
        intent.putExtra("UserName", UserName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}