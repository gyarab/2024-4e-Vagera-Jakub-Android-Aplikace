package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.models.CalendarOrganizationModel;
import com.example.rp_android.R;

import java.util.List;

/**
 * Soubor nacita jednotlive uzivatele v kalendari v konkretnim dni
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements CalendarAdapter.OnItemClickListener {

    private String pasteFrom;
    private String pasteTo;
    private List<CalendarOrganizationModel> dataList;
    private ListAdapter.OnItemClickListener listener;
    private CalendarAdapter.OnItemClickListener listener_paste;
    private Context context;
    private FragmentManager fragmentManager;

    @Override
    public void pasteClick(int position) {

    }

    @Override
    public void openFromPicker(int position) {

    }

    @Override
    public void openToPicker(int position) {

    }

    @Override
    public void deletePicker(int position) {

    }

    @Override
    public void copyTime(String from, String to) {

    }

    @Override
    public void pasteTime(int position) {

    }

    @Override
    public void allDay(int position) {

    }

    public interface OnItemClickListener {

        void pasteClick(int position);
        void openFromPicker(int position);
        void openToPicker(int position);
        void deletePicker(int position);
        void copyTime(String from, String to);

        void pasteTime(int position);
        void allDay(int position);

        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public ListAdapter(List<CalendarOrganizationModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;

    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        CalendarOrganizationModel item = dataList.get(position);
        holder.fromText.setText(item.getFrom());
        holder.toText.setText(item.getTo());
        holder.nameText.setText(item.getName());
        holder.shiftText.setText(item.getShift());
        holder.sideImageView.setBackgroundColor(Color.parseColor(item.getColor()));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageURL())
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.profileImage);

    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }

    public void updateItem(int position, String from, String to) {
        pasteFrom = from;
        pasteTo = to;
        notifyItemChanged(position); // Refresh only this item
    }
    public void updateFromPicker(int position, String time, int month, int year, int id) {

        if (position >= 0 && position < dataList.size()) {

        }
        notifyItemChanged(position); // Refresh only this item

    }
    public void updateToPicker(int position, String time, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);  // Refresh only this item
        }
        notifyItemChanged(position); // Refresh only this item

    }
    public void copyTime(int position, String from, String to, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void allTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {


            notifyItemChanged(position);
        }
        notifyItemChanged(position);
    }
    public void updateAPI(int position, String from, String to ) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void deleteTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromText;
        TextView toText;
        TextView nameText;
        TextView shiftText;



        ImageView profileImage;
        ImageView sideImageView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            nameText = itemView.findViewById(R.id.nameText);
            shiftText = itemView.findViewById(R.id.shiftText);
            profileImage = itemView.findViewById(R.id.profileImage);
            sideImageView = itemView.findViewById(R.id.sideImageView);

        }
    }

}