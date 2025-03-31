package com.example.rp_android.ui.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.adapters.SpinnerAdapter;
import com.example.rp_android.models.CalendarModel;
import com.example.rp_android.models.CalendarOrganizationModel;
import com.example.rp_android.models.ORGPostModel;
import com.example.rp_android.models.OptionsPostModel;
import com.example.rp_android.models.SpinnerModel;
import com.example.rp_android.R;
import com.example.rp_android.adapters.CalendarAdapter;
import com.example.rp_android.adapters.ListAdapter;
import com.example.rp_android.databinding.FragmentCalendarBinding;
import com.example.rp_android.resources.HolidayList;
import com.example.rp_android.models.DetailModel;
import com.example.rp_android.ui.options.OptionsViewModel;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemClickListener, ListAdapter.OnItemClickListener {
    private String pasteFrom;
    private int pastePosition;

    private String pasteTo;

    private int selectedSideId;


    SharedPreferences sharedPreferences;


    private List<Integer> dayList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();

    private List<String> weekList = new ArrayList<>();
    private List<String> monthNameList = new ArrayList<>();
    private OptionsViewModel mViewModel;
    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private TextView monthText;
    private TextView yearText;
    private List<CalendarModel> calendarModel;
    private int userId;

    private int lastDay;
    private int currentMonth;
    private int currentYear;
    private int firstLoad = 0;

    private Spinner spinner;
    private Spinner spinnerSide;
    private Calendar calendarglobal;


    private List<String> selectId = new ArrayList<>();

    private List<String> selectName = new ArrayList<>();
    private List<SpinnerModel> spinnerModel = new ArrayList<>();

    private List<String> selecSideId = new ArrayList<>();

    private List<String> selectSideName = new ArrayList<>();
    private List<SpinnerModel> spinnerSideModel = new ArrayList<>();

    private List<CalendarOrganizationModel> ORGList = new ArrayList<>();


    private TextView selectedTimeTextView;
    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel optionsViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recycleViewCalendar;
        monthText = binding.monthText;
        yearText = binding.yearText;
        spinner = binding.mainObjectSpinner;
        spinnerSide = binding.sideObjectSpinner;

        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);

        calendarglobal = Calendar.getInstance();

        currnetLoad(calendarglobal);

        Button nextButton = (Button) binding.nextButton;
        nextButton.setOnClickListener(v -> {
            calendarglobal.set(Calendar.MONTH, calendarglobal.get(Calendar.MONTH) + 1);
            reloadCalendar(calendarglobal, 1);

        });

        Button previousButton = (Button) binding.previousButton;
        previousButton.setOnClickListener(v -> {
            calendarglobal.set(Calendar.MONTH, calendarglobal.get(Calendar.MONTH) - 1);
            reloadCalendar(calendarglobal, 1);


        });
        spinnerLoader();
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
        calendarModel = new ArrayList<>();
        lastDay = getLastDayOfMonth(currentYear, currentMonth);
        List<String> holidayList = HolidayList.DATES;
        int passedId = 0;
        for (int i = 1; i < lastDay + 1; i++) {
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
            List<CalendarOrganizationModel> ORGListEmpty = new ArrayList<>();


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                ORGList.add(new CalendarOrganizationModel());

                calendarModel.add(new CalendarModel(i, String.valueOf(currentMonth + 1), dayFormat.format(formmater),  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24),ORGListEmpty));


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        adapter = new CalendarAdapter(calendarModel,
                this,
                getParentFragmentManager(),
                requireContext()
        );

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

            if(holidayList.contains(rawMonth + "-" + rawDay)){
                adapter.holidaySet((i-1));
            }
            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;
            loadWorkday((i - 1), fullDate);
        }
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
            int formattedHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
            String selectedTime = String.format("%02d:%02d", hour, minute);
            adapter.updateFromPicker(position, selectedTime, currentMonth, currentYear, userId);

        });
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

        });
    }

    @Override
    public void deletePicker(int position) {
        adapter.deleteTime(position, currentMonth, currentYear, userId);

    }

    @Override
    public void copyTime(String from, String to) {
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

    private void loadWorkday(int position, String date) {
        Retrofit retrofitSecond = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getOption = retrofitSecond.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        OptionsPostModel modelOptions = new OptionsPostModel(idUser, date);

        Call<ResponseBody> callOptions = getOption.postWorkDay("Bearer " +sharedPreferences.getString("plainToken", "") , modelOptions);
        callOptions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", response.toString());

                if (response.isSuccessful()) {

                    adapter.workSet(position);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                adapter.updateAPI(position, "--:--", "--:--");


            }
        });


    }

    public void spinnerLoader(){
        List<String> spinnerItems = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getObjects = retrofit.create(ApiDataService.class);
        DetailModel model = new DetailModel("ds");

        Call<ResponseBody> callObject = getObjects.postObjects("Bearer "+sharedPreferences.getString("plainToken", ""), model);
        callObject.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        Log.d("API_RESPONSE", "Response: " + res);

                        // Convert to JSON
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.has("data")) {
                            Object dataObject = jsonObject.get("data");

                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;
                                Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                            }
                            else if (dataObject instanceof JSONArray) {
                                JSONArray jsonArrayData = (JSONArray) dataObject;
                                Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " items");


                                selectId = new ArrayList<>();
                                selectName = new ArrayList<>();
                                spinnerModel = new ArrayList<>();

                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    selectName.add(item.getString("name"));
                                    spinnerItems.add(item.getString("name"));
                                    String icon = (item.getString("icon").substring(3)).replaceAll("-", "_");;
                                    int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
                                    Drawable draw;
                                    if (resourceId != 0) {
                                        draw = ContextCompat.getDrawable(requireContext(), resourceId);
                                    }else{
                                        draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                    }
                                    spinnerModel.add(new SpinnerModel(item.getInt("id"), item.getString("name"), draw));



                                }


                                SpinnerAdapter adapterSpin = new SpinnerAdapter(requireContext(), spinnerModel);
                                spinner.setAdapter(adapterSpin);


                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        SpinnerModel selectedItem = (SpinnerModel) parent.getItemAtPosition(position);

                                        int selectedId = selectedItem.getId();
                                        spinnerSideLoader(selectedId);


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Do nothing
                                    }
                                });

                            }
                        }

                    } catch (IOException e) {
                        Log.e("IO_ERROR", "Error reading response: " + e.getMessage());
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_FAILURE", "Request failed: " + throwable.getMessage());
            }
        });

    }
    public void spinnerSideLoader(Integer objectId) {
        List<String> spinnerItems = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getSideObjects = retrofit.create(ApiDataService.class);
        DetailModel sideModel = new DetailModel(String.valueOf(objectId));

        Call<ResponseBody> callSideObject = getSideObjects.postSideObjects("Bearer "+sharedPreferences.getString("plainToken", ""), sideModel);
        callSideObject.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.has("data")) {
                            Object dataObject = jsonObject.get("data");

                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;
                                Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                            }
                            else if (dataObject instanceof JSONArray) {
                                JSONArray jsonArrayData = (JSONArray) dataObject;
                                Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " items");


                                selecSideId = new ArrayList<>();
                                selectSideName = new ArrayList<>();
                                spinnerSideModel = new ArrayList<>();

                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);

                                    String icon = (item.getString("icon").substring(3)).replaceAll("-", "_");;
                                    int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
                                    Drawable draw;
                                    if (resourceId != 0) {
                                        draw = ContextCompat.getDrawable(requireContext(), resourceId);
                                    }else{
                                        draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                    }
                                    spinnerSideModel.add(new SpinnerModel(item.getInt("id"), item.getString("name"), draw));

                                }

                                SpinnerAdapter adapterSpinSide = new SpinnerAdapter(requireContext(), spinnerSideModel);
                                spinnerSide.setAdapter(adapterSpinSide);
                                spinnerSide.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        SpinnerModel selectedItem = (SpinnerModel) parent.getItemAtPosition(position);

                                         selectedSideId = selectedItem.getId();
                                        if(firstLoad > 0 ){
                                            calendarglobal.set(Calendar.MONTH, calendarglobal.get(Calendar.MONTH));
                                            reloadCalendar(calendarglobal, 0);
                                        }
                                        firstLoad++;

                                        List<String> holidayList = HolidayList.DATES;
                                        for (int i = 1; i < lastDay + 1; i++) {
                                            List<CalendarOrganizationModel> ORGListEmpty = new ArrayList<>();
                                            adapter.setORGData(position, ORGListEmpty);

                                        }
                                        for (int i = 1; i < lastDay + 1; i++) {
                                            String rawDay = String.valueOf(i);
                                            if (i < 10) {
                                                rawDay = "0" + String.valueOf(i);
                                            }
                                            String rawMonth = String.valueOf(currentMonth + 1);
                                            if (currentMonth + 1 < 10) {
                                                rawMonth = "0" + String.valueOf(currentMonth + 1);
                                            }

                                            if (holidayList.contains(rawMonth + "-" + rawDay)) {
                                                adapter.holidaySet((i - 1));
                                            }
                                            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;
                                            loadORG((i-1), fullDate, selectedSideId);
                                        }


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                            }
                        }

                    } catch (IOException e) {
                        Log.e("IO_ERROR", "Error reading response: " + e.getMessage());
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_FAILURE", "Request failed: " + throwable.getMessage());
            }
        });
    }
    private void loadORG(int position,String date, int object ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getORG = retrofit.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        ORGPostModel modelORG = new ORGPostModel(date, object);

        Call<ResponseBody> callORG = getORG.postOrgCalendar("Bearer "+sharedPreferences.getString("plainToken", ""), modelORG);
        callORG.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", response.toString());
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        Object dataObject = jsonObject.get("data");

                        if (dataObject instanceof JSONObject) {
                            JSONObject jsonObjectData = (JSONObject) dataObject;
                            Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                        } else if (dataObject instanceof JSONArray) {
                            JSONArray jsonArrayData = (JSONArray) dataObject;
                            Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " elements. ");
                            ORGList = new ArrayList<>();

                            if (jsonArrayData.length() != 0) {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    ORGList.add(new CalendarOrganizationModel(item.getInt("id"), item.getString("from"), item.getString("to"), item.getString("color"), item.getString("shift"), item.getString("name"), ConnectionFile.returnURLRaw()+item.getString("image")));
                                    Log.e("ppppppppp", ORGList.toString());
                                }
                                adapter.setORGData(position, ORGList);

                            }


                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {


            }
        });


    }
    private void reloadCalendar(Calendar calendar, int refresh ) {
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
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);

        monthText.setText(fullMonthFormat.format(calendar.getTime()));
        yearText.setText(fullYearFormat.format(calendar.getTime()));
        calendarModel = new ArrayList<>();
        lastDay = getLastDayOfMonth(currentYear, currentMonth);
        List<String> holidayList = HolidayList.DATES;
        for (int i = 1; i < lastDay + 1; i++) {
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
            List<CalendarOrganizationModel> ORGListEmpty = new ArrayList<>();


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                ORGList.add(new CalendarOrganizationModel());

                calendarModel.add(new CalendarModel(i, String.valueOf(currentMonth + 1), dayFormat.format(formmater),  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24),ORGListEmpty));


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        adapter = new CalendarAdapter(calendarModel,
                this,
                getParentFragmentManager(),
                requireContext()
        );

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

            if(holidayList.contains(rawMonth + "-" + rawDay)){
                adapter.holidaySet((i-1));
            }
            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;
            loadWorkday((i - 1), fullDate);
            if(refresh == 1){
                loadORG((i-1), fullDate, selectedSideId);
            }
        }

    }




    }