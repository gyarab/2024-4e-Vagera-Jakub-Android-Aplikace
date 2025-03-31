package com.example.rp_android.ui.employees;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.api_routes.ApiLoginService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.R;
import com.example.rp_android.SearchEmployeeAdapter;
import com.example.rp_android.SearchModel;
import com.example.rp_android.databinding.FragmentEmployeesBinding;
import com.example.rp_android.ui.employee_detail.EmployeeDetailFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeesFragment#} factory method to
 * create an instance of this fragment.
 */
public class EmployeesFragment extends Fragment {

    private FragmentEmployeesBinding binding;
    private SearchView searchView;
    SharedPreferences sharedPreferences;

    private SearchBar searchBar;
    //private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchEmployeeAdapter adapter;
    private List<String> dataList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();

    private List<String> imgURLList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();
    private List<String> positionList = new ArrayList<>();
    private List<String> colorList = new ArrayList<>();
    private int intAdmin;
    private int intManager;
    private int intFullTime;
    private int intPartTime;
    private String searchText;



    private static Retrofit retrofit = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmployeesViewModel EmployeesViewModel =
                new ViewModelProvider(this).get(EmployeesViewModel.class);

        binding = FragmentEmployeesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchView = binding.searchView;
        searchBar = binding.searchBar;
        recyclerView = binding.recycleViewEmployees;


        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);

        adapter = new SearchEmployeeAdapter(dataList,emailList, imgURLList,idList,positionList,colorList,  (id) -> openNewFragment("1"));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        searchBar.setOnClickListener(v -> searchView.show());


        searchView.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView
                searchText = s.toString();

                searchEmployee();

            }

            @Override
            public void afterTextChanged(Editable s) {}

        });

        // Close SearchView on back press

        MaterialButtonToggleGroup toggleGroup = binding.toggleButtonGroup;

        toggleGroup.addOnButtonCheckedListener(
                new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                        if (isChecked) {
                            if (checkedId == binding.buttonAdmin.getId()) {
                                binding.buttonAdmin.setIconResource(R.drawable.baseline_done_24);
                                binding.buttonAdmin.setPadding(30,0,10,0);
                                intAdmin = 1;
                                //binding.buttonAdmin.setCompoundDrawablePadding(0);
                                //binding.buttonAdmin.icon
                            } else if (checkedId == binding.buttonManger.getId()) {
                                binding.buttonManger.setIconResource(R.drawable.baseline_done_24);
                                binding.buttonManger.setPadding(30,0,40,0);
                                intManager = 1;

                            } else if (checkedId == binding.buttonFull.getId()) {
                                binding.buttonFull.setIconResource(R.drawable.baseline_done_24);
                                binding.buttonFull.setPadding(30,0,40,0);
                                intFullTime = 1;

                            } else if (checkedId == binding.buttonPart.getId()) {
                                binding.buttonPart.setIconResource(R.drawable.baseline_done_24);
                                binding.buttonPart.setPadding(30,0,40,0);
                                intPartTime = 1;

                            }
                            searchEmployee();

                        }else{
                            MaterialButton button = group.findViewById(checkedId);
                            if (checkedId == binding.buttonManger.getId()) {
                                binding.buttonManger.setPadding(60,0,60,0);
                                intManager = 0;


                            }else if (checkedId == binding.buttonFull.getId()) {
                                binding.buttonFull.setPadding(60,0,60,0);
                                intFullTime = 0;


                            } else if (checkedId == binding.buttonPart.getId()) {
                                binding.buttonPart.setPadding(60,0,60,0);
                                intPartTime = 0;


                            } else if (checkedId == binding.buttonAdmin.getId()) {
                                intAdmin = 0;

                                //binding.buttonPart.setPadding(60,0,60,0);

                            }
                            searchEmployee();
                            button.setIcon(null);
                        }
                    }
                });

        return root;
    }

    private void openNewFragment(String id) {
        Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.profile);

    }

    private void searchEmployee() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        String token = "Bearer " + sharedPreferences.getString("plainToken", "");
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiDataService searchEmployee = retrofit.create(ApiDataService.class);
        SearchModel searchModel = new SearchModel(searchText, intAdmin, intManager, intFullTime, intPartTime);
        String token = sharedPreferences.getString("token", "");
        Call<ResponseBody> callEmployee = searchEmployee.searchEmployee("Bearer "+ sharedPreferences.getString("plainToken", ""), searchModel);
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
                            dataList = new ArrayList<>();
                            emailList = new ArrayList<>();
                            imgURLList = new ArrayList<>();
                            idList = new ArrayList<>();
                            positionList = new ArrayList<>();
                            colorList = new ArrayList<>();

                            int passedId = 0;
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                JSONObject item = jsonArrayData.getJSONObject(i);
                                Log.d("JSON_ITEM", "ID: " + item.getString("profile_url"));
                                dataList.add(item.getString("firstname") + " " + item.getString("middlename") +  item.getString("lastname"));
                                emailList.add(item.getString("email"));
                                idList.add(item.getString("id"));
                                //positionList.add(item.getString("position"));
                                if(item.getString("position").equals("manager")){
                                    positionList.add("M");
                                    colorList.add("#dc3545");
                                } else if (item.getString("position").equals("fulltime")) {
                                    positionList.add("F");
                                    colorList.add("#0d6efd");
                                } else if (item.getString("position").equals("parttime")) {
                                    positionList.add("P");
                                    colorList.add("#198754");
                                }else if (item.getString("position").equals("admin")) {
                                    positionList.add("A");
                                    colorList.add("#000000");
                                } else{
                                    positionList.add("/");
                                    colorList.add("#000000");

                                }
                                imgURLList.add(ConnectionFile.returnURLRaw() + item.getString("profile_url"));
                            }
                            adapter = new SearchEmployeeAdapter(dataList, emailList, imgURLList, idList, positionList, colorList,  this::openNewFragment);
                            recyclerView.setAdapter(adapter);
                            searchBar.setOnClickListener(v -> searchView.show());
                            //adapter.filter(query.toString());

                        }


                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                        // Handle invalid JSON
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
            private void openNewFragment(String employeeId) {
                Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment
                /*NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_employee_detail);
                navController.navigate(R.id.fragment_employee_detail);*/
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("employee_id", employeeId);
                Log.e("5555", employeeId);// Passing Integer
                navController.navigate(R.id.nav_employee_detail, bundle);
                /*Bundle bundle = new Bundle();
                bundle.putString("item_key", item); // Pass data to new fragment
                newFragment.setArguments(bundle);*/

                // Replace fragment
                /*getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_employee_detail, newFragment)
                        .addToBackStack(null)
                        .commit();*/
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void addData(String newItem) {
        dataList.add(newItem);
        adapter.notifyDataSetChanged(); // Refresh ListView
    }


}