package com.example.rp_android.ui.employee_detail;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.adapters.AssignmentAdapter;
import com.example.rp_android.adapters.ChatMessageAdapter;
import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.R;
import com.example.rp_android.adapters.SpinnerAdapter;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.databinding.FragmentEmployeeDetailBinding;
import com.example.rp_android.models.AssignmentModel;
import com.example.rp_android.models.DetailModel;
import com.example.rp_android.models.ObjectModel;
import com.example.rp_android.models.SpinnerModel;
import com.example.rp_android.ui.chat_detail.ChatDetailFragment;

/*public class EmployeeDetailFragment {
}*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeDetailFragment extends Fragment implements AssignmentAdapter.OnItemClickListener {

    private FragmentEmployeeDetailBinding binding;
    SharedPreferences sharedPreferences;
    private static Retrofit retrofit = null;
    private String employeeId;
    private TextView nameText;
    private TextView bioText;
    private TextView roleText;
    private TextView phoneText;
    private TextView emailText;
    private TextView usernameText;

    private TextView joinedText;
    private ImageView profileImage;
    private AssignmentAdapter adapter;


    private List<String> selectName = new ArrayList<>();
    private List<SpinnerModel> spinnerModel = new ArrayList<>();
    private List<AssignmentModel> assignmentModel = new ArrayList<>();

    private RecyclerView recyclerView;

    private String phone;
    private String email;
    private String firstname;
    private String middlename;
    private String lastname;
    private String bio;
    private String role;
    private String joined;
    private String image;
    private String username;
    private String phoneNumber;
    private String phoneCode;


    private Spinner spinner;
    private LinearLayout noAssignmentDiv;










    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmployeeDetailViewModel galleryViewModel =
                new ViewModelProvider(this).get(EmployeeDetailViewModel.class);

        binding = FragmentEmployeeDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        spinner = binding.spinnerObject;
        recyclerView = binding.assignmentRecycleView;
        noAssignmentDiv = binding.noAssignmentDiv;
        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);

        /*String receivedData = getArguments() != null ? getArguments().getString("item_key") : "No Data";
        textView.setText(receivedData);*/
        if (getArguments() != null) {
            //String employeeName = getArguments().getString("employee_name");
            employeeId = getArguments().getString("employee_id");

            Log.e("EmployeeDetail", "Name:  ID: " + employeeId);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getDetail = retrofit.create(ApiDataService.class);
        DetailModel detailModel = new DetailModel( employeeId);
        String token = sharedPreferences.getString("token", "");
        Call<ResponseBody> callEmployee = getDetail.postEmployeeDetail("Bearer "+ sharedPreferences.getString("plainToken", ""), detailModel);
        callEmployee.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("ResourceAsColor")
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

                    Log.e("bodyssssss", response.message());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        email = jsonObject.getString("email");
                        firstname = jsonObject.getString("firstname");
                        middlename = jsonObject.getString("middlename");
                        lastname = jsonObject.getString("lastname");
                        role = jsonObject.getString("role");
                        username = jsonObject.getString("username");
                        image = jsonObject.getString("profile_url");
                        if(Objects.equals(jsonObject.getString("phone_number"), null)){
                            phoneNumber = "";

                        }else{
                            phoneNumber = jsonObject.getString("phone_number");

                        }
                        phoneCode= jsonObject.getString("phone_code");
                        bio = jsonObject.getString("bio");
                        joined = jsonObject.getString("joined");

                        //StringBuilder buffer = new StringBuilder();
                        //buffer = new StringBuilder(jsonObject.getString("bio"));

                        nameText = binding.nameText;
                        phoneText = binding.phoneText;
                        emailText = binding.emailText;
                        joinedText = binding.joinedText;
                        usernameText = binding.usernameText;
                        profileImage = binding.profileImage;
                        roleText = binding.roleText;


                        bioText = binding.bioText;
                        bioText.setText(bio);
                        emailText.setText(email);
                        usernameText.setText(username);
                        joinedText.setText("Joined: " +joined.substring(0,10));
                        Log.e("role", role);
                        GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                        gradientDrawable.setCornerRadius(20f);
                        if(Objects.equals(role, "admin")){
                            roleText.setText("Administator");
                            //roleText.setBackgroundColor(Color.parseColor("#FF000000"));

                            gradientDrawable.setColor(Color.parseColor("#FF000000"));

                            roleText.setBackground(gradientDrawable);

                        }else if(Objects.equals(role, "manager")){
                            roleText.setText("Manager");
                            //roleText.setBackgroundColor(Color.parseColor("#dc3545"));

                            gradientDrawable.setColor(Color.parseColor("#dc3545"));

                            roleText.setBackground(gradientDrawable);

                        }else if(Objects.equals(role, "fulltime")){
                            roleText.setText("Full-time");
                            //roleText.setBackgroundColor(Color.parseColor("#0d6efd"));

                            gradientDrawable.setColor(Color.parseColor("#0d6efd"));

                            roleText.setBackground(gradientDrawable);
                        }else{
                            roleText.setText("Part-time");
                            //roleText.setBackgroundColor(Color.parseColor("#198754"));

                            gradientDrawable.setColor(Color.parseColor("#198754"));

                            roleText.setBackground(gradientDrawable);
                        }
                        spinnerLoader(sharedPreferences.getString("plainToken", ""));
                        /*URL url;
                        String BaseConnection = ConnectionFile.returnURLRaw() ;

                        try {
                            url = new URL(ConnectionFile.returnURLRaw() +image);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        URLConnection ucon = null;
                        try {
                            ucon = url.openConnection();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        URLConnection finalUcon = ucon;*/
                       Glide.with(requireContext()).load(ConnectionFile.returnURLRaw() + image).into(binding.profileImage);

                       /* new Thread(() -> {
                            try {
                                InputStream inputStream = finalUcon.getInputStream();
                                Bitmap imageBit = BitmapFactory.decodeStream(inputStream);
                                inputStream.close();
                               Glide.with(this).load(imageUrl).into(profileImage);
                                onStart(() -> profileImage.setImageBitmap(imageBit)); // Update UI on main thread
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();*/

                        nameText.setText(firstname + " "+ middlename + " " + lastname);
                        phoneText.setText("(+" +phoneCode+ ") " + phoneNumber );

                    /*}else if(finalX == 1){
                        recyclerView = binding.recycleViewToday;
                    }else{
                        recyclerView = binding.recycleViewTommorow;
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));*/
                        //String ds = jsonObject.getString("id");
                        Log.e("qwe", email);



                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                      //  // Handle invalid JSON
                    }
                } else {
                    Toast.makeText(requireContext(), "Error in success response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("failled","---");
                Toast.makeText(requireContext(), "Error: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        /*final TextView textView = binding.textEmployeeDetail;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

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
                                spinnerModel = new ArrayList<>();
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
                                    spinnerModel.add(new SpinnerModel(item.getInt("id"), item.getString("name"), draw));

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
                                        SpinnerModel selectedItem = (SpinnerModel) parent.getItemAtPosition(position);

                                        int selectedId = selectedItem.getId();
                                        String selectedName = selectedItem.getName();
                                        Log.e("vvvv", String.valueOf(selectedId));
                                        loadAssignments(selectedId);

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


    public void loadAssignments(int id_object){
        Retrofit retrofitAssignment = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService getAssignments = retrofitAssignment.create(ApiDataService.class);
        ObjectModel model = new ObjectModel(id_object);

        Call<ResponseBody> callAssignments = getAssignments.getAssignments("Bearer "+ sharedPreferences.getString("plainToken", ""), model);
        callAssignments.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res", response.toString());
                if (response.isSuccessful() && response.body() != null) {
                    noAssignmentDiv.setVisibility(View.VISIBLE);

                    try {
                        // Convert response body to string
                        String res = response.body().string();
                        Log.d("API_RESPONSE", "Response: " + res);

                        // Convert to JSON
                        JSONObject jsonObject = new JSONObject(res);
                        assignmentModel = new ArrayList<>();
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
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    noAssignmentDiv.setVisibility(View.GONE);

                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    String icon = (item.getString("icon").substring(3)).replaceAll("-", "_");;
                                    int resourceId = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
                                    Drawable draw;
                                    if (resourceId != 0) {
                                        draw = ContextCompat.getDrawable(requireContext(), resourceId);
                                    }else{
                                        draw = ContextCompat.getDrawable(requireContext(), R.drawable.bi_house);
                                    }
                                    assignmentModel.add(new AssignmentModel(item.getInt("id"), item.getString("name"), item.getString("color"), draw));

                                }

                            }
                        }
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                adapter = new AssignmentAdapter(assignmentModel,
                        EmployeeDetailFragment.this,
                        getParentFragmentManager(),
                        requireContext()
                );

                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter( adapter);
                recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int totalHeight = 500;
                        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                            View view = recyclerView.getLayoutManager().findViewByPosition(i);
                            if (view != null) {
                                totalHeight += view.getHeight();
                            }
                        }
                        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                        params.height = totalHeight;
                        recyclerView.setLayoutParams(params);
                    }
                });
                //recyclerView.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

}