package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.List;

/**
 *  Soubor slouzi pro nacteni oblibenych uzivatel v chatu pres RecycleView
 */
public class ChatFavoriteAdapter extends RecyclerView.Adapter<ChatFavoriteAdapter.ViewHolder>{



    private List<ChatFavoriteModel> dataList;
    private ChatFavoriteAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;



    public interface OnItemClickListener {
        void openChat(int position, int id);


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public ChatFavoriteAdapter(List<ChatFavoriteModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context) {
        this.dataList =dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }


    @NonNull
    @Override
    public ChatFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_favorite_item, parent, false);
        return new ChatFavoriteAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull ChatFavoriteAdapter.ViewHolder holder, int position) {
        ChatFavoriteModel item = dataList.get(position);
        holder.nameTextView.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageURL())
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.favoriteImage);
        holder.favoriteLinearLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.openChat(position, item.getUserId());
            }

        });


    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }









    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView favoriteImage;
        LinearLayout favoriteLinearLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            favoriteLinearLayout = itemView.findViewById(R.id.favoriteLinearLayout);
        }
    }

}