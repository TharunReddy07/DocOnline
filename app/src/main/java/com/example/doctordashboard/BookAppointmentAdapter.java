package com.example.doctordashboard;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class BookAppointmentAdapter extends SectionedRecyclerViewAdapter<BookAppointmentAdapter.ViewHolder> {

    Activity activity;
    ArrayList<String> sectionList;
    HashMap<String, ArrayList<String>> itemList;
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();

    int selectedSection = -1;
    int selectedItem = -1;

    private DatabaseReference ref;

    public BookAppointmentAdapter(Activity activity, ArrayList<String> sectionList, HashMap<String, ArrayList<String>> itemList,
                           ArrayList<String> docTimings, ArrayList<String> docReserved) {
        this.activity = activity;
        this.sectionList = sectionList;
        this.itemList = itemList;
        this.docTimings = docTimings;
        this.docReserved = docReserved;

        ref = FirebaseDatabase.getInstance().getReference("slots").child("Kakashi");;
        notifyDataSetChanged();
    }



    @Override
    public int getSectionCount() {
        return sectionList.size();
    }

    @Override
    public int getItemCount(int section) {
        return itemList.get(sectionList.get(section)).size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        holder.textView.setText(sectionList.get(section));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        String sItem = itemList.get(sectionList.get(section)).get(relativePosition);

        holder.textView.setText(sItem);
        holder.textView.setOnClickListener(v -> {
            if (docTimings.contains(sItem)) {
                Toast.makeText(activity, sItem, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activity, "Please select an Available slot", Toast.LENGTH_SHORT).show();
            }
            selectedSection = section;
            selectedItem = relativePosition;
            notifyDataSetChanged();
        });


        if (selectedSection == section && selectedItem == relativePosition && docTimings.contains(sItem)) {
            holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_fill));
            holder.textView.setTextColor(Color.WHITE);
        }
        else{
            if (!docTimings.contains(sItem) && !docReserved.contains(sItem))
                holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_fill_black));
            else if (docReserved.contains(sItem))
                holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_fill_yellow));
            else
                holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_outline));

            holder.textView.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if(section == 1)
            return 0;
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        if (viewType == VIEW_TYPE_HEADER) {
            layout = R.layout.slot_header;
        }else
            layout = R.layout.slot_design;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);

        }
    }

    public String getSelected() {
        if (selectedItem != -1 && selectedSection != 1) {
            String sItem = itemList.get(sectionList.get(selectedSection)).get(selectedItem);
            if (docTimings.contains(sItem)) {
                selectedItem = -1;
                ref.child(sItem).setValue("Reserved");
                notifyDataSetChanged();
                return sItem;
            }
        }
        return null;
    }

}