package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.rp_android.models.CalendarModel3;
import com.example.rp_android.models.MyShiftsModel;
import com.example.rp_android.models.OffersModel;

import java.util.List;

/**
 * Soubor slouží pro načténí směn přihlášeného uživatele v RecycleView
 */
public class CalendarMyShiftsAdapter extends RecyclerView.Adapter<CalendarMyShiftsAdapter.ViewHolder> {


    private MyShiftsAdapter adapter;
    private List<CalendarModel3> dataList;
    private CalendarMyShiftsAdapter.OnItemClickListener listener;
    private Context context;
    private FragmentManager fragmentManager;
    public interface OnItemClickListener {


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public CalendarMyShiftsAdapter(List<CalendarModel3> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;

    }


    @NonNull
    @Override
    public CalendarMyShiftsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new CalendarMyShiftsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalendarMyShiftsAdapter.ViewHolder holder, int position) {
        CalendarModel3 item = dataList.get(position);


        holder.dateText.setText(String.valueOf(item.getDay())+ "." + item.getMonth() + ".");
        holder.monthText.setText(item.getWeek());
        holder.vacationImageView.setImageDrawable(item.getVacationStatus());

        holder.workImageView.setImageDrawable(item.getWorkStatus());
        holder.holidayImageView.setImageDrawable(item.getHolidayStatus());

        adapter = new MyShiftsAdapter(item.getOffersModel(),
                (MyShiftsAdapter.OnItemClickListener) listener,
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


    public void vacationSet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableVacation = ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24);
            if (drawableVacation != null) {
                drawableVacation.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setVacationStatus(drawableVacation);
                notifyItemChanged(position);
            }
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

    public void  setMyList(int position, List<MyShiftsModel> Shifts){
        if (position >= 0 && position < dataList.size()) {

            dataList.get(position).setOffersModel(Shifts);
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
            notifyItemChanged(position);  // Refresh only this item
        }

    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView monthText;
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
