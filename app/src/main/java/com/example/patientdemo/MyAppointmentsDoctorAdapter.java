package com.example.patientdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;


public class MyAppointmentsDoctorAdapter extends FirebaseRecyclerAdapter<MyAppointmentsDoctorModel, MyAppointmentsDoctorAdapter.drViewHolder> {

    Context context;
    public static String UserName;
    private DatabaseReference ref1;
    String price_paid;

    public MyAppointmentsDoctorAdapter(@NonNull FirebaseRecyclerOptions<MyAppointmentsDoctorModel> options, Context context) {
        super(options);
        this.context = context;
        MyAppointmentsDoctorAdapter.UserName = MainActivity.UserName;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAppointmentsDoctorAdapter.drViewHolder holder, int position, @NonNull MyAppointmentsDoctorModel docmodel) {

        holder.patient.setText(docmodel.getPatient());
        holder.remarks.setText(docmodel.getRemarks());
        holder.time.setText(docmodel.getSlot());

    }

    @NonNull
    @Override
    public MyAppointmentsDoctorAdapter.drViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_my_appointments_doctor, parent, false);
        return new MyAppointmentsDoctorAdapter.drViewHolder(view);
    }

    class drViewHolder extends RecyclerView.ViewHolder {

        final TextView patient, remarks, time;

        public drViewHolder(@NonNull View itemView) {
            super(itemView);

            patient = itemView.findViewById(R.id.patient_name);
            remarks = itemView.findViewById(R.id.patient_remarks);
            time = itemView.findViewById(R.id.patient_time);
        }
    }
}

