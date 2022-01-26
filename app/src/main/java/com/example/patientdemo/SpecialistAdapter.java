package com.example.patientdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.CardViewHolder> {

    ArrayList<com.example.patientdemo.SpecialistModel> CardLocations;
    RecyclerViewClickListener listener;

    public SpecialistAdapter(ArrayList<com.example.patientdemo.SpecialistModel> CardLocations, RecyclerViewClickListener listener) {
        this.CardLocations = CardLocations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_specilaist, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        com.example.patientdemo.SpecialistModel cardHelperClass = CardLocations.get(position);

        holder.image.setImageResource(cardHelperClass.getImage());
        holder.desc.setText(cardHelperClass.getDesc());
        holder.prob.setText(cardHelperClass.getProb());
        holder.title.setText(cardHelperClass.getTitle());

    }

    @Override
    public int getItemCount() {
        return CardLocations.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, desc, prob;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.card_image);
            title = itemView.findViewById(R.id.card_title);
            desc = itemView.findViewById(R.id.card_desc);
            prob = itemView.findViewById(R.id.card_prob);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            listener.onClick(v, getAdapterPosition());

        }
    }
}
