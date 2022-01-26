package com.example.patientdemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveAppointmentsAdapter extends FirebaseRecyclerAdapter<ActiveAppointmentsModel, ActiveAppointmentsAdapter.ViewHolder> {

    Context context;
    private DatabaseReference ref1, ref2;

    public ActiveAppointmentsAdapter(@NonNull FirebaseRecyclerOptions<ActiveAppointmentsModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ActiveAppointmentsModel model) {

        holder.app_dr_price.setText(model.getPrice());
        holder.app_dr_name.setText(model.getName());
        holder.app_dr_time.setText(model.getTime());

        Glide.with(holder.app_dr_image.getContext())
                .load(model.getImage())
                .error(R.drawable.doctor_image)
                .into(holder.app_dr_image);

        ref1 = FirebaseDatabase.getInstance().getReference("doctors").child(model.getName());

        ref2 = FirebaseDatabase.getInstance().getReference("patients").child(PatientDashboard.UserName);

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete Appointment");
                dialog.setMessage("Are you sure you want to Delete this Appointment ?");

                dialog.setPositiveButton("Yes", (dialog1, which) -> {
                    ref1.child("slots").child(model.getTime()).setValue("Available");
                    ref1.child("my_app").child(model.getTime()).removeValue();

                    ref2.child("active_app").child(model.getName() + model.getTime()).removeValue();
                });

                dialog.setNegativeButton("No", (dialog12, which) -> {

                });

                dialog.show();
            }
        });

        holder.reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.example.patientdemo.ReSchedule.class);

                intent.putExtra("slot", model.getTime());
                intent.putExtra("dr_name", model.getName());
                intent.putExtra("dr_img", model.getImage());

                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_active_appointments, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final CircleImageView app_dr_image;
        final TextView app_dr_name, app_dr_price, app_dr_time;
        Button del, reschedule;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            app_dr_image = itemView.findViewById(R.id.app_dr_image);
            app_dr_name = itemView.findViewById(R.id.app_dr_name);
            app_dr_price = itemView.findViewById(R.id.app_dr_price);
            app_dr_time = itemView.findViewById(R.id.app_dr_time);

            del = itemView.findViewById(R.id.delete);
            reschedule = itemView.findViewById(R.id.reschedule);
        }
    }
}
