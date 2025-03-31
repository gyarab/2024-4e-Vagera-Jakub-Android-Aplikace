package com.example.rp_android.ui.offers;

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
import com.example.rp_android.models.OfferSendModel;
import com.example.rp_android.tooltip.TooltipCommentHandler;
import com.example.rp_android.tooltip.TooltipInfoHandler;
import com.example.rp_android.adapters.CalendarOffersAdapter;
import com.example.rp_android.adapters.OfferAdapter;
import com.example.rp_android.models.CalendarModel2;
import com.example.rp_android.models.OffersModel;
import com.example.rp_android.models.OffersPostModel;
import com.example.rp_android.models.OptionsPostModel;
import com.example.rp_android.models.SpinnerModel;
import com.example.rp_android.R;
import com.example.rp_android.databinding.FragmentOffersBinding;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OffersFragment extends Fragment implements CalendarOffersAdapter.OnItemClickListener, OfferAdapter.OnItemClickListener  {
    private String pasteFrom;
    private int pastePosition;

    private String pasteTo;

    private int selectedId;
    private int firstLoad = 0;


    SharedPreferences sharedPreferences;


    private List<Integer> dayList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();

    private List<String> weekList = new ArrayList<>();
    private List<String> monthNameList = new ArrayList<>();
    private OptionsViewModel mViewModel;
    private RecyclerView recyclerView;
    private CalendarOffersAdapter adapter;
    private OfferAdapter adapterOffer;

    private TextView monthText;
    private TextView yearText;
    private List<CalendarModel2> calendarModel;
    //private HashMap<Integer, String> dateMap = new HashMap<Integer, String>();
    private int userId;

    private int lastDay;
    private int currentMonth;
    private int currentYear;
    private Button closePopup;
    private Spinner spinner;
    private Spinner spinnerSide;
    private Calendar calendarglobal;

    private View popupView;
    private List<String> selectId = new ArrayList<>();

    private List<String> selectName = new ArrayList<>();
    private List<SpinnerModel> spinnerModel = new ArrayList<>();

    private List<String> selecSideId = new ArrayList<>();

    private List<String> selectSideName = new ArrayList<>();
    private List<SpinnerModel> spinnerSideModel = new ArrayList<>();


   private HashMap<Integer, Integer> idOfferMap = new HashMap<Integer, Integer>();

    private List<OffersModel> OffersList = new ArrayList<>();
    private FragmentOffersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OffersViewModel offersViewModel =
                new ViewModelProvider(this).get(OffersViewModel.class);

        binding = FragmentOffersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recycleViewCalendar;
        monthText = binding.monthText;
        yearText = binding.yearText;
        spinner = binding.mainObjectSpinner;
        //spinnerSide = binding.sideObjectSpinner;

        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id", 0);

        calendarglobal = Calendar.getInstance();

        currnetLoad(calendarglobal);
        Button nextButton = (Button) binding.nextButton;
        nextButton.setOnClickListener(v -> {
            calendarglobal.set(Calendar.MONTH, calendarglobal.get(Calendar.MONTH) + 1);
            //currnetLoad(calendarglobal);

            reloadCalendar(calendarglobal, 1);

        });

        Button previousButton = (Button) binding.previousButton;
        previousButton.setOnClickListener(v -> {
            calendarglobal.set(Calendar.MONTH, calendarglobal.get(Calendar.MONTH) - 1);
            //currnetLoad(calendarglobal);
            reloadCalendar(calendarglobal, 1);

            //Log.e("next ", "nnnnn");

        });
        spinnerLoader();
        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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
            //selectedTimeTextView.setText("Selected Time: " + selectedTime);
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
        Log.e("qqqq", monthFormat.format(calendar.getTime()));
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
            List<OffersModel> OffersListEmpty = new ArrayList<>();


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                //monthNameList.add(dayFormat.format(formmater) + ".");
                OffersList.add(new OffersModel());

                calendarModel.add(new CalendarModel2(i, String.valueOf(currentMonth + 1), dayFormat.format(formmater),  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24),OffersListEmpty));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            //monthNameList.add(dayFormat.format(fullDate));
        }
        adapter = new CalendarOffersAdapter(calendarModel,
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
            //loadOptions((i - 1), fullDate);
            loadWorkday((i - 1), fullDate);
            //loadORG((i - 1), fullDate);
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
        //adapter.updateItem(position, pasteFrom, pasteTo);
    }

    @Override
    public void openFromPicker(int position) {

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

            //adapter.updateToPicker(position, selectedTime, currentMonth, currentYear, userId);
            //adapter.saveData(position);

            //notifyItemChanged(position); // Refresh only this item
        });
        Log.e("position", String.valueOf(position));
    }

    @Override
    public void deletePicker(int position) {
       // adapter.deleteTime(position, currentMonth, currentYear, userId);

    }

    @Override
    public void copyTime(String from, String to) {
        Log.e("dsads", from);
        pasteFrom = from;
        pasteTo = to;
    }

    @Override
    public void pasteTime(int position) {
       // adapter.copyTime(position, pasteFrom, pasteTo, currentMonth, currentYear, userId);

    }

    @Override
    public void allDay(int position) {
        //adapter.allTime(position, currentMonth, currentYear, userId);
    }

    /*private void loadOptions(int position, String date) {
        Retrofit retrofitSecond = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getOptions = retrofitSecond.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        OptionsPostModel modelOPtions = new OptionsPostModel(idUser, date);

        Call<ResponseBody> callOptions = getOptions.postOptions("Bearer ", modelOPtions);
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


    }*/
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
                //adapter.updateAPI(position, "--:--", "--:--");


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

        Call<ResponseBody> callObject = getObjects.postObjects("Bearer "+ sharedPreferences.getString("plainToken", ""), model);
        callObject.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Convert response body to string
                        String res = response.body().string();
                        Log.d("API_RESPONSE", "Response: " + res);

                        // Convert to JSON
                        JSONObject jsonObject = new JSONObject(res);

                        // Check if "data" exists
                        if (jsonObject.has("data")) {
                            Object dataObject = jsonObject.get("data");

                            // Check if data is a JSON Object
                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;
                                Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                            }
                            // Check if data is a JSON Array
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
                                    Log.e("icon", icon);
                                    //int resId = getResources().getIdentifier("b", "drawable", icon);
                                    int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
                                    Drawable draw;
                                    if (resourceId != 0) {
                                         draw = ContextCompat.getDrawable(requireContext(), resourceId);
                                    }else{
                                         draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                    }
                                    spinnerModel.add(new SpinnerModel(item.getInt("id"), item.getString("name"), draw));



                                }
                                //ArrayAdapter<SpinnerModel> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,  spinnerModel);
                                //adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                SpinnerAdapter adapterSpin = new SpinnerAdapter(requireContext(), spinnerModel);
                                spinner.setAdapter(adapterSpin);
                                // Set adapter to Spinner
                                //spinner.setAdapter(adapterSpinner);

                                // Handle Spinner item selection
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        //String selectedItem = parent.getItemAtPosition(position).toString();
                                        SpinnerModel selectedItem = (SpinnerModel) parent.getItemAtPosition(position);

                                        // Access the ID and Name
                                        //int selectedId = selectedItem.getId();
                                        selectedId = selectedItem.getId();
                                        if(firstLoad > 0 ){
                                            calendarglobal.set(Calendar.MONTH, calendarglobal.get(Calendar.MONTH));
                                            reloadCalendar(calendarglobal, 0);
                                        }
                                        firstLoad++;

                                        //spinnerSideLoader(selectedId);
                                        Log.e("vvvv", String.valueOf(selectedId));
                                        //loadOrganization(selectedId);
                                        List<String> holidayList = HolidayList.DATES;
                                        /*for (int i = 1; i < lastDay + 1; i++) {
                                            Log.e("gg", "--u-u-");
                                            List<CalendarOrganizationModel> ORGListEmpty = new ArrayList<>();
                                            adapter.setORGData(position, ORGListEmpty);

                                        }*/
                                        for (int i = 1; i < lastDay + 1; i++) {
                                            String rawDay = String.valueOf(i);
                                            if (i < 10) {
                                                rawDay = "0" + String.valueOf(i);
                                            }
                                            String rawMonth = String.valueOf(currentMonth + 1);
                                            if (currentMonth + 1 < 10) {
                                                rawMonth = "0" + String.valueOf(currentMonth + 1);
                                            }
                                            Log.e("wwwwwwwwwwwwwwwwwwwwwwwwww", (rawMonth + "-" + rawDay));

                                            if (holidayList.contains(rawMonth + "-" + rawDay)) {
                                                adapter.holidaySet((i - 1));
                                                Log.e("hol", (rawMonth + "-" + rawDay));
                                            }
                                            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;
                                            loadOffers((i-1), fullDate, selectedId);
                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Do nothing
                                    }
                                });
                                // Loop through JSON array
                                for (int i = 0; i < jsonArrayData.length(); i++) {

                                }
                                Log.d("JSON", "Lists populated successfully");
                            }
                        } else {
                            Log.e("JSON_ERROR", "Missing 'data' field in JSON response");
                        }

                    } catch (IOException e) {
                        Log.e("IO_ERROR", "Error reading response: " + e.getMessage());
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "Request failed: " + response.code() + ", " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("API_FAILURE", "Request failed: " + throwable.getMessage());
            }
        });

    }

    private void loadOffers(int position, String date, int object ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getOffers = retrofit.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        OffersPostModel offersPostModel = new OffersPostModel(idUser, date, object);

        Call<ResponseBody> callOffers = getOffers.postOffers("Bearer "+ sharedPreferences.getString("plainToken", ""), offersPostModel);
        callOffers.enqueue(new Callback<ResponseBody>() {
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
                            OffersList = new ArrayList<>();

                            if (jsonArrayData.length() != 0) {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    String comment = "";
                                    //idOfferMap.get()
                                    Drawable drawableComment = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_chat_bubble_24);
                                    int visibility = View.GONE;
                                    if(item.getString("comments").isEmpty()){
                                        comment = "";
                                    }else{
                                        comment = item.getString("comments");
                                        drawableComment.setTint(ContextCompat.getColor(requireContext(), R.color.primary));
                                    }
                                    if(item.getInt("visibility") == 0){
                                        visibility = View.INVISIBLE;
                                    }else{
                                        visibility = View.VISIBLE;

                                    }
                                    String buttonText = "Request";
                                    String buttonColor = "#ffffff";
                                    Log.e("status", String.valueOf(item.getInt("status" )));

                                    if(item.getInt("status") == 0){
                                            buttonText = "Request";
                                        buttonColor = "#0d6efd";
                                    }else if(item.getInt("status") == 1){
                                        buttonText = "Waiting";
                                        buttonColor = "#ffc107";
                                    }else if(item.getInt("status") == 2){
                                        buttonText = "Granted";
                                        buttonColor = "#198754";
                                    }else if(item.getInt("status") == 3){
                                        buttonText = "Denied";
                                        buttonColor = "#dc3545";
                                    }else{
                                        buttonText = "Unconfirmed";
                                        buttonColor = "#f9f9f9";
                                    }
                                    OffersList.add(new OffersModel(item.getInt("id"),item.getString("name"), item.getString("object"), item.getString("from"), item.getString("to"), item.getString("shift") , drawableComment , comment , item.getString("creation") , item.getString("color"), visibility,item.getInt("status"), buttonText, buttonColor));
                                    Log.e("ppppppppp", OffersList.toString());
                                }
                                adapter.setOffersData(position, OffersList);

                            }



                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                        // Handle invalid JSON
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                //adapter.updateAPI(position, "--:--", "--:--");


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
        //Log.e("qqqq", monthFormat.format(calendar.getTime()));
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
            List<OffersModel> ORGListEmpty = new ArrayList<>();


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                OffersList.add(new OffersModel());

                calendarModel.add(new CalendarModel2(i, String.valueOf(currentMonth + 1), dayFormat.format(formmater),  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24),ORGListEmpty));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            //monthNameList.add(dayFormat.format(fullDate));
        }
        adapter = new CalendarOffersAdapter(calendarModel,
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
            Log.e("vbv" ,(rawMonth + "-" + rawDay) );

            if(holidayList.contains(rawMonth + "-" + rawDay)){
                adapter.holidaySet((i-1));
                //Log.e("hol" ,(rawMonth + "-" + rawDay) );
            }
            String fullDate = currentYear + "-" + rawMonth + "-" + rawDay;
            loadWorkday((i - 1), fullDate);
            if(refresh == 1){
                loadOffers((i-1), fullDate, selectedId);
            }
        }
        //private void reloadCalendar(Calendar calendar, int refresh ) {

    }

    @Override
    public void toolTipInfo(int position, String user, String time, Context context, View v) {
        TooltipInfoHandler.showOfferInfo(v.getContext(), v,  user, time);
        //return true;
    }

    @Override
    public void toolTipComment(int position, String comment, Context context, View v) {
        TooltipCommentHandler.showOfferComment(v.getContext(), v,  comment);
    }

    @Override
    public void buttonClick(int position, int id_offer, int status, List<OffersModel> dataList, int parent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService updateOffers = retrofit.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);

        if(status == 0){
            OfferSendModel offerSendModel = new OfferSendModel(idUser ,id_offer );
            Call<ResponseBody> callOffers = updateOffers.sendOffer("Bearer "+ sharedPreferences.getString("plainToken", ""), offerSendModel);
            callOffers.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("response", response.toString());

                    if (response.isSuccessful()) {
                        Log.e("position", String.valueOf(position));
                        //adapterOffer.changeWaiting(position);
                        adapter.changeWaiting(position, dataList, parent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        }else if(status == 1){
            OfferSendModel offerSendModel = new OfferSendModel(idUser ,id_offer );
            Call<ResponseBody> callOffers = updateOffers.deleOffer("Bearer "+ sharedPreferences.getString("plainToken", ""), offerSendModel);
            callOffers.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("response", response.toString());

                    if (response.isSuccessful()) {
                        Log.e("position", String.valueOf(position));
                        //adapterOffer.changeWaiting(position);
                        adapter.changeDelete(position, dataList, parent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        }

    }
    //private void reloadCalendar(Calendar calendar, int refresh ) {

    //}

    }