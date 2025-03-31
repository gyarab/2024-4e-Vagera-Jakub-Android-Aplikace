package com.example.rp_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.connection.ConnectionFile;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rp_android.databinding.ActivityHomePageBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView textName;
    private UpCommingShiftsAdapter adapter;
    private RecyclerView recyclerView;
    private Menu sideMenu;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    private List<String> colorList = new ArrayList<>();
    private List<String> objectList = new ArrayList<>();
    private List<String> fromList = new ArrayList<>();
    private List<String> toList = new ArrayList<>();
    private List<String> shiftList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //recyclerView = binding.r;

        sharedPreferences = getApplication().getSharedPreferences("com.rp_android.app", MODE_PRIVATE);

        //NavigationView navigationView = findViewById(R.id.mobile_navigation);
        //String email = sharedPreferences.getString("email", "email@gmail.com"); // Default value if not found
        //texEmail.setText("---");

        if(sharedPreferences.getString("loginStatus", "false").equals("false")){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        setSupportActionBar(binding.appBarHomePage.toolbar);
        binding.appBarHomePage.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        binding.appBarHomePage.fab.setVisibility(View.GONE);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerLayout = navigationView.getHeaderView(0);
        TextView texEmail = headerLayout.findViewById(R.id.emailFieldSidebar);
        ImageView profile = headerLayout.findViewById(R.id.profileImage);
        //recyclerView = navigationView.findViewById(R.id.recycleViewYesterday); // Correct reference to RecyclerView


        texEmail.setText(sharedPreferences.getString("email", "email@example.com"));
        TextView textName = headerLayout.findViewById(R.id.nameFieldSidebar);
        String firstname = sharedPreferences.getString("firstname", "");
        String middlename = sharedPreferences.getString("middlename", "");
        String lastname = sharedPreferences.getString("lastname", "");
        if(firstname.trim().equals("null")){
           firstname = "";
        }
        if(middlename.trim().equals("null")){
            middlename = "";
        }
        if(lastname.trim().equals("null")){
            lastname = "";
        }

        textName.setText(firstname + " " + middlename +" " + lastname);
        changeText();

            URL url = null;
    /** source:: https://stackoverflow.com/questions/5776851/load-image-from-url*/
    ConnectionFile connection = new ConnectionFile();
    Log.e("----", connection.returnURLRaw());
    String BaseConnection = connection.returnURLRaw() + sharedPreferences.getString("profileURL", "");
        try {
            url = new URL(BaseConnection);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        URLConnection ucon = null;
        try {
            ucon = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        URLConnection finalUcon = ucon;
        new Thread(() -> {
            try {
                InputStream inputStream = finalUcon.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                runOnUiThread(() -> profile.setImageBitmap(image)); // Update UI on main thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();





        TextView dayOfWeekText = findViewById(R.id.dayOfWeek);

        //binding = com.example.rp_android.databinding.FragmentHomeBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        Date calendar = Calendar.getInstance().getTime();
        Calendar dayOfWeek = Calendar.getInstance();
        int day = dayOfWeek.get(Calendar.DAY_OF_WEEK);
        TimeZone tz = TimeZone.getDefault();
        //System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezone id :: " +tz.getID());
        //String dateFormat = DateFormat.getDateInstance(DateFormat.TIMEZONE_FIELD).format(calendar);
        /* source: https://stackoverflow.com/questions/5574673/what-is-the-easiest-way-to-get-the-current-day-of-the-week-in-android*/
        /*switch (day) {
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
        }*/


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService searchUpcomming = retrofit.create(ApiDataService.class);
        //ApiDataService searchModel = new SearchModel("-");
        Calendar calendarDay = Calendar.getInstance();
        calendarDay.add(Calendar.DATE, 0); // Subtract 1 day
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String yesterdayDate = sdf.format(calendarDay.getTime());
        String token = sharedPreferences.getString("token", "");
        UpCommingModel model = new UpCommingModel(sharedPreferences.getInt("id", 0), yesterdayDate);
       /* Call<ResponseBody> callEmployee = searchUpcomming.postUpcomming("Bearer "+ sharedPreferences.getString("token", ""), model);
        callEmployee.enqueue(new Callback<ResponseBody>() {
            /*@Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());

                if (response.isSuccessful()) {
                    /*String res = null;
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
                            Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " elements.");
                            fromList = new ArrayList<>();
                            toList = new ArrayList<>();
                            colorList = new ArrayList<>();
                            objectList = new ArrayList<>();
                            shiftList = new ArrayList<>();


                            int passedId = 0;
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                JSONObject item = jsonArrayData.getJSONObject(i);
                                fromList.add(item.getString("from"));
                                //Log.e("from", item.getString("from"));
                                toList.add(item.getString("to"));
                                colorList.add(item.getString("color"));
                                objectList.add(item.getString("object"));
                                shiftList.add(item.getString("shift"));*/

                                /*colorList = new ArrayList<>();
                                objectList = new ArrayList<>();
                                shiftList = new ArrayList<>();*/
                                //Log.d("JSON_ITEM", "ID: " + item.getString("profile_url"));
                                //dataList.add(item.getString("firstname") + " " + item.getString("middlename") +  item.getString("lastname"));
                                //emailList.add(item.getString("email"));
                                //idList.add(item.getString("id"));
                                //imgURLList.add(ConnectionFile.returnURLRaw() + item.getString("profile_url"));
                          /*  }

                           // adapter = new UpcommingYeasterdayAdapter(colorList, objectList, fromList, toList, shiftList);
                           // recyclerView.setAdapter(adapter);
                            //searchBar.setOnClickListener(v -> searchView.show());*/
                            //adapter.filter(query.toString());

                       /* }/*else{
                            JSONArray jsonArrayData = (JSONArray) dataObject;

                            Log.d("JSON", "Data is an array with " + jsonArrayData.length() +" elements.");

                        }*/


                    /*} catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                                             // Handle invalid JSON
                    }*/
                /*} else {
                                         //Toast.makeText(requireContext(), "Error in success response", Toast.LENGTH_SHORT).show();
                }*/
            /*}

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });*/





        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_employees, R.id.nav_statistics, R.id.nav_offers, R.id.nav_calendar, R.id.nav_options, R.id.nav_chat_menu, R.id.nav_my_shifts, R.id.nav_personal_stats)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
       // NavController setInvisible = Navigation.findNavController(this, R.id.nav_options);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
            Menu menu = navigationView.getMenu();

            // Get specific menu items
            //MenuItem employeesItem = menu.findItem(R.id.nav_employees);
            //MenuItem statisticsItem = menu.findItem(R.id.nav_statistics);
            MenuItem optionsItem = menu.findItem(R.id.nav_options);
            if (sharedPreferences.getString("role", "").equals("parttime")) {
                optionsItem.setVisible(true);

            }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.primaryOrganizationColor));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.: // Replace with your menu ID
                clearSharedPreferences(); // Call the method to clear preferences
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_left, menu);  // Replace with your menu file
        super.onCreateOptionsMenu(menu, inflater);
    }*/
    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true); // Enable menu for this fragment
    }*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_profile) {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_settings) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            clearSharedPreferences(); // Call log out method
            return true;
        }

        return false;
    }
    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.rp_android.app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // This clears all the data
        editor.apply(); // Always use apply for async operation
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
    private void changeText(){





    }

}