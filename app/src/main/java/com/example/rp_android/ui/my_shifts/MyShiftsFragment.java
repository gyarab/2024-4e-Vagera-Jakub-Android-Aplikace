package com.example.rp_android.ui.my_shifts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.R;
import com.example.rp_android.adapters.CalendarMyShiftsAdapter;
import com.example.rp_android.adapters.MyShiftsAdapter;
import com.example.rp_android.databinding.FragmentMyShiftsBinding;
import com.example.rp_android.models.CalendarModel3;
import com.example.rp_android.models.MyShiftsModel;
import com.example.rp_android.models.ORGPostModel;
import com.example.rp_android.models.OptionsPostModel;
import com.example.rp_android.models.SpinnerModel;
import com.example.rp_android.resources.HolidayList;
import com.example.rp_android.tooltip.TooltipCommentHandler;
import com.example.rp_android.ui.options.OptionsViewModel;

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


public class MyShiftsFragment extends Fragment implements CalendarMyShiftsAdapter.OnItemClickListener, MyShiftsAdapter.OnItemClickListener  {
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

    private List<MyShiftsModel> myList = new ArrayList<>();

    private OptionsViewModel mViewModel;
    private RecyclerView recyclerView;
    private CalendarMyShiftsAdapter adapter;
    private MyShiftsAdapter adapterOffer;

    private TextView monthText;
    private TextView yearText;
    private List<CalendarModel3> calendarModel;
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

    private List<MyShiftsModel> myShiftsList = new ArrayList<>();
    private FragmentMyShiftsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyShiftsViewModel myShiftsViewModel =
                new ViewModelProvider(this).get(MyShiftsViewModel.class);

        binding = FragmentMyShiftsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textGallery;
        //myShiftsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        recyclerView = binding.recycleViewCalendar;
        monthText = binding.monthText;
        yearText = binding.yearText;
        //spinner = binding.mainObjectSpinner;
        //spinnerSide = binding.sideObjectSpinner;

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
        //spinnerLoader();
        return root;
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
            List<MyShiftsModel> OffersListEmpty = new ArrayList<>();


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                //monthNameList.add(dayFormat.format(formmater) + ".");
                /*if(i == 1) {
                    myShiftsList.add(new MyShiftsModel(1, "kl", "jk", "j", "kj", "jkbjk", "jjkb", "hjk", "hlk"));
                }*/
                calendarModel.add(new CalendarModel3(i, String.valueOf(currentMonth + 1), dayFormat.format(formmater),  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24),myShiftsList));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            //monthNameList.add(dayFormat.format(fullDate));
        }
        adapter = new CalendarMyShiftsAdapter(calendarModel,
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
            loadMyShifts((i - 1), fullDate);
        }
        //searchBar.setOnClickListener(v -> searchView.show());
    }


    public int getLastDayOfMonth(int year, int monthNumber) {
        Calendar calendarExtra = Calendar.getInstance();
        calendarExtra.set(Calendar.YEAR, year);
        calendarExtra.set(Calendar.MONTH, monthNumber); // Month index starts from 0
        return calendarExtra.getActualMaximum(Calendar.DAY_OF_MONTH);
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

                    adapter.workSet(position);

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
            List<MyShiftsModel> myShiftsListEmpty = new ArrayList<>();


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date formmater = inputFormat.parse(fullDate); // Convert String to Date
                myShiftsList.add(new MyShiftsModel());

                calendarModel.add(new CalendarModel3(i, String.valueOf(currentMonth + 1), dayFormat.format(formmater),  ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24),ContextCompat.getDrawable(requireContext(), R.drawable.baseline_inbox_24), ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24),myShiftsListEmpty));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            //monthNameList.add(dayFormat.format(fullDate));
        }
        adapter = new CalendarMyShiftsAdapter(calendarModel,
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
                loadMyShifts((i-1), fullDate);
            }
        }
        //private void reloadCalendar(Calendar calendar, int refresh ) {

    }


    /*public void spinnerLoader(){
        List<String> spinnerItems = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getObjects = retrofit.create(ApiDataService.class);
        DetailModel model = new DetailModel("ds");

        Call<ResponseBody> callObject = getObjects.postObjects("Bearer ", model);
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
                                            //reloadCalendar(calendarglobal, 0);
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
                                        /*for (int i = 1; i < lastDay + 1; i++) {
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
                                            //loadOffers((i-1), fullDate, selectedId);
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

    }*/

    private void loadMyShifts(int position, String date ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getORG = retrofit.create(ApiDataService.class);
        int idUser = sharedPreferences.getInt("id", 0);
        ORGPostModel modelORG = new ORGPostModel(date, idUser);

        Call<ResponseBody> callORG = getORG.myShifts("Bearer "+ sharedPreferences.getString("plainToken", ""), modelORG);
        callORG.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", response.toString());
                //currnetLoad(calendarglobal);
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
                            Log.d("JSON_Shift"+ position, "Data is an array with " + jsonArrayData.length() + " elements. ");
                            myList = new ArrayList<>();

                            if (jsonArrayData.length() != 0) {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    Log.e("main", item.getString("main_object"));
                                    myList.add(new MyShiftsModel(item.getInt("id"), item.getString("from"), item.getString("to"), item.getString("color"), item.getString("shift"), item.getString("main_object") + " - " + item.getString("object"), ConnectionFile.returnURLRaw()+item.getString("image"), "", item.getString("comment")));
                                    //Log.e("ppppppppp", myList.toString());
                                }
                                adapter.setMyList(position, myList);

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

    @Override
    public void toolTipComment(int position, String comment, Context context, View v) {
        TooltipCommentHandler.showOfferComment(v.getContext(), v,  comment);
    }
}