package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.R;
import com.example.rp_android.models.ChatFavoriteModel;
import com.example.rp_android.models.StatisticModel;

import java.util.List;

/**
 * Soubor nacita recycleView tabulku statistik
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder>{



    private List<StatisticModel> dataList;
    private StatisticsAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;



    public interface OnItemClickListener {
        void openDialog(int position);


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public StatisticsAdapter(List<StatisticModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }


    @NonNull
    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false);
        return new StatisticsAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull StatisticsAdapter.ViewHolder holder, int position) {
        StatisticModel item = dataList.get(position);
        holder.dateText.setText(item.getDate());
        holder.scheduleText.setText(item.getScheduled());
        holder.logText.setText(item.getLog());
        holder.loginTime.setText(item.getLogInTime());
        holder.logoutTime.setText(item.getLogOutTime());
        holder.shiftObject.setText(item.getObject() + " - "+ item.getShift());
        holder.sideImageView.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.objectImage.setImageDrawable(item.getSubIcon());
        holder.infoIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.openDialog(position);
            }
        });


    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }


    public void changeWaiting(int position) {

        notifyItemChanged(position);


    }







    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView scheduleText;
        ImageView objectImage;
        ImageView sideImageView;
        ImageView infoIcon;
        TextView shiftObject;
        TextView logText;
        TextView loginTime;
        TextView logoutTime;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            scheduleText = itemView.findViewById(R.id.scheduleText);
            objectImage = itemView.findViewById(R.id.objectImage);
            sideImageView = itemView.findViewById(R.id.sideImageView);
            shiftObject = itemView.findViewById(R.id.shiftObject);
            logText = itemView.findViewById(R.id.logText);
            loginTime = itemView.findViewById(R.id.loginTime);
            logoutTime = itemView.findViewById(R.id.logoutTime);
            infoIcon = itemView.findViewById(R.id.infoIcon);

        }
    }

}