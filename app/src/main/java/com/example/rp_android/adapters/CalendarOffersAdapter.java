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

import com.example.rp_android.R;
import com.example.rp_android.models.CalendarModel2;
import com.example.rp_android.models.OffersModel;

import java.util.List;

public class CalendarOffersAdapter extends RecyclerView.Adapter<CalendarOffersAdapter.ViewHolder> {
    private List<Integer> dayList;
    private List<String> monthList;
    private List<String> weekList;

    private List<String> monthNameList;



    SharedPreferences sharedPreferences;



    private List<Integer> filteredDay;
    private List<String> filteredMonth;
    private List<String> filteredWeek;

    private List<String> filteredMonthName;
    private String pasteFrom;
    private String pasteTo;
    private String timeSelectedFrom;
    private OfferAdapter adapter;
    private List<CalendarModel2> dataList;
    private CalendarOffersAdapter.OnItemClickListener listener;
    private CalendarOffersAdapter.OnItemClickListener listener_paste;
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

    public CalendarOffersAdapter(List<CalendarModel2> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;

    }


    @NonNull
    @Override
    public CalendarOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new CalendarOffersAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalendarOffersAdapter.ViewHolder holder, int position) {
        CalendarModel2 item = dataList.get(position);


        holder.dateText.setText(String.valueOf(item.getDay())+ "." + item.getMonth() + ".");
        holder.monthText.setText(item.getWeek());
        holder.vacationImageView.setImageDrawable(item.getVacationStatus());

        holder.workImageView.setImageDrawable(item.getWorkStatus());
        holder.holidayImageView.setImageDrawable(item.getHolidayStatus());

        adapter = new OfferAdapter(item.getOffersModel(),
                (OfferAdapter.OnItemClickListener) listener,
                fragmentManager,
                context,
                position
        );

        holder.childListView.setLayoutManager(new LinearLayoutManager(context));
        holder.childListView.setAdapter(adapter);
        Log.e("frf" , String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void vacationSet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableVacation = ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24);
            if (drawableVacation != null) {
                drawableVacation.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setVacationStatus(drawableVacation);
                notifyItemChanged(position);
            }// Refresh only this item
        }
        //notifyItemChanged(position); // Refresh only this item

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

    public void setOffersData(int position, List<OffersModel> Offers){
        if (position >= 0 && position < dataList.size()) {

            dataList.get(position).setOffersModel(Offers);
            notifyItemChanged(position);
            //}


        }
        //notifyItemChanged(position);

    }

    public void workSet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableWork = ContextCompat.getDrawable(context, R.drawable.baseline_inbox_24);
            if (drawableWork != null) {
                drawableWork.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setWorkStatus(drawableWork);
            }
            notifyItemChanged(position);  // Refresh only this item
        }

    }
    public void changeWaiting(int position, List<OffersModel> dataListOffer, int parent) {
        //adapter.changeWaiting(position);
        if (position >= 0 && position < dataList.size()) {
            dataListOffer.get(position).setButtonColor("#ffc107");
            dataListOffer.get(position).setButtonText("Waiting");
            dataListOffer.get(position).setStatus(1);
            dataList.get(parent).setOffersModel(dataListOffer);
            Log.e("AdapterError", "Adapter is null or position is invalid");
            notifyItemChanged(parent); // Refresh only this item
        }

    }
    public void changeDelete(int position, List<OffersModel> dataListOffer, int parent) {
        //adapter.changeWaiting(position);
        if (position >= 0 && position < dataList.size()) {
            dataListOffer.get(position).setButtonColor("#0d6efd");
            dataListOffer.get(position).setButtonText("Request");
            dataListOffer.get(position).setStatus(0);
            dataList.get(parent).setOffersModel(dataListOffer);
            Log.e("AdapterError", "Adapter is null or position is invalid");
            notifyItemChanged(parent); // Refresh only this item
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



        TextView monthNameText;
        Button fromButton;
        Button toButton;
        Button deleteButton;
        Button copyButton;
        Button pasteButton;
        Button allButton;

        RecyclerView childListView;





        ImageView profileImage;



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
            // weekText = itemView.findViewById(R.id.optionsWeekText);
        }
    }
}
