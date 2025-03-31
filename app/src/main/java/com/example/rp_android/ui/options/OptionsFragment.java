package com.example.rp_android.ui.options;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.models.OptionsModel;
import com.example.rp_android.models.OptionsPostModel;
import com.example.rp_android.R;
import com.example.rp_android.adapters.CalendarOptionsAdapter;
import com.example.rp_android.databinding.FragmentOptionsBinding;
import com.example.rp_android.resources.HolidayList;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OptionsFragment extends Fragment implements CalendarOptionsAdapter.OnItemClickListener {
    private String pasteFrom;
    private int pastePosition;

    private String pasteTo;

    @Override
    public void onItemClick(String from, String to, int position) {
        pasteFrom = from;
        pasteTo = to;
        pastePosition = position;

        //Toast.makeText(getContext(), "Clicked: " + data, Toast.LENGTH_SHORT).show();
    }

    SharedPreferences sharedPreferences;


    private List<Integer> dayList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();

    private List<String> weekList = new ArrayList<>();
    private List<String> monthNameList = new ArrayList<>();
    private OptionsViewModel mViewModel;
    private RecyclerView recyclerView;
    private CalendarOptionsAdapter adapter;
    private TextView monthText;
    private TextView yearText;
    private List<OptionsModel> optionsModel;
    private HashMap<Integer, String> dateMap = new HashMap<Integer, String>();
    private int userId;

    private int lastDay;
    private int currentMonth;
    private int currentYear;

    /*public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OptionsViewModel.class);
        // TODO: Use the ViewModel
    }*/
    private TextView selectedTimeTextView;
    private FragmentOptionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OptionsViewModel optionsViewModel =
                new ViewModelProvider(this).get(OptionsViewModel.class);

        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recycleViewCalendar;
        monthText = binding.monthText;
        yearText = binding.yearText;

        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);
        /*Button selectTimeButton = binding.selectTimeButton;
        selectedTimeTextView = binding.selectedTimeTextView;*/

        //selectTimeButton.setOnClickListener(v -> showTimePicker());
        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Calendar calendar = Calendar.getInstance();

        currnetLoad(calendar);

        Button nextButton = (Button) binding.nextButton;
        nextButton.setOnClickListener(v -> {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            currnetLoad(calendar);
// Month index starts from 0
            /*Log.e("next-- ", String.valueOf(calendar.get(Calendar.YEAR)));
            Log.e("next-- ", String.valueOf(calendar.get(Calendar.MONTH)));*/

        });

        Button previousButton = (Button) binding.previousButton;
        previousButton.setOnClickListener(v -> {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            currnetLoad(calendar);

            //Log.e("next ", "nnnnn");

        });
        return root;
    }

    private void showTimePicker() {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // Use TimeFormat.CLOCK_24H for 24-hour format
                .setHour(10) // Default hour
                .setMinute(30) // Default minute
                .setTitleText("Select Time")
                .build();

        picker.show(getParentFragmentManager(), "MATERIAL_TIME_PICKER");

        picker.addOnPositiveButtonClickListener(dialog -> {
            int hour = picker.getHour();
            int minute = picker.getMinute();
            String amPm = hour >= 12 ? "PM" : "AM";

            // Convert to 12-hour format
            int formattedHour = (hour == 0 || hour == 12) ? 12 : hour % 12;

            String selectedTime = String.format("%02d:%02d %s", formattedHour, minute, amPm);
            selectedTimeTextView.setText("Selected Time: " + selectedTime);
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void currnetLoad(Calendar calendar) {
        dayList = new ArrayList<>();
        monthList = new ArrayList<>();
        weekList = new ArrayList<>();
        monthNameList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        DateFormat monthFormat = new SimpleDateFormat("MMM");
        @SuppressLint("SimpleDateFormat")
        DateFormat fullMonthFormat = new SimpleDateFormat("MMMM");
        @SuppressLint("SimpleDateFormat")
        DateFormat fullYearFormat = new SimpleDateFormat("yyyy");
        @SuppressLint("SimpleDateFormat")
        DateFormat dayFormat = new SimpleDateFormat("EEE");
        Date date = new Date();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);

        monthText.setText(fullMonthFormat.format(calendar.getTime()));
        yearText.setText(fullYearFormat.format(calendar.getTime()));
        // Log.e("Month", String.valueOf(Calendar.getInstance().getActualMaximum(month)));
        optionsModel = new ArrayList<>();
        lastDay = getLastDayOfMonth(currentYear, currentMonth);
        Log.e("qqqq", monthFormat.format(calendar.getTime()));
        //Log.e("qqqq" , year.format(calendar.getTime()));
        List<String> holidayList = HolidayList.DATES;
        //Log.d("ListValues", "Size: " + holidayList.size());
        int passedId = 0;
        //int lastDay = getLastDayOfMonth(2024, 3); // Output: 31
        for (int i = 1; i < lastDay + 1; i++) {
            //JSONObject item = jsonArrayData.getJSONObject(i);
            dayList.add(i);
            monthList.add(String.valueOf(currentMonth + 1));
            weekList.add(monthFormat.format(i));
            String rawDay = String.valueOf(i);
            if (i < 10) {
                rawDay = "0" + String.valueOf(i);
            }
            String rawMonth = String.valueOf(i);
            if (currentMonth + 1 < 10) {
                rawMonth = "0" + String.valueOf(currentMonth + 1);
            }
            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;

            dateMap.put(i, fullDate);


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                monthNameList.add(dayFormat.format(formmater) + ".");


                optionsModel.add(new OptionsModel(i, String.valueOf(currentMonth + 1), monthFormat.format(i), dayFormat.format(formmater), "--:--", "--:--",ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24)));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            //monthNameList.add(dayFormat.format(fullDate));
        }
        adapter = new CalendarOptionsAdapter(optionsModel,
                this,
                getParentFragmentManager(),
                requireContext()
        );

        //loadOptions("2025-03-05");
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        for (int i = 1; i < lastDay + 1; i++) {
            String rawDay = String.valueOf(i);
            if (i < 10) {
                rawDay = "0" + String.valueOf(i);
            }
            String rawMonth = String.valueOf(currentMonth+1);
            if (currentMonth + 1 < 10) {
                rawMonth = "0" + String.valueOf(currentMonth + 1);
            }
            Log.e("wwwwwwwwwwwwwwwwwwwwwwwwww" ,(rawMonth + "-" + rawDay) );

            if(holidayList.contains(rawMonth + "-" + rawDay)){
                adapter.holidaySet((i-1));
                Log.e("hol" ,(rawMonth + "-" + rawDay) );
            }
            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;
            loadOptions((i - 1), fullDate);
            loadWorkday((i - 1), fullDate);
        }
        //searchBar.setOnClickListener(v -> searchView.show());
    }

    public int getLastDayOfMonth(int year, int monthNumber) {
        Calendar calendarExtra = Calendar.getInstance();
        calendarExtra.set(Calendar.YEAR, year);
        calendarExtra.set(Calendar.MONTH, monthNumber); // Month index starts from 0
        return calendarExtra.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void openPicker(String id) {


    }

    private void pasteAction(String from, String to) {

    }

    @Override
    public void pasteClick(int position) {
        adapter.updateItem(position, pasteFrom, pasteTo);
    }

    @Override
    public void openFromPicker(int position) {
        FragmentManager fragmentManager = getParentFragmentManager();
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(10)
                .setMinute(30)
                .setTitleText("Select Time")
                .build();

        picker.show(fragmentManager, "MATERIAL_TIME_PICKER");

        picker.addOnPositiveButtonClickListener(dialog -> {
            int hour = picker.getHour();
            int minute = picker.getMinute();
            //String amPm = hour >= 23 ;
            int formattedHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
            String selectedTime = String.format("%02d:%02d", hour, minute);
            //String selectedTime = String.format("%02d:%02d %s", formattedHour, minute, amPm);
            //item.setTime(selectedTime); // Update the correct item in the list
            //holder.fromTextView.setText(selectedTime);
            adapter.updateFromPicker(position, selectedTime, currentMonth, currentYear, userId);

        });
        Log.e("position", String.valueOf(position));
    }

    @Override
    public void openToPicker(int position) {
        FragmentManager fragmentManager = getParentFragmentManager();
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(10)
                .setMinute(30)
                .setTitleText("Select Time")
                .build();

        picker.show(fragmentManager, "MATERIAL_TIME_PICKER");

        picker.addOnPositiveButtonClickListener(dialog -> {
            int hour = picker.getHour();
            int minute = picker.getMinute();

            String selectedTime = String.format("%02d:%02d", hour, minute);

            adapter.updateToPicker(position, selectedTime, currentMonth, currentYear, userId);
            //adapter.saveData(position);

            //notifyItemChanged(position); // Refresh only this item
        });
        Log.e("position", String.valueOf(position));
    }

    @Override
    public void deletePicker(int position) {
        adapter.deleteTime(position, currentMonth, currentYear, userId);

    }

    @Override
    public void copyTime(String from, String to) {
        Log.e("dsads", from);
        pasteFrom = from;
        pasteTo = to;
    }

    @Override
    public void pasteTime(int position) {
        adapter.copyTime(position, pasteFrom, pasteTo, currentMonth, currentYear, userId);

    }

    @Override
    public void allDay(int position) {
        adapter.allTime(position, currentMonth, currentYear, userId);
    }

    private void loadOptions(int position, String date) {
        Retrofit retrofitSecond = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getOptions = retrofitSecond.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        OptionsPostModel modelOPtions = new OptionsPostModel(idUser, date);

        Call<ResponseBody> callOptions = getOptions.postOptions("Bearer "+ sharedPreferences.getString("plainToken", ""), modelOPtions);
        callOptions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e("res", response.toString());

                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //Log.e("body", "-------------");
                    //Log.e("body--ydob", response.message());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String fromTime = jsonObject.getString("from");
                        String toTime = jsonObject.getString("to");
                        adapter.updateAPI(position, fromTime, toTime);


                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                adapter.updateAPI(position, "--:--", "--:--");


            }
        });


    }
    private void loadWorkday(int position, String date) {
        Retrofit retrofitSecond = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getOption = retrofitSecond.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        OptionsPostModel modelOptions = new OptionsPostModel(idUser, date);

        Call<ResponseBody> callOptions = getOption.postWorkDay("Bearer "+ sharedPreferences.getString("plainToken", ""), modelOptions);
        callOptions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", response.toString());

                if (response.isSuccessful()) {
                    /*String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //Log.e("body", "-------------");
                    //Log.e("body--ydob", response.message());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String fromTime = jsonObject.getString("from");
                        String toTime = jsonObject.getString("to");
                        adapter.updateAPI(position, fromTime, toTime);


                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }*/
                    adapter.workSet(position);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                adapter.updateAPI(position, "--:--", "--:--");


            }
        });


    }


}