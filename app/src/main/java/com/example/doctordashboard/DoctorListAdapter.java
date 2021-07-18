package com.example.doctordashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListAdapter extends FirebaseRecyclerAdapter<DoctorListModel, DoctorListAdapter.drViewHolder> {

    Context context;

    public DoctorListAdapter(@NonNull FirebaseRecyclerOptions<DoctorListModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull drViewHolder holder, int position, @NonNull DoctorListModel docmodel) {

        holder.dr_name.setText(docmodel.getDr_name());
        holder.exp.setText(docmodel.getExp());
        holder.price.setText(docmodel.getPrice());
        holder.degree.setText(docmodel.getDegree());
        holder.place.setText(docmodel.getPlace());

        Glide.with(holder.img.getContext())
                .load(docmodel.getImg())
                .error(R.drawable.doctor_image)
                .into(holder.img);

        holder.btn.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), BookAppointment.class);

            intent.putExtra("dr_app_name", docmodel.getDr_name());
            intent.putExtra("dr_app_exp", docmodel.getExp());
            intent.putExtra("dr_app_degree", docmodel.getDegree());
            intent.putExtra("dr_app_place", docmodel.getPlace());
            intent.putExtra("dr_app_img", docmodel.getImg());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
        });

    }

    @NonNull
    @Override
    public drViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_doctor_list, parent, false);
        return new drViewHolder(view);
    }

    class drViewHolder extends RecyclerView.ViewHolder {

        final CircleImageView img;
        final TextView dr_name, exp, price, degree, place;
        Button btn;

        public drViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.dr_card_image);
            dr_name = itemView.findViewById(R.id.dr_card_name);
            exp = itemView.findViewById(R.id.dr_card_exp);
            price = itemView.findViewById(R.id.dr_card_price);
            degree = itemView.findViewById(R.id.dr_card_degree);
            place = itemView.findViewById(R.id.dr_card_place);

            btn = itemView.findViewById(R.id.book_btn);
        }
    }

}
