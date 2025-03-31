package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.models.CalendarModel;
import com.example.rp_android.models.CalendarOrganizationModel;
import com.example.rp_android.R;

import java.util.List;

/**
 * Soubor pro RecycleView, který načítá celou organizaci
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {


    private String pasteFrom;
    private String pasteTo;
    private ListAdapter adapter;
    private List<CalendarModel> dataList;
    private CalendarAdapter.OnItemClickListener listener;
    private Context context;
    private FragmentManager fragmentManager;
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

    public CalendarAdapter(List<CalendarModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;

    }


    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new CalendarAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        CalendarModel item = dataList.get(position);

        holder.dateText.setText(String.valueOf(item.getDay())+ "." + item.getMonth() + ".");
        holder.monthText.setText(item.getWeek());
        holder.vacationImageView.setImageDrawable(item.getVacationStatus());

        holder.workImageView.setImageDrawable(item.getWorkStatus());
        holder.holidayImageView.setImageDrawable(item.getHolidayStatus());
            adapter = new ListAdapter(item.getCalendarOrganizationModel(),
                    (ListAdapter.OnItemClickListener) listener,
                    fragmentManager,
                    context
            );

        holder.childListView.setLayoutManager(new LinearLayoutManager(context));
        holder.childListView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateItem(int position, String from, String to) {
        pasteFrom = from;
        pasteTo = to;
        notifyItemChanged(position); // Refresh adaptéru
    }
    public void updateFromPicker(int position, String time, int month, int year, int id) {

        if (position >= 0 && position < dataList.size()) {

        }
        notifyItemChanged(position); // Refresh adaptéru

    }
    public void updateToPicker(int position, String time, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);  // Refresh adaptéru
        }
        notifyItemChanged(position);// Refresh adaptéru

    }
    public void copyTime(int position, String from, String to, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);  // Refresh adaptéru
        }
        notifyItemChanged(position); // Refresh adaptéru

    }
    public void allTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {


            notifyItemChanged(position);  // Refresh adaptéru
        }
        notifyItemChanged(position);
// Refresh only this item
    }
    public void updateAPI(int position, String from, String to ) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void deleteTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);  // Refresh adaptéru
        }
        notifyItemChanged(position); // Refresh adaptéru

    }

    public void vacationSet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableVacation = ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24);
            if (drawableVacation != null) {
                drawableVacation.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setVacationStatus(drawableVacation);
                notifyItemChanged(position);
            }// Refresh adaptéru
        }

    }
    public void holidaySet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableHoliday = ContextCompat.getDrawable(context, R.drawable.baseline_flag_24);
            if (drawableHoliday != null) {
                drawableHoliday.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setHolidayStatus(drawableHoliday);
            }

            notifyItemChanged(position);
        }

    }

    public void setORGData(int position, List<CalendarOrganizationModel> ORG){
        if (position >= 0 && position < dataList.size()) {

                dataList.get(position).setCalendarOrganizationModel(ORG);

                notifyItemChanged(position);



        }

    }

    public void workSet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableWork = ContextCompat.getDrawable(context, R.drawable.baseline_inbox_24);
            if (drawableWork != null) {
                drawableWork.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setWorkStatus(drawableWork);
            }
            notifyItemChanged(position); // Refresh adaptéru
        }

    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView monthText;
        TextView weekText;
        TextView fromTextView;
        TextView toTextView;

        ImageView vacationImageView;
        ImageView workImageView;
        ImageView holidayImageView;



        Button fromButton;
        Button toButton;
        Button deleteButton;
        Button copyButton;
        Button pasteButton;
        Button allButton;

        RecyclerView childListView;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.optionsDateText);
            monthText = itemView.findViewById(R.id.optionsMonthText);
            fromButton = itemView.findViewById(R.id.fromButton);
            toButton = itemView.findViewById(R.id.toButton);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            copyButton =  itemView.findViewById(R.id.copyButton);
            pasteButton =  itemView.findViewById(R.id.pasteButton);
            allButton =  itemView.findViewById(R.id.allButton);
            vacationImageView = itemView.findViewById(R.id.vacationImageView);
            workImageView = itemView.findViewById(R.id.workImageView);
            holidayImageView = itemView.findViewById(R.id.holidayImageView);
            childListView = itemView.findViewById(R.id.childListView);
        }
    }
}