package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
//import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rp_android.R;
import com.example.rp_android.models.OffersModel;

import java.util.List;

/**
 * Soubor nacita nabidky smen v RecycleView
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> implements CalendarOffersAdapter.OnItemClickListener {

    private String pasteFrom;
    private String pasteTo;
    private String timeSelectedFrom;

    private List<OffersModel> dataList;
    private OfferAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;

    @Override
    public void pasteClick(int position) {

    }

    @Override
    public void openFromPicker(int position) {

    }

    @Override
    public void openToPicker(int position) {

    }

    @Override
    public void deletePicker(int position) {

    }

    @Override
    public void copyTime(String from, String to) {

    }

    @Override
    public void pasteTime(int position) {

    }

    @Override
    public void allDay(int position) {

    }


    public interface OnItemClickListener {


        //void pasteTime(int position);
        void toolTipInfo(int position, String user, String time, Context context, View v);
        void toolTipComment(int position, String comment, Context context, View v);
        void buttonClick(int position, int id, int status, List<OffersModel> dataList, int parent);


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public OfferAdapter(List<OffersModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context, Integer parent ) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.parent = parent;


    }


    @NonNull
    @Override
    public OfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item, parent, false);
        return new OfferAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.ViewHolder holder, int position) {
        OffersModel item = dataList.get(position);

        holder.title.setText(item.getObject() + " - " + item.getShift());
        holder.timeText.setText(item.getFrom() + " - " + item.getTo());
        holder.sideImageView.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.commentIcon.setImageDrawable(item.getCommentStatus());
        holder.statusButton.setVisibility(item.getVisibility());
        holder.statusButton.setBackgroundColor(Color.parseColor(item.getButtonColor()));
        holder.statusButton.setText(item.getButtonText());

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ViewCompat.setTooltipText(holder.timeText, "This is item: " + "text");
        } else {
            // API 24-25: Fallback to Toast
            holder.timeText.setOnLongClickListener(v -> {
                Toast.makeText(context, "This is item: " + "text", Toast.LENGTH_SHORT).show();
                return true;
            });
        }*/
        /*holder.timeText.setOnLongClickListener(v -> {
            showTooltip(v, "This is item: " + "text");
            return true;
        });*/

        /*holder.timeText.setOnLongClickListener(v -> {
            TooltipHandler.showTooltip(v.getContext(), v, "Offered by: " + "text");
            return true;
        });*/
        holder.infoIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.toolTipInfo(position, item.getCreatedBy(), item.getCreated_at(), v.getContext(), v);
            }
        });
        holder.statusButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.buttonClick(position, item.getId() , item.getStatus(), dataList, parent);
            }
        });
        holder.commentIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.toolTipComment(position, item.getComment(), v.getContext(), v);
            }
        });
        //ViewCompat.setTooltipText(holder.timeText, "This is item: " +item.getTo());
        /*holder.fromText.setText(item.getFrom());
        holder.toText.setText(item.getTo());
        holder.nameText.setText(item.getName());
        holder.shiftText.setText(item.getShift());
        holder.sideImageView.setBackgroundColor(Color.parseColor(item.getColor()));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageURL())
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.profileImage);*/
        //holder.sideImageView.setBackground(item.getShift());

        //Log.e("[[[", String.valueOf(position));
        //holder.MMM.setText("----");
        /*holder.monthText.setText(item.getWeek());
        holder.vacationImageView.setImageDrawable(item.getVacationStatus());

        holder.workImageView.setImageDrawable(item.getWorkStatus());

        holder.holidayImageView.setImageDrawable(item.getHolidayStatus());*/
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

    public void changeWaiting(int position) {
            notifyItemChanged(position);

    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView timeText;


        TextView fromText;
        TextView toText;
        TextView nameText;
        TextView shiftText;



        ImageView profileImage;
        View sideImageView;

        ImageView infoIcon;
        ImageView commentIcon;

        Button statusButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            timeText = itemView.findViewById(R.id.time);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            nameText = itemView.findViewById(R.id.nameText);
            shiftText = itemView.findViewById(R.id.shiftText);
            profileImage = itemView.findViewById(R.id.profileImage);
            sideImageView = itemView.findViewById(R.id.sideImageView);
            infoIcon = itemView.findViewById(R.id.infoIcon);
            commentIcon = itemView.findViewById(R.id.commentIcon);
            statusButton = itemView.findViewById(R.id.statusButton);

        }
    }

}