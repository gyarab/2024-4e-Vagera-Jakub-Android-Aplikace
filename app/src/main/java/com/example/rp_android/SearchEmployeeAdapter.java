package com.example.rp_android;


import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rp_android.databinding.FragmentHomeBinding;
import com.example.rp_android.ui.employee_detail.EmployeeDetailFragment;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.bumptech.glide.Glide;

public class SearchEmployeeAdapter extends RecyclerView.Adapter<SearchEmployeeAdapter.ViewHolder> {

    private List<String> dataList;
    private List<String> emailList;
    private List<String> imgURLList;

    private List<String> idList;


    private List<String> filteredList;
    private List<String> filteredEmail;
    private List<String> filteredImgURL;
    private List<String> filteredId;
    private List<String> filteredPosition;

    private List<String> filteredColor;


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String id);
            /*Fragment newFragment = new EmployeeDetailFragment(); // Your target fragment

            Bundle bundle = new Bundle();
            bundle.putString("item_key", item); // Pass data to new fragment
            newFragment.setArguments(bundle);

            // Replace fragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, newFragment)
                    .addToBackStack(null)
                    .commit();*/


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public SearchEmployeeAdapter(List<String> dataList, List<String> emailList, List<String> imgURLList, List<String> idList, List<String> positionList, List<String> colorList, OnItemClickListener listener) {
        /*this.dataList = dataList;
        this.emailList = emailList;
        this.idList = idList;
        this.imgURLList = imgURLList;*/
        this.listener = listener;
        this.filteredList = new ArrayList<>(dataList);
        this.filteredEmail = new ArrayList<>(emailList);
        this.filteredId = new ArrayList<>(idList);
        this.filteredImgURL = new ArrayList<>(imgURLList);// Copy of original data
        this.filteredPosition = new ArrayList<>(positionList);
        this.filteredColor = new ArrayList<>(colorList);

    }

    public void filter(String query) {
        /*filteredList.clear();
        filteredEmail.clear();
        filteredImgURL.clear();
        filteredId.clear();
        if (query.isEmpty()) {
            filteredList.addAll(dataList);
            filteredEmail.addAll(emailList);
            filteredImgURL.addAll(imgURLList);
            filteredId.addAll(idList);
        } else {
            for (String item : dataList) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();*/
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(filteredList.get(position));
        holder.emailText.setText(filteredEmail.get(position));
        String selectID = filteredId.get(position);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(selectID));
        holder.positionButton.setText(filteredPosition.get(position));
        holder.positionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor( filteredColor.get(position) )));
        Log.e("---" , filteredImgURL.get(position));
        Log.e("---" , selectID);

        Glide.with(holder.itemView.getContext())
                .load(filteredImgURL.get(position))
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.profileImage);
       // ConnectionFile connection = new ConnectionFile();
        /*URL url = null;
        try {
            url = new URL(filteredImgURL.get(position));
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

                runOnUiThread(() -> holder.profileImage.setImageBitmap(image)); // Update UI on main thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();*/
        //holder.profileImage.set(filteredEmail.get(position));

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView emailText;
        ImageView profileImage;
        Button positionButton;



        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            emailText = itemView.findViewById(R.id.emailText);
            profileImage = itemView.findViewById(R.id.profileImage);
            positionButton = itemView.findViewById(R.id.positionButton);
        }
    }
}