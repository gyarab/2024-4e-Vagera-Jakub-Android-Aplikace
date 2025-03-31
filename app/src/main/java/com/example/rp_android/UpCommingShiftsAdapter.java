package com.example.rp_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UpCommingShiftsAdapter extends RecyclerView.Adapter<UpCommingShiftsAdapter.ViewHolder>{

    private List<String> colorList;
    private List<String> objectList;
    private List<String> fromList;

    private List<String> toList;
    private List<String> shiftList;



    private List<String> filteredList;
    private List<String> filteredEmail;
    private List<String> filteredImgURL;

    private List<String> filteredId;

    private UpCommingShiftsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String id);
        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public UpCommingShiftsAdapter(List<String> colorList, List<String> objectList, List<String> fromList, List<String> toList, List<String> shiftList) {
        this.colorList = new ArrayList<>(colorList);
        this.objectList =  new ArrayList<>(objectList);
        this.fromList =  new ArrayList<>(fromList);
        this.toList =  new ArrayList<>(toList);
        this.shiftList = new ArrayList<>(shiftList);
        //this.listener = listener;
    }
    @Override
    public int getItemViewType(int position) {
        // Example: Check some condition in your data
        if (fromList.get(position) == "------") {
            return 1; // Special layout
        } else {
            return 0; // Default layout
        }
    }
    public void filter(String query) {
       /* filteredList.clear();
        filteredEmail.clear();
        filteredImgURL.clear();
        filteredId.clear();
        if (query.isEmpty()) {
            filteredList.addAll(colorList);
            filteredEmail.addAll(objectList);
            filteredId.addAll(toList);
        } else {
            for (String item : colorList) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();*/
    }

    @NonNull
    @Override
    public UpCommingShiftsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcomming_item, parent, false);*/
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcomming_empty_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcomming_item, parent, false);
        }
        return new UpCommingShiftsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpCommingShiftsAdapter.ViewHolder holder, int position) {
        holder.shiftObjectText.setText(objectList.get(position) + " - " + shiftList.get(position) );
        holder.fromToText.setText(fromList.get(position).substring(0, 5) + " - "+ toList.get(position).substring(0, 5));
        holder.sideColor.setBackgroundColor(Color.parseColor(colorList.get(position)));

        /*String selectID = filteredId.get(position);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(selectID));
        //Log.e("---" , filteredImgURL.get(position));
        Glide.with(holder.itemView.getContext())
                .load(filteredImgURL.get(position))
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.profileImage);*/

    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shiftObjectText;
        TextView fromToText;
        ImageView sideColor;



        public ViewHolder(View itemView) {
            super(itemView);
            shiftObjectText = itemView.findViewById(R.id.shiftObjectText);
            fromToText = itemView.findViewById(R.id.fromToText);
            sideColor = itemView.findViewById(R.id.sideColor);
        }
    }
    private void openNewFragment(String item) {

    }
}
