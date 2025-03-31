package com.example.rp_android.ui.chat_menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.SearchEmployeeAdapter;
import com.example.rp_android.SearchModel;
import com.example.rp_android.api_routes.ApiChatService;
import com.example.rp_android.api_routes.ApiDataService;
import com.example.rp_android.api_routes.ApiLoginService;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.R;
import com.example.rp_android.adapters.ChatContactAdapter;
import com.example.rp_android.adapters.ChatFavoriteAdapter;
import com.example.rp_android.databinding.FragmentChatMenuBinding;
import com.example.rp_android.models.ChatFavoriteModel;
import com.example.rp_android.models.ChatGetModel;
import com.example.rp_android.models.ContactModel;
import com.example.rp_android.ui.employee_detail.EmployeeDetailFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

public class ChatMenuFragment extends Fragment implements ChatFavoriteAdapter.OnItemClickListener, ChatContactAdapter.OnItemClickListener  {

    private FragmentChatMenuBinding binding;
    SharedPreferences sharedPreferences;
    private ChatFavoriteAdapter adapter;
    private ChatContactAdapter adapterContact;

    private List<ChatFavoriteModel> chatFavoriteModel;
    private List<ContactModel> chatConntactmodel;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewConntact;
    private int intAdmin;
    private int intManager;
    private int intFullTime;
    private int intPartTime;
    private Retrofit retrofit;
    private List<String> dataList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();

    private List<String> imgURLList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();
    private List<String> positionList = new ArrayList<>();
    private List<String> colorList = new ArrayList<>();
    private SearchEmployeeAdapter adapterSearch;
    private String searchText;
    private SearchView searchView;
    private SearchBar searchBar;
    private RecyclerView recycleViewEmployees;
    private LinearLayout personalLayout;






    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatMenuViewModel chatMenuViewModel =
                new ViewModelProvider(this).get(ChatMenuViewModel.class);

        binding = FragmentChatMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireContext().getSharedPreferences("com.rp_android.app", Context.MODE_PRIVATE);



        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionFile.returnURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiChatService apiChatService = retrofit.create(ApiChatService.class);

        int idUser = sharedPreferences.getInt("id", 0);
        chatFavoriteModel = new ArrayList<>();
        chatConntactmodel = new ArrayList<>();
        searchView = binding.searchView;
        searchBar = binding.searchBar;
        recyclerView = binding.favoriteRecyclerView;
        recyclerViewConntact = binding.contactRecyclerView;
        recycleViewEmployees = binding.recycleViewEmployees;
        personalLayout = binding.personalLayout;


        adapterSearch = new SearchEmployeeAdapter(dataList,emailList, imgURLList,idList,positionList,colorList,  (id) -> openNewFragment("1"));
        recycleViewEmployees.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycleViewEmployees.setAdapter(adapterSearch);
        searchBar.setOnClickListener(v -> searchView.show());

        searchView.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                recycleViewEmployees.setVisibility(View.VISIBLE); // Show RecyclerView
                searchText = s.toString();

                searchEmployee();

            }

            @Override
            public void afterTextChanged(Editable s) {}

        });
        personalLayout.setOnClickListener(v -> {
                openNewFragment(sharedPreferences.getInt("id", 0));
        }
        );

        ChatGetModel chatGetModel = new ChatGetModel(idUser);
        Call<ResponseBody> callFavorite = apiChatService.chatifyFavorite("Bearer "+sharedPreferences.getString("plainToken", ""), chatGetModel);
        callFavorite.enqueue(new Callback<ResponseBody>() {
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
                            Object dataObject = jsonObject.get("favorites");
                            if (dataObject instanceof JSONObject) {
                                JSONObject jsonObjectData = (JSONObject) dataObject;
                                Log.d("JSON", "Data is an object: " + jsonObjectData.toString());
                            } else if (dataObject instanceof JSONArray) {
                                JSONArray jsonArrayData = (JSONArray) dataObject;

                                Log.d("JSON", "Data is an array with " + jsonArrayData.length() + " elements. ");
                                if (jsonArrayData.length() != 0) {
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        JSONObject item = jsonArrayData.getJSONObject(i);
                                        JSONObject userObject = item.getJSONObject("user");
                                            Log.e("++", String.valueOf(item.getInt("favorite_id")));
                                            chatFavoriteModel.add(new ChatFavoriteModel(item.getInt("favorite_id"),userObject.getString("first_name") + " " + userObject.getString("middle_name") + " " + userObject.getString("last_name") ,ConnectionFile.returnURLRaw() + item.getString("image")));

                                    }
                                }

                                adapter = new ChatFavoriteAdapter(chatFavoriteModel,
                                        ChatMenuFragment.this,
                                        getParentFragmentManager(),
                                        requireContext()
                                );
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
        Call<ResponseBody> callContacts = apiChatService.chatifyGetContacts("Bearer "+ sharedPreferences.getString("plainToken", ""), chatGetModel);
        callContacts.enqueue(new Callback<ResponseBody>() {
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
                            if (jsonArrayData.length() != 0) {
                                for (int i = jsonArrayData.length()-1; i > -1 ; i--) {
                                    JSONObject item = jsonArrayData.getJSONObject(i);
                                    JSONArray userArray = item.getJSONArray("innerdata");
                                    if (userArray.length() != 0) {

                                        for (int x = 0; x < userArray.length(); x++) {
                                            JSONObject user = userArray.getJSONObject(x);
                                            String dateTimeString = user.getString("created_at");
                                            long timestamp = (System.currentTimeMillis()-convertStringToTimestamp(dateTimeString))/1000;


                                            String who = "You";
                                            if(item.getInt("who") == 0){
                                                who ="You : ";
                                            }else{
                                                who = user.getString("last_name")+ " : ";
                                            }
                                            int visibility = View.GONE;
                                            if(item.getInt("count") > 0){
                                                visibility = View.VISIBLE;
                                            }

                                            chatConntactmodel.add(new ContactModel(user.getInt("id_user"),who,ConnectionFile.returnURLRaw()+ item.getString("image"),
                                                    user.getString("first_name") + " " + user.getString("middle_name") + " " + user.getString("last_name"),
                                                    convertSeconds(timestamp),user.getString("body"),item.getInt("count") ,visibility ));

                                        }

                                    }

                                }
                            }
                            adapterContact = new ChatContactAdapter(chatConntactmodel,
                                    ChatMenuFragment.this,
                                    getParentFragmentManager(),
                                    requireContext()
                            );
                            recyclerViewConntact.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerViewConntact.setAdapter(adapterContact);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });


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

                            }
                            searchEmployee();
                            button.setIcon(null);
                        }
                    }
                });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void openChat(int position, int id) {
        openNewFragment(id);
    }

    @Override
    public void openMessage(int position, int id) {
        openNewFragment(id);

    }
    private void openNewFragment(String id) {
        Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.profile);

    }
    public static String convertSeconds(long seconds) {
        if (seconds < 60) {
            return seconds + "s ago" ;
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "m ago" ;
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "h ago" ;
        }

        long days = hours / 24;
        if (days < 7) {
            return days + "d ago" ;
        }

        long weeks = days / 7;
        if (weeks < 52) {
            return weeks + "w ago" ;
        }

        long years = weeks / 52;
        return years + "y ago" ;
    }
    public static long convertStringToTimestamp(String dateTimeString) {
        try {
            // Define the format of your input string
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            }

            // Parse the string into a LocalDateTime object
            LocalDateTime localDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localDateTime = LocalDateTime.parse(dateTimeString, formatter);
            }

            long timestamp = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            }

            return timestamp;

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Or handle the error appropriately
        }
    }
    private void openNewFragment(Integer employeeId) {
        Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment
        NavController navController = Navigation.findNavController(requireView());
        Bundle bundle = new Bundle();
        bundle.putInt("employee_id", employeeId);
        navController.navigate(R.id.nav_chat_detail, bundle);

    }
    private void searchEmployee() {

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
                            adapterSearch = new SearchEmployeeAdapter(dataList, emailList, imgURLList, idList, positionList, colorList,  this::openNewFragment);
                            recycleViewEmployees.setAdapter(adapterSearch);
                            searchBar.setOnClickListener(v -> searchView.show());

                        }


                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON format: " + e.getMessage());
                    }
                } else {
                    Toast.makeText(requireContext(), "Error in success response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(requireContext(), "Error: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            private void openNewFragment(String employeeId) {
                Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment

                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putInt("employee_id", Integer.parseInt(employeeId));
                navController.navigate(R.id.nav_chat_detail, bundle);

            }
        });
    }
}
