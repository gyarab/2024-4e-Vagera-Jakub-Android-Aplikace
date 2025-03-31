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
//import androidx.core.content.ContextCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.models.OptionsModel;
import com.example.rp_android.models.OptionsSaveModel;
import com.example.rp_android.R;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Soubor slouzi pro nacteni casovzch moznosti pro brigadnika
 */
public class CalendarOptionsAdapter extends RecyclerView.Adapter<CalendarOptionsAdapter.ViewHolder> {
    private List<Integer> dayList;
    private List<String> monthList;
    private List<String> weekList;

    private List<String> monthNameList;




    private List<Integer> filteredDay;
    private List<String> filteredMonth;
    private List<String> filteredWeek;

    private List<String> filteredMonthName;
    private String pasteFrom;
    private String pasteTo;
    private String timeSelectedFrom;

    private List<OptionsModel> dataList;
    private CalendarOptionsAdapter.OnItemClickListener listener;
    private CalendarOptionsAdapter.OnItemClickListener listener_paste;
    private Context context;
    private FragmentManager fragmentManager;
    public interface OnItemClickListener {

         void onItemClick(String from, String to, int position);
         void pasteClick(int position);
        void openFromPicker(int position);
        void openToPicker(int position);
        void deletePicker(int position);
        void copyTime(String from, String to);

        void pasteTime(int position);
        void allDay(int position);





        //void pasteClick(String from, String to);

            /*Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment

            Bundle bundle = new Bundle();
            bundle.putString("item_key", item); // Pass data to new fragment
            newFragment.setArguments(bundle);

            // Replace fragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, newFragment)
                    .addToBackStack(null)
                    .commit();*/


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }


    public CalendarOptionsAdapter(List<OptionsModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void filter(String query) {
        filteredDay.clear();
        filteredMonth.clear();
        filteredWeek.clear();
        filteredMonthName.clear();
        if (query.isEmpty()) {
            filteredDay.addAll(dayList);
            filteredMonth.addAll(monthList);
            filteredWeek.addAll(weekList);
            filteredMonthName.addAll(monthNameList);
        } else {

        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarOptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_option_day_item, parent, false);
        return new CalendarOptionsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalendarOptionsAdapter.ViewHolder holder, int position) {
        OptionsModel item = dataList.get(position);

        holder.dateText.setText(String.valueOf(item.getDay())+ "." + item.getMonth() + ".");
        holder.monthText.setText(item.getMonthName());
        holder.fromTextView.setText(item.getFromList());
        holder.toTextView.setText(item.getToList());
        holder.vacationImageView.setImageDrawable(item.getVacationStatus());

        holder.workImageView.setImageDrawable(item.getWorkStatus());

        holder.holidayImageView.setImageDrawable(item.getHolidayStatus());



        holder.fromButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.openFromPicker(position);
            }
        });
        holder.toButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.openToPicker(position);
            }
        });
        holder.fromTextView.setOnClickListener(v -> {
            if (listener != null) {
                listener.openFromPicker(position);
            }
        });
        holder.toTextView.setOnClickListener(v -> {
            if (listener != null) {
                listener.openToPicker(position);
            }
        });
        holder.copyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.copyTime(item.getFromList(), item.getToList());
            }
        });
        holder.pasteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.pasteTime(position);
            }
        });
        holder.allButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.allDay(position);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.deletePicker(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateItem(int position, String from, String to) {
        pasteFrom = from;
        pasteTo = to;
        notifyItemChanged(position);
    }
    public void updateFromPicker(int position, String time, int month, int year, int id) {

        if (position >= 0 && position < dataList.size()) {
            dataList.get(position).setFromList(time);

        }
        notifyItemChanged(position);
        saveData(position, fulldate(position, month, year), id);

    }
    public void updateToPicker(int position, String time, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {
            dataList.get(position).setToList(time);

            notifyItemChanged(position);
        }
        notifyItemChanged(position);
        saveData(position, fulldate(position, month, year), id);

    }
    public void copyTime(int position, String from, String to, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {
            dataList.get(position).setFromList(from);
            dataList.get(position).setToList(to);

            notifyItemChanged(position);
        }
        notifyItemChanged(position);
        saveData(position, fulldate(position, month, year), id);

    }
    public void allTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {
            dataList.get(position).setFromList("00:00");
            dataList.get(position).setToList("00:00");

            notifyItemChanged(position);
        }
        notifyItemChanged(position);
        saveData(position, fulldate(position, month, year), id);
    }
    public void updateAPI(int position, String from, String to ) {
        if (position >= 0 && position < dataList.size()) {
            dataList.get(position).setFromList(from);
            dataList.get(position).setToList(to);
            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void deleteTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {
            dataList.get(position).setFromList("--:--");
            dataList.get(position).setToList("--:--");
            notifyItemChanged(position);
        }
        notifyItemChanged(position);
        saveData(position, fulldate(position, month, year), id);

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
    public void workSet(int position) {
        if (position >= 0 && position < dataList.size()) {
            Drawable drawableWork = ContextCompat.getDrawable(context, R.drawable.baseline_inbox_24);
            if (drawableWork != null) {
                drawableWork.setTint(ContextCompat.getColor(context, R.color.primary));
                dataList.get(position).setWorkStatus(drawableWork);
            }
            notifyItemChanged(position);
        }

    }


    public void saveData(int position, String date, int id){
        OptionsSaveModel model = new OptionsSaveModel(id, date, dataList.get(position).getFromList(), dataList.get(position).getToList() );
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService setOptions = retrofit.create(ApiDataService.class);

        Call<ResponseBody> callOptions = setOptions.postOptionsSave("Bearer ", model);
        callOptions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }
    public String fulldate(int day, int month, int year ){
        String newDay;
        if((day+1) < 10){
            newDay = "0" + (day+1);
        }else{
            newDay = String.valueOf((day+1));
        }
        String newMonth;
        if((month+1) < 10){
            newMonth = "0" + (month+1);
        }else{
            newMonth = String.valueOf((month+1));
        }
        return year + "-" + newMonth + "-" + newDay;
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
        }
    }
}
