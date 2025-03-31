package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.R;
import com.example.rp_android.models.AssignmentModel;
import com.example.rp_android.models.StatisticModel;

import java.util.List;
import java.util.Objects;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    /**
     * Soubor načítá prirazene smeny v RecycleView
     */




    private List<AssignmentModel> dataList;
    private AssignmentAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;





    public interface OnItemClickListener {


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public AssignmentAdapter(List<AssignmentModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }


    @NonNull
    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.object_header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.assignment_item, parent, false);
        }
        return new AssignmentAdapter.ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(dataList.get(position).getId(), 0)) {
            return 1; // nactení objektu
        } else {
            return 0; // nactení pridelene smeny
        }
    }



    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.ViewHolder holder, int position) {
        AssignmentModel item = dataList.get(position);
        holder.nameText.setText(item.getName());
        holder.iconView.setImageDrawable(item.getIcon());
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30f);
        gradientDrawable.setColor(Color.parseColor(item.getColor()));

        holder.nameText.setBackground(gradientDrawable);

    }

    @Override
    public int getItemCount() {

        return dataList.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView scheduleText;
        ImageView iconView;
        ImageView sideImageView;
        TextView shiftObject;
        TextView logText;
        TextView loginTime;
        TextView logoutTime;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            scheduleText = itemView.findViewById(R.id.scheduleText);
            iconView = itemView.findViewById(R.id.iconView);
            sideImageView = itemView.findViewById(R.id.sideImageView);
            shiftObject = itemView.findViewById(R.id.shiftObject);
            logText = itemView.findViewById(R.id.logText);
            loginTime = itemView.findViewById(R.id.loginTime);
            logoutTime = itemView.findViewById(R.id.logoutTime);

        }
    }
}
