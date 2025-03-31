package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.R;
import com.example.rp_android.models.ContactModel;


import java.util.List;

/**
 * Soubor slouzi pro nacteni contaktu uzivatele v chatu pres RecycleView
 */
public class ChatContactAdapter extends RecyclerView.Adapter<ChatContactAdapter.ViewHolder>{


    private List<ContactModel> dataList;
    private ChatContactAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;



    public interface OnItemClickListener {
        void openMessage(int position, int id);


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public ChatContactAdapter(List<ContactModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }


    @NonNull
    @Override
    public ChatContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_message_item, parent, false);
        return new ChatContactAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull ChatContactAdapter.ViewHolder holder, int position) {
        ContactModel item = dataList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.messageTextView.setText(item.getMessage());
        holder.timestampText.setText(item.getTime());
        holder.whoTextView.setText(item.getWho());
        holder.counterIcon.setText( String.valueOf(item.getCounter()));
        holder.counterIcon.setVisibility(item.getVisibility());
        Glide.with(holder.itemView.getContext())
                .load(item.getImageURL())
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.contactImage);
        holder.contactLinearLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.openMessage(position, item.getId());
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }








    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView contactImage;
        TextView messageTextView;
        TextView timestampText;
        TextView whoTextView;
        Button counterIcon;

        LinearLayout contactLinearLayout;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            contactImage = itemView.findViewById(R.id.contactImage);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampText = itemView.findViewById(R.id.timestampText);
            whoTextView = itemView.findViewById(R.id.whoTextView);
            contactLinearLayout = itemView.findViewById(R.id.contactLinearLayout);
            counterIcon =  itemView.findViewById(R.id.counterIcon);

        }
    }

}
