package com.example.rp_android.adapters;


import android.annotation.SuppressLint;
import android.content.Context;

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
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.models.ChatMessageModel;

import java.util.List;
import java.util.Objects;

/**
 * Soubor nacita zpravy v chatovacich mistnostech
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {



    private List<ChatMessageModel> dataList;
    private ChatMessageAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;




    public interface OnItemClickListener {
        void deleteChat(int position, int id);


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public ChatMessageAdapter(List<ChatMessageModel> dataList, OnItemClickListener listener, FragmentManager fragmentManager, Context context) {
        this.dataList = dataList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }
    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(dataList.get(position).getType(), 1)) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_send_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_receive_message, parent, false);
        }
        return new ChatMessageAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ViewHolder holder, int position) {
        ChatMessageModel item = dataList.get(position);
            holder.messageText.setText(item.getIcon());
        Glide.with(holder.itemView.getContext())
                .load(ConnectionFile.returnURL()+ "storage/attachments/" +item.getAttechment())
                .into(holder.attechmentImage);
            holder.attechmentImage.setVisibility(item.getVisibility());


    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }


    public void changeData(int position, ChatMessageModel model) {
        if (position >= 0 && position < dataList.size()) {
            if(dataList.get(position).getId() != model.getId()){
                dataList.remove(position);
            }else{
                dataList.set(position, model);
            }
            notifyItemChanged(position);
        }else if(position >= dataList.size()){
            dataList.add(model);
            notifyItemChanged(position);
        }
    }
    public void reloadMessage(int position) { // nacteni zpravy znovu

        notifyItemChanged(position);


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView favoriteImage;
        ImageView attechmentImage;
        LinearLayout mainLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            attechmentImage = itemView.findViewById(R.id.attechmentImage);
        }
    }
}
