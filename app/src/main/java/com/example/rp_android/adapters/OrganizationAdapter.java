package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.example.rp_android.models.ORGModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {
    private List<ORGModel> dataList;
    private List<String> plannedFromList;
    private List<String> plannedToList;
    private List<String> logFromList;
    private List<String> logToList;
    private List<String> colorList;
    private List<String> nameList;
    private List<String> shiftList;
    private List<String> imageList;
    private List<String> statusList;





    private List<String> filteredDay;
    private List<String> filteredMonth;
    private List<String> filteredWeek;

    private List<String> filteredMonthName;

    private OrganizationAdapter.OnItemClickListener listener;
    private OrganizationAdapter.OnItemClickListener listener_paste;

    private FragmentManager fragmentManager;

    public interface OnItemClickListener {
        void onItemClick(String id);

        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public OrganizationAdapter(List<ORGModel> dataList , FragmentManager fragmentManager) {
        this.dataList = new ArrayList<>(dataList);
        this.fragmentManager = fragmentManager;

    }
    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(dataList.get(position).getId(), 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public OrganizationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.object_header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.organization_item, parent, false);
        }
        return new OrganizationAdapter.ViewHolder(view);
    }

    /**source: https://stackoverflow.com/questions/54819214/recyclerview-is-duplicating-items */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrganizationAdapter.ViewHolder holder, int position) {
        ORGModel item = dataList.get(position);

            holder.nameText.setText(item.getName());
            holder.iconView.setImageDrawable(item.getIcon());
            holder.shiftText.setText(item.getShift());
            holder.fromText.setText(item.getFrom() + " ");
            holder.toText.setText(item.getTo() + " ");
            holder.fromLogText.setText("(" + item.getLogFrom() + ")");
            holder.toLogText.setText("(" + item.getLogTo() + ")");
            holder.sideImageView.setBackgroundColor(Color.parseColor(item.getColor()));
            if(Objects.equals(item.getStatus(), "0")){
                holder.workLinearLayout.setBackgroundResource(R.drawable.ordinary_org_frame);
                setColor(holder, position, "#ECECEC");
            } else if (Objects.equals(item.getStatus(), "1")) {
                holder.workLinearLayout.setBackgroundResource(R.drawable.inactive_org_frame);
                setColor(holder, position, "#dc3545");
            }else if (Objects.equals(item.getStatus(), "2")) {
                holder.workLinearLayout.setBackgroundResource(R.drawable.warning_org_frame);
                setColor(holder, position, "#ffc107");
            }else{
                holder.workLinearLayout.setBackgroundResource(R.drawable.active_org_frame);
                setColor(holder, position, "#198754");
            }

            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .placeholder(R.drawable.border_profile)
                    .error(R.drawable.border_profile)
                    .into(holder.profileImg);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setColor(@NonNull OrganizationAdapter.ViewHolder holder, int position, String color){
        holder.fromText.setTextColor(Color.parseColor(color));
        holder.toText.setTextColor(Color.parseColor(color));
        holder.fromLogText.setTextColor(Color.parseColor(color));
        holder.toLogText.setTextColor(Color.parseColor(color));

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromText;
        TextView toText;
        TextView nameText;
        TextView fromLogText;
        TextView toLogText;
        TextView shiftText;
        TextView textObject;

        //Drawable warningFrame;

        ImageView profileImg;
        ImageView sideImageView;

        ImageView iconView;

        LinearLayout workLinearLayout;



        /*TextView monthText;
        TextView weekText;
        TextView fromTextView;
        TextView toTextView;

        TextView monthNameText;
        Button fromButton;
        Button toButton;
        Button deleteButton;
        Button copyButton;
        Button pasteButton;
        Button allButton;


        ImageView profileImage;*/


        public ViewHolder(View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            fromLogText = itemView.findViewById(R.id.fromLogText);
            toLogText = itemView.findViewById(R.id.toLogText);
            shiftText = itemView.findViewById(R.id.shiftText);
            nameText =  itemView.findViewById(R.id.nameText);
            profileImg = itemView.findViewById(R.id.profileImage);
            //textObject = itemView.findViewById(R.id.textObject);
            workLinearLayout =  itemView.findViewById(R.id.workLinearLayout);
            sideImageView = itemView.findViewById(R.id.sideImageView);
            iconView =  itemView.findViewById(R.id.iconView);
            //warningFrame =  itemView.findViewById(R.drawable.ordinary_org_frame);
            /*dateText = itemView.findViewById(R.id.optionsDateText);
            monthText = itemView.findViewById(R.id.optionsMonthText);
            fromButton = itemView.findViewById(R.id.fromButton);
            toButton = itemView.findViewById(R.id.toButton);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);*/
            // weekText = itemView.findViewById(R.id.optionsWeekText);
        }
    }
}

