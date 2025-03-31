package com.example.rp_android.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.adapters.BoardAdapter;
import com.example.rp_android.adapters.SpinnerAdapter;
import com.example.rp_android.models.BoardModel;
import com.example.rp_android.models.ORGModel;
import com.example.rp_android.models.ObjectModel;
import com.example.rp_android.models.SpinnerModel;
import com.example.rp_android.R;
import com.example.rp_android.UpCommingModel;
import com.example.rp_android.UpCommingShiftsAdapter;
import com.example.rp_android.adapters.OrganizationAdapter;
import com.example.rp_android.databinding.FragmentHomeBinding;
import com.example.rp_android.models.DetailModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView textName;
    private UpCommingShiftsAdapter adapter;
    private OrganizationAdapter adapterOrganization;
    private BoardAdapter adapterBoard;


    private RecyclerView recyclerView;
    private RecyclerView recyclerViewOrganization;
    private RecyclerView recyclerViewBoard;



    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] courses = {
            "C", "Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS"
    };
    private AppBarConfiguration mAppBarConfiguration;
    //private ActivityHomePageBinding binding;

    private List<String> colorList = new ArrayList<>();
    private List<String> objectList = new ArrayList<>();
    private List<String> fromList = new ArrayList<>();
    private List<String> toList = new ArrayList<>();
    private List<String> shiftList = new ArrayList<>();

    private List<String> idOrgList = new ArrayList<>();
    private List<String> fromOrgList = new ArrayList<>();
    private List<String> toOrgList = new ArrayList<>();
    private List<String> logFromOrgList = new ArrayList<>();
    private List<String> logToOrgList = new ArrayList<>();
    private List<String> colorOrgList = new ArrayList<>();
    private List<String> shiftOrgList = new ArrayList<>();
    private List<String> nameOrgList = new ArrayList<>();
    private List<String> imageOrgList = new ArrayList<>();
    private List<String> statusOrgList = new ArrayList<>();



    private List<String> selectId = new ArrayList<>();

    private List<String> selectName = new ArrayList<>();
    private List<SpinnerModel> spinnerModel = new ArrayList<>();
    private List<BoardModel> boardModel = new ArrayList<>();
    private List<ORGModel> orgModel = new ArrayList<>();

    private FragmentHomeBinding binding;
    private Spinner spinner;
    private String authToken;

    private MaterialButton confirm;
    private MaterialButton departure;
    private MaterialButton pauseEnd;
    private MaterialButton pauseStart;
    private LinearLayout noShiftDiv;
    private TextInputLayout logDiv;
    private TextInputEditText commentInput;
    private TextView dayOfWeekText;




    private Retrofit retrofit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //super.onViewCreated(savedInstanceState);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //TextView dayOfWeekText = view.findViewById(R.id.dayOfWeek);
        //Button loginButton = view.findViewById(R.id.loginButton);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerViewOrganization = binding.recycleViewOrganization;
        swipeRefreshLayout = binding.swipeRefreshLayout;
        recyclerViewBoard = binding.recyclerViewBoard;

        // Set the refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call your refresh method
                refreshContent();
            }
        });

        spinner = binding.objectSpinner;
        confirm = binding.confirmButton;
        departure = binding.departuButton;
        pauseEnd = binding.pauseEndButton;
        pauseStart = binding.pauseStartButton;
        noShiftDiv = binding.noShiftDiv;
        logDiv = binding.LogDiv;
        commentInput = binding.commentInput;
        dayOfWeekText = binding.dayOfWeek;
        //Spinner spin = findViewById(R.id.spinner);

        Zdroj: https://www.itnetwork.cz/java/android/vlastni-view/vlastni-android-spinner-vytvoreni-zakladniho-menu
        //setContentView(binding.getRoot());

        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);
        spinnerLoader(sharedPreferences.getString("plainToken", ""));

        /*if(sharedPreferences.getString("loginStatus", "false").equals("false")){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }*/
        loadBoard();
        //String dateFormat = DateFormat.getDateInstance(DateFormat.TIMEZONE_FIELD).format(calendar);
        /* source: https://stackoverflow.com/questions/5574673/what-is-the-easiest-way-to-get-the-current-day-of-the-week-in-android*/
        Date calendar = Calendar.getInstance().getTime();
        Calendar dayOfWeek = Calendar.getInstance();
        int day = dayOfWeek.get(Calendar.DAY_OF_WEEK);
        TimeZone tz = TimeZone.getDefault();
        //System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezone id :: " +tz.getID());
        //String dateFormat = DateFormat.getDateInstance(DateFormat.TIMEZONE_FIELD).format(calendar);
        /* source: https://stackoverflow.com/questions/5574673/what-is-the-easiest-way-to-get-the-current-day-of-the-week-in-android*/
        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeekText.setText("Sunday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
            case Calendar.MONDAY:
                dayOfWeekText.setText("Monday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
            case Calendar.TUESDAY:
                dayOfWeekText.setText("Tuesday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekText.setText("Wednesday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
            case Calendar.THURSDAY:
                dayOfWeekText.setText("Thursday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
            case Calendar.FRIDAY:
                dayOfWeekText.setText("Friday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
            case Calendar.SATURDAY:
                dayOfWeekText.setText("Saturday (" +tz.getDisplayName(false, TimeZone.SHORT) + ")");
                break;
        }
        /*switch (day) {
            case Calendar.SUNDAY:
                Log.e("String", "Sunday");
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                break;
            case Calendar.TUESDAY:
                // etc.
                break;
            case Calendar.WEDNESDAY:
                // etc.
                break;
            case Calendar.THURSDAY:
                // etc.
                break;
            case Calendar.FRIDAY:
                // etc.
                break;
            case Calendar.SATURDAY:
                dayOfWeekText.setText("Saturday");
                break;
        }*/
        //Button loginButton = view.findViewById(R.id.myButton);

        // Set OnClickListener

        /*binding.loginButton.setOnClickListener(v -> {
            Log.e("---","---0");

        });*/
        //TextView texEmail = (TextView) getView(R.id.emailFieldSidebar);
        attendaceConditions();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService searchUpcomming = retrofit.create(ApiDataService.class);
        //ApiDataService searchModel = new SearchModel("-");
        for (int x = 0; x < 3; x++) {
            Log.e("++++", String.valueOf(x));
            Calendar calendarDay = Calendar.getInstance();
            calendarDay.add(Calendar.DATE, -1 + x ); // Subtract 1 day
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String yesterdayDate = sdf.format(calendarDay.getTime());
            //String token = sharedPreferences.getString("token", "");
            Log.e("token", sharedPreferences.getString("token", ""));
            UpCommingModel model = new UpCommingModel(sharedPreferences.getInt("id", 0), yesterdayDate);
            Call<ResponseBody> callEmployee = searchUpcomming.postUpcomming("Bearer " + sharedPreferences.getString("plainToken", ""), model);
            int finalX = x;
            callEmployee.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("res", response.toString());

                    if (response.isSuccessful()) {
                        String res = null;
                        try {
                            res = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.e("body", "-------------");
                        Log.e("body", response.message());
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Object dataObject = jsonObject.get("data");

                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;
                                Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                            } else if (dataObject instanceof JSONArray) {
                                JSONArray jsonArrayData = (JSONArray) dataObject;
                                Log.e("++++", String.valueOf(finalX));

                                Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " elements. " );
                                fromList = new ArrayList<>();
                                toList = new ArrayList<>();
                                colorList = new ArrayList<>();
                                objectList = new ArrayList<>();
                                shiftList = new ArrayList<>();


                                //int  = 0;
                                if (jsonArrayData.length() != 0) {
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        JSONObject item = jsonArrayData.getJSONObject(i);
                                        fromList.add(item.getString("from"));
                                        //Log.e("from", item.getString("from"));
                                        toList.add(item.getString("to"));
                                        colorList.add(item.getString("color"));
                                        objectList.add(item.getString("object"));
                                        shiftList.add(item.getString("shift"));

                                /*colorList = new ArrayList<>();
                                objectList = new ArrayList<>();
                                shiftList = new ArrayList<>();*/
                                        //Log.d("JSON_ITEM", "ID: " + item.getString("profile_url"));
                                        //dataList.add(item.getString("firstname") + " " + item.getString("middlename") +  item.getString("lastname"));
                                        //emailList.add(item.getString("email"));
                                        //idList.add(item.getString("id"));
                                        //imgURLList.add(ConnectionFile.returnURLRaw() + item.getString("profile_url"));
                                    }
                                } else {
                                    fromList.add("------");
                                    //Log.e("from", item.getString("from"));
                                    toList.add("------");
                                    colorList.add("#000000");
                                    objectList.add("0");
                                    shiftList.add("#000000");
                                }

                                adapter = new UpCommingShiftsAdapter(colorList, objectList, fromList, toList, shiftList);
                                //Log.e("--", "789");
                                if(finalX == 0){
                                    recyclerView = binding.recycleViewYesterday;

                                }else if(finalX == 1){
                                    recyclerView = binding.recycleViewToday;
                                }else{
                                    recyclerView = binding.recycleViewTommorow;
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                recyclerView.setAdapter(adapter);
                                //recyclerView.setAdapter(adapter);
                                //searchBar.setOnClickListener(v -> searchView.show());*/
                                //adapter.filter(query.toString());

                            }/*else{
                            JSONArray jsonArrayData = (JSONArray) dataObject;

                            Log.d("JSON", "Data is an array with " + jsonArrayData.length() +" elements.");

                        }*/


                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                            // Handle invalid JSON
                        }
                    } else {
                        //Toast.makeText(requireContext(), "Error in success response", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        }



        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //showDialog();


        confirm.setOnClickListener(v -> {
                    Retrofit retrofitLogin = new Retrofit.Builder()
                            .baseUrl(ConnectionFile.returnURL())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

            ApiDataService getLogin = retrofitLogin.create(ApiDataService.class);
            String text = String.valueOf(commentInput.getText());
            DetailModel object = new DetailModel(text);

            Call<ResponseBody> callLogin = getLogin.confirmArrival("Bearer "+ sharedPreferences.getString("plainToken", ""), object);
            callLogin.enqueue(new Callback<ResponseBody>() {
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
                            //int status = jsonObject.getInt("status");
                            int comment = jsonObject.getInt("comment");
                            int status = jsonObject.getInt("status");
                            if(status == 0){
                                confirm.setVisibility(View.GONE);
                                pauseStart.setVisibility(View.VISIBLE);
                                departure.setVisibility(View.VISIBLE);
                                commentInput.setText("");
                                showCustomDialog("Your arrival has been confirmed", "", "baseline_check_circle_outline_24");

                            }else if(status == 1){
                                showCustomDialog("Your arrival has not been confirmed", "Please enter comment why are you late", "baseline_remove_circle_outline_24");

                            }else if(status == 2){
                                showCustomDialog("Your arrival has not been confirmed", "Please enter comment why are you early", "baseline_remove_circle_outline_24");

                            }else if(status == 4){
                                showCustomDialog("Your arrival has not been confirmed", "Your current device is not register in the system", "baseline_remove_circle_outline_24");

                            }else if(status == 5){
                                showCustomDialog("Your arrival has not been confirmed", "Connection error", "baseline_remove_circle_outline_24");

                            }else{
                                showCustomDialog("Your arrival has not been confirmed", "", "baseline_remove_circle_outline_24");

                            }

                            // Check if "data" exists
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    showCustomDialog("Your arrival has not been confirmed", "Connection error", "baseline_remove_circle_outline_24");

                }
            });
        });
        pauseStart.setOnClickListener(v -> {
            Retrofit retrofitPauseStart = new Retrofit.Builder()
                    .baseUrl(ConnectionFile.returnURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiDataService getPause = retrofitPauseStart.create(ApiDataService.class);
            String text = String.valueOf(commentInput.getText());
            ObjectModel object = new ObjectModel(sharedPreferences.getInt("id", 0));

            Call<ResponseBody> callPauseStart = getPause.pauseStart("Bearer "+ sharedPreferences.getString("plainToken", ""), object);
            callPauseStart.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("res", response.toString());
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            // Convert response body to string
                            String res = response.body().string();
                            Log.d("API_RESPONSE", "Response: " + res);
                            JSONObject jsonObject = new JSONObject(res);
                            //int status = jsonObject.getInt("status");
                            String status = jsonObject.getString("status");
                            if(status.equals("success")){
                                pauseStart.setVisibility(View.GONE);
                                pauseEnd.setVisibility(View.VISIBLE);

                                showCustomDialog("Your pause has been confirmed", "", "baseline_check_circle_outline_24");

                            }else{
                                showCustomDialog("Your pause has not been confirmed", "Connection erorr detected", "baseline_remove_circle_outline_24");
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    showCustomDialog("Your arrival has not been confirmed", "Connection error", "baseline_remove_circle_outline_24");

                }
            });
        });
        pauseEnd.setOnClickListener(v -> {
            Retrofit retrofitPauseEnd = new Retrofit.Builder()
                    .baseUrl(ConnectionFile.returnURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiDataService getPause = retrofitPauseEnd.create(ApiDataService.class);
            ObjectModel object = new ObjectModel(sharedPreferences.getInt("id", 0));

            Call<ResponseBody> callPauseEnd = getPause.pauseEnd("Bearer "+ sharedPreferences.getString("plainToken", ""), object);
            callPauseEnd.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("res", response.toString());
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            // Convert response body to string
                            String res = response.body().string();
                            Log.d("API_RESPONSE", "Response: " + res);
                            JSONObject jsonObject = new JSONObject(res);
                            //int status = jsonObject.getInt("status");
                            String status = jsonObject.getString("status");
                            if(status.equals("success")){
                                pauseEnd.setVisibility(View.GONE);
                                pauseStart.setVisibility(View.VISIBLE);

                                showCustomDialog("Your pause has been ended", "", "baseline_check_circle_outline_24");

                            }else{
                                showCustomDialog("Your pause has not been ende", "Connection erorr detected", "baseline_remove_circle_outline_24");
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    showCustomDialog("Your arrival has not been confirmed", "Connection error", "baseline_remove_circle_outline_24");

                }
            });
        });
        departure.setOnClickListener(v -> {
            Retrofit retrofitDeparture = new Retrofit.Builder()
                    .baseUrl(ConnectionFile.returnURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiDataService getDeparture = retrofitDeparture.create(ApiDataService.class);
            String text = String.valueOf(commentInput.getText());
            DetailModel object = new DetailModel(text);

            Call<ResponseBody> callDeparture = getDeparture.confirmDeparture("Bearer "+ sharedPreferences.getString("plainToken", ""), object);
            callDeparture.enqueue(new Callback<ResponseBody>() {
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
                            //int status = jsonObject.getInt("status");
                            int comment = jsonObject.getInt("comment");
                            int status = jsonObject.getInt("left");
                            if(status == 0){
                                /*confirm.setVisibility(View.GONE);
                                pauseStart.setVisibility(View.VISIBLE);
                                departure.setVisibility(View.VISIBLE);
                                commentInput.setText("");
                                showCustomDialog("Your arrival has been confirmed", "", "baseline_check_circle_outline_24");
*/                              attendaceConditions();
                                commentInput.setText("");
                                showCustomDialog("Your arrival has been confirmed", "", "baseline_check_circle_outline_24");
                            }else if(status == 1){
                                showCustomDialog("Your departure has not been confirmed", "Please enter comment why are you late", "baseline_remove_circle_outline_24");

                            }else if(status == 2){
                                showCustomDialog("Your departure has not been confirmed", "Please enter comment why are you early", "baseline_remove_circle_outline_24");

                            }else if(status == 4){
                                showCustomDialog("Your departure has not been confirmed", "Your current device is not register in the system", "baseline_remove_circle_outline_24");

                            }else if(status == 5){
                                showCustomDialog("Your departure has not been confirmed", "Connection error", "baseline_remove_circle_outline_24");

                            }else{
                                showCustomDialog("Your departure has not been confirmed", "Error not identify", "baseline_remove_circle_outline_24");

                            }

                            // Check if "data" exists
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    showCustomDialog("Your arrival has not been confirmed", "Connection error", "baseline_remove_circle_outline_24");

                }
            });
        });


        return root;
    }
    private void showCustomDialog(String header, String text, String icon) {
        // Create a new dialog
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attendance_dialog);
        dialog.setCancelable(false);
        int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
        Drawable draw;
        if (resourceId != 0) {
            draw = ContextCompat.getDrawable(requireContext(), resourceId);
        }else{
            draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
        }
        // Find and handle the "Close" button
        ImageView imageDialog = dialog.findViewById(R.id.imageDialog);
        TextView dialogTextHeader = dialog.findViewById(R.id.dialogTextHeader);
        TextView dialogText = dialog.findViewById(R.id.dialogText);
        Drawable dialogIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_check_circle_outline_24);
        dialogIcon.setTint(ContextCompat.getColor(requireContext(), R.color.danger));
        if("baseline_remove_circle_outline_24" == icon){
            draw.setTint(ContextCompat.getColor(requireContext(), R.color.danger));

        }else if("baseline_check_circle_outline_24" == icon){
            draw.setTint(ContextCompat.getColor(requireContext(), R.color.success_light));

        }
        imageDialog.setImageDrawable(draw);
        dialogTextHeader.setText(header);
        dialogText.setText(text);

        Button btnClose = dialog.findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Close the dialog
            }
        });

        // Show the dialog
        dialog.show();
    }
    /*private void showDialog() {
        AttendanceDialog dialog = AttendanceDialog.newInstance(
                "Confirmation",
                "Do you want to proceed with this action?",
                "Confirm",
                "Cancel"
        );
        dialog.show(getChildFragmentManager(), "confirmation_dialog");
    }*/

    public void spinnerLoader(String token){
        List<String> spinnerItems = new ArrayList<>();
        Retrofit retrofitSecond = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getObjects = retrofitSecond.create(ApiDataService.class);
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
                                    //selectId.add(item.getString("id"));
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

                                    //Drawable draw = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_flag_24);
                                    spinnerModel.add(new SpinnerModel(item.getInt("id"), item.getString("name"), draw));
                                    /*spinnerModel.add(new SpinnerModel(2, "Item Two"));
                                    spinnerModel.add(new SpinnerModel(3, "Item Three"));*/
                                    // Create an ArrayAdapter using the list and a default spinner layout

                                    //Log.e("from", item.getString("from"));



                                }
                                //ArrayAdapter<SpinnerModel> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,  spinnerModel);
                                //adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // Set adapter to Spinner
                                //spinner.setAdapter(adapterSpinner);
                                SpinnerAdapter adapterSpinSide = new SpinnerAdapter(requireContext(), spinnerModel);
                                spinner.setAdapter(adapterSpinSide);
                                // Handle Spinner item selection
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        //String selectedItem = parent.getItemAtPosition(position).toString();
                                        SpinnerModel selectedItem = (SpinnerModel) parent.getItemAtPosition(position);

                                        // Access the ID and Name
                                        int selectedId = selectedItem.getId();
                                        String selectedName = selectedItem.getName();
                                        Log.e("vvvv", String.valueOf(selectedId));
                                        loadOrganization(selectedId);
                                        // Prevent toast for the first default item
                                        /*if (position != 0) {
                                            Toast.makeText(requireContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                                        }*/
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

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Make toast of the name of the course which is selected in the spinner
        Toast.makeText(requireContext(), courses[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No action needed when no selection is made
    }*/
    /*    Zdroj: https://www.itnetwork.cz/java/android/vlastni-view/vlastni-android-spinner-vytvoreni-zakladniho-menu
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void refreshContent() {
        // Perform your refresh operations here (e.g., fetch new data)

        // Simulating network call or data refresh with delay
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop the refresh animation
                swipeRefreshLayout.setRefreshing(false);
                //Toast.makeText().show();
            }
        }, 2000); // 2 seconds delay
    }

    private void loadOrganization(Integer idObject){
        Retrofit retrofitSecond = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getOrganization = retrofitSecond.create(ApiDataService.class);
        ObjectModel object = new ObjectModel(idObject);

        Call<ResponseBody> callOrganization = getOrganization.postOrganization("Bearer "+ sharedPreferences.getString("plainToken", ""), object);
        callOrganization.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());

                if (response.isSuccessful()) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.e("body", "-------------");
                    Log.e("body--ydob", response.message());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        Object dataObject = jsonObject.get("data");

                        if (dataObject instanceof JSONObject) {
                            JSONObject jsonObjectData = (JSONObject) dataObject;
                            Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                        } else if (dataObject instanceof JSONArray) {
                            JSONArray jsonArrayData = (JSONArray) dataObject;
                            Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " elements. ");
                            /*idOrgList = new ArrayList<>();
                            fromOrgList = new ArrayList<>();
                            toOrgList = new ArrayList<>();
                            logFromOrgList = new ArrayList<>();
                            logToOrgList = new ArrayList<>();
                            nameOrgList = new ArrayList<>();
                            colorOrgList = new ArrayList<>();
                            shiftOrgList = new ArrayList<>();
                            imageOrgList = new ArrayList<>();
                            statusOrgList = new ArrayList<>();*/
                            orgModel = new ArrayList<>();
                            Log.e("ddatt" , jsonArrayData.toString());
                            if (jsonArrayData.length() != 0) {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    /*idOrgList.add(item.getString("id"));
                                    fromOrgList.add(item.getString("from"));
                                    toOrgList.add(item.getString("to"));
                                    logFromOrgList.add(item.getString("logFrom"));
                                    logToOrgList.add(item.getString("logTo"));
                                    nameOrgList.add(item.getString("name"));
                                    colorOrgList.add(item.getString("color"));
                                    Log.e("color", item.getString("color"));
                                    shiftOrgList.add(item.getString("shift"));
                                    imageOrgList.add(ConnectionFile.returnURLRaw()+item.getString("imgURL"));
                                    statusOrgList.add(item.getString("status"));*/
                                    String icon = (item.getString("icon").substring(3)).replaceAll("-", "_");;
                                    int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
                                    Drawable draw;
                                    if(!icon.equals("none")) {
                                        if (resourceId != 0) {
                                            draw = ContextCompat.getDrawable(requireContext(), resourceId);
                                        } else {
                                            draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                        }
                                    }else{
                                        draw = new ColorDrawable(Color.TRANSPARENT);
                                    }
                                    orgModel.add(new ORGModel(item.getInt("id"), item.getString("from"),  item.getString("to"),  item.getString("logFrom"),
                                            item.getString("logTo"),item.getString("name"), item.getString("color"), item.getString("shift"), ConnectionFile.returnURLRaw()+item.getString("imgURL"), item.getString("status"), draw));
                                }

                            } else {
                                /*fromList.add("------");
                                toList.add("------");
                                colorList.add("#000000");
                                objectList.add("0");
                                shiftList.add("#000000");*/
                            }
                            adapterOrganization = new OrganizationAdapter(orgModel, getParentFragmentManager());
                            //adapterOrganization = new OrganizationAdapter(idOrgList, fromOrgList,  toOrgList, logFromOrgList, logToOrgList, colorOrgList, nameOrgList, shiftOrgList, imageOrgList, statusOrgList, getParentFragmentManager());
                            /*adapter = new UpCommingShiftsAdapter(colorList, objectList, fromList, toList, shiftList);*/
                            recyclerViewOrganization.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerViewOrganization.setAdapter(adapterOrganization);

                            /*adapter = new UpCommingShiftsAdapter(colorList, objectList, fromList, toList, shiftList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(adapter);*/


                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                        // Handle invalid JSON
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }
    public void attendaceConditions(){
        Retrofit retrofitConditions = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getConditions= retrofitConditions.create(ApiDataService.class);

        ObjectModel model = new ObjectModel(sharedPreferences.getInt("id", 0));
        Call<ResponseBody> callConditions = getConditions.attendanceConditions("Bearer " + sharedPreferences.getString("plainToken", ""), model);
        //int finalX = x;
        callConditions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Convert response body to string
                        String res = response.body().string();

                        JSONObject jsonObject = new JSONObject(res);
                        int pause = jsonObject.getInt("pause");
                        int checkform = jsonObject.getInt("checkform");
                        int log_exists = jsonObject.getInt("log_exists");
                        if(checkform == 1){
                            confirm.setVisibility(View.GONE);
                            departure.setVisibility(View.VISIBLE);
                            if(pause == 0){
                                pauseStart.setVisibility(View.VISIBLE);
                                pauseEnd.setVisibility(View.GONE);
                            }else{
                                pauseEnd.setVisibility(View.VISIBLE);
                                pauseStart.setVisibility(View.GONE);
                            }
                        }else {
                            confirm.setVisibility(View.VISIBLE);
                            departure.setVisibility(View.GONE);
                        }
                        if(log_exists == 1){
                            noShiftDiv.setVisibility(View.GONE);
                            logDiv.setVisibility(View.VISIBLE);
                        }else{
                            noShiftDiv.setVisibility(View.VISIBLE);
                            logDiv.setVisibility(View.GONE);
                        }


                        Log.d("API_RESPONSE", "Response: " );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }
    public void loadBoard(){
        Retrofit retrofitBoard= new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getBoard= retrofitBoard.create(ApiDataService.class);

        DetailModel model = new DetailModel(sharedPreferences.getString("role", "parttime"));
        Call<ResponseBody> callBoards = getBoard.boardLoader("Bearer " + sharedPreferences.getString("plainToken", ""), model);
        //int finalX = x;
        callBoards.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());
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
                            boardModel = new ArrayList<>();
                            if (jsonArrayData.length() != 0) {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    int visibility = View.GONE;
                                    Log.e("profile", ConnectionFile.returnURLRaw()+item.getString("profile_link"));
                                    if(!item.getString("board_link").isEmpty()){
                                        visibility = View.VISIBLE;
                                    }
                                    Log.e("board", ConnectionFile.returnURLRaw()+item.getString("board_link"));
                                    boardModel.add(new BoardModel(item.getInt("id_board"),item.getString("content"), item.getString("caption"),item.getString("color"),
                                            item.getString("board_creation").substring(0, 10), ConnectionFile.returnURLRaw()+item.getString("board_link"), ConnectionFile.returnURLRaw()+ item.getString("profile_link"), item.getString("name"), visibility ));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());

                    }
                }
                /*if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Convert response body to string
                        String res = response.body().string();

                        JSONObject jsonObject = new JSONObject(res);

                        Log.d("API_RESPONSE",  res);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }*/
                adapterBoard = new BoardAdapter(boardModel,
                        getParentFragmentManager(),
                        requireContext()
                );

                //adapterBoard = new BoardAdapter(boardModel, getParentFragmentManager());
                //adapterOrganization = new OrganizationAdapter(idOrgList, fromOrgList,  toOrgList, logFromOrgList, logToOrgList, colorOrgList, nameOrgList, shiftOrgList, imageOrgList, statusOrgList, getParentFragmentManager());
                /*adapter = new UpCommingShiftsAdapter(colorList, objectList, fromList, toList, shiftList);*/
                recyclerViewBoard.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewBoard.setAdapter(adapterBoard);
                if(boardModel.size() !=0 ){
                    for (int i = 0; i < boardModel.size(); i++) {
                        adapterBoard.setImgae(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

}