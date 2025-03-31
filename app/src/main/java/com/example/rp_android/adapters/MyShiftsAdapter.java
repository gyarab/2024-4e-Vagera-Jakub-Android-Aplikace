package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.R;
import com.example.rp_android.models.MyShiftsModel;

import java.util.List;

/**
 * Soubor nacita konkretni smeny prihlaseneho uzivatele v konkretnim dni
 */
public class MyShiftsAdapter extends RecyclerView.Adapter<MyShiftsAdapter.ViewHolder> implements CalendarMyShiftsAdapter.OnItemClickListener {

    private String pasteFrom;
    private String pasteTo;
    private String timeSelectedFrom;

    private List<MyShiftsModel> dataList;
    private MyShiftsAdapter.OnItemClickListener listener;
    private Context context;
    private FragmentManager fragmentManager;


    public interface OnItemClickListener {

        void toolTipComment(int position, String comment, Context context, View v);

        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public MyShiftsAdapter(List<MyShiftsModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;

    }


    @NonNull
    @Override
    public MyShiftsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_shifts_item, parent, false);
        return new MyShiftsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyShiftsAdapter.ViewHolder holder, int position) {
        MyShiftsModel item = dataList.get(position);
        holder.fromText.setText("("+item.getFrom()+")");
        holder.toText.setText("("+item.getTo()+")");
        holder.nameText.setText(item.getName());
        holder.shiftText.setText(item.getShift());
        holder.sideImageView.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.commentIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.toolTipComment(position, item.getComment(), v.getContext(), v);
            }
        });
    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }

    public void updateItem(int position, String from, String to) {
        pasteFrom = from;
        pasteTo = to;
        notifyItemChanged(position);
    }
    public void updateFromPicker(int position, String time, int month, int year, int id) {

        if (position >= 0 && position < dataList.size()) {

        }
        notifyItemChanged(position);

    }
    public void updateToPicker(int position, String time, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void copyTime(int position, String from, String to, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void allTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);
    }
    public void updateAPI(int position, String from, String to ) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }
    public void deleteTime(int position, int month, int year, int id) {
        if (position >= 0 && position < dataList.size()) {

            notifyItemChanged(position);
        }
        notifyItemChanged(position);

    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromText;
        TextView toText;
        TextView nameText;
        TextView shiftText;



        ImageView profileImage;
        ImageView sideImageView;
        ImageView commentIcon;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            nameText = itemView.findViewById(R.id.nameText);
            shiftText = itemView.findViewById(R.id.shiftText);
            profileImage = itemView.findViewById(R.id.profileImage);
            sideImageView = itemView.findViewById(R.id.sideImageView);
            commentIcon = itemView.findViewById(R.id.commentIcon);

        }
    }
}

