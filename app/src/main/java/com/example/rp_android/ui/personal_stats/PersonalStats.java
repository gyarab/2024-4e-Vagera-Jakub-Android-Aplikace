package com.example.rp_android.ui.personal_stats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.R;
import com.example.rp_android.adapters.StatisticsAdapter;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.databinding.FragmentPersonalStatsBinding;
import com.example.rp_android.models.StatisticModel;
import com.example.rp_android.models.StatisticsPostModel;
import com.example.rp_android.ui.slideshow.SlideshowViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PersonalStats extends Fragment implements StatisticsAdapter.OnItemClickListener{

    private FragmentPersonalStatsBinding binding;
    private Spinner spinnerYear;
    private Spinner spinnerMonth;
    private Retrofit retrofit;
    SharedPreferences sharedPreferences;
    private StatisticsAdapter adapter;

    private List<StatisticModel> statisticModel;
    private RecyclerView recyclerView;
    private TextView scheduleText;
    private TextView logText;
    private List<String> years;
    private int currentM;
    private  int currentY;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentPersonalStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        spinnerYear = binding.yearSpinner;
        spinnerMonth = binding.monthSpinner;
        recyclerView = binding.tableRecycleView;
        scheduleText = binding.scheduleText;
        logText = binding.logText;

        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);

        /*final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        int[] monthValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        currentM = Calendar.getInstance().get(Calendar.MONTH); // 0-indexed

        years = new ArrayList<>();
        currentY = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 2020; year <= currentY; year++) {
            years.add(String.valueOf(year));
        }
        Log.e("----", String.valueOf(currentM));
        setupYearSpinner(years, currentY);
        setupMonthSpinner(monthValues);
        loadStats( currentY,  currentM);


        return root;
    }
    private void setupYearSpinner(List<String> years, int currentYear) {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
        int currentYearPosition = years.indexOf(String.valueOf(currentYear));
        if (currentYearPosition != -1) {
            spinnerYear.setSelection(currentYearPosition);
        }
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the corresponding integer value
                int selectYear = Integer.parseInt(years.get(position));
                loadStats( selectYear,  currentM);

                // Display the value (Toast or Log)
               // Toast.makeText(getContext(), "Year"+selectYear, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected (optional)
            }
        });
    }

    private void setupMonthSpinner(int[] monthValues) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        //int[] monthValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH); // 0-indexed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        spinnerMonth.setSelection(currentMonth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the corresponding integer value
                int selectedMonthValue = monthValues[position];
                loadStats( currentY,  selectedMonthValue-1);
                // Display the value (Toast or Log)
                //Toast.makeText(getContext(), "Month: " + selectedMonthName + " (Value: " + selectedMonthValue + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected (optional)
            }
        });
    }

    private void loadStats(int currentYear, int currentMonth){
        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiDataService getStats= retrofit.create(ApiDataService.class);

        StatisticsPostModel statisticsPostModel = new StatisticsPostModel(sharedPreferences.getInt("id",0),currentMonth+1,currentYear);
        String token = sharedPreferences.getString("plainToken", "");

        //GetSendModel getSendModel = new GetSendModel(sharedPreferences.getInt("id",0),employeeId);
        Call<ResponseBody> callStats= getStats.statisticCall("Bearer "+ token, statisticsPostModel);
        callStats.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Log.e("ressdwa", response.toString());
                Log.e("res", response.toString());
                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Log.e("body", response.message());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        Object dataObject = jsonObject.get("data");
                        scheduleText.setText(jsonObject.getString("scheduleTotal"));
                        logText.setText(jsonObject.getString("logTotal"));

                        if (dataObject instanceof JSONObject) {
                            JSONObject jsonObjectData = (JSONObject) dataObject;
                            Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                        } else if (dataObject instanceof JSONArray) {
                            JSONArray jsonArrayData = (JSONArray) dataObject;
                            Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " elements.");
                            /*dataList = new ArrayList<>();
                            emailList = new ArrayList<>();
                            imgURLList = new ArrayList<>();
                            idList = new ArrayList<>();*/

                            statisticModel = new ArrayList<>();
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                JSONObject item = jsonArrayData.getJSONObject(i);
                                int resourceMain = getResources().getIdentifier((item.getString("mainIcon").substring(3)).replaceAll("-", "_"), "drawable", requireContext().getPackageName());
                                Drawable drawMain;
                                if (resourceMain != 0) {
                                    drawMain = ContextCompat.getDrawable(requireContext(), resourceMain);
                                }else{
                                    drawMain = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                }
                                Log.e("ic - 2" , item.getString("mainIcon"));
                                Log.e("ic - 1" , item.getString("subIcon"));
                                int resourceSub = getResources().getIdentifier((item.getString("subIcon").substring(3)).replaceAll("-", "_"), "drawable", requireContext().getPackageName());
                                Drawable drawSub;
                                if (resourceSub != 0) {
                                    drawSub = ContextCompat.getDrawable(requireContext(), resourceSub);
                                }else{
                                    drawSub = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                }
                                statisticModel.add(new StatisticModel(item.getString("date"),item.getString("shift"), item.getString("main"), item.getString("object"),
                                        item.getString("scheduled"), item.getString("log"), drawMain, drawSub,item.getString("logInTime"), item.getString("logOutTime"), item.getString("color")));

                                //Log.d("JSON_ITEM", "ID: " + item.getString("profile_url"));
                               /* dataList.add(item.getString("firstname") + " " + item.getString("middlename") +  item.getString("lastname"));
                                emailList.add(item.getString("email"));
                                idList.add(item.getString("id"));
                                imgURLList.add(ConnectionFile.returnURLRaw() + item.getString("profile_url"));*/
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    adapter = new StatisticsAdapter(statisticModel,
                            PersonalStats.this,
                            getParentFragmentManager(),
                            requireContext()
                    );

                    //loadOptions("2025-03-05");
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void openDialog(int position) {

    }
}