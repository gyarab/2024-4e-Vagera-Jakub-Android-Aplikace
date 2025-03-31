package com.example.rp_android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.R;
import com.example.rp_android.connection.ConnectionFile;
import com.example.rp_android.models.PhotosModel;

import java.util.List;

/**
 * Soubor nacita obrazky ziskane z API
 */
public class ChatPhotosAdapter extends RecyclerView.Adapter<ChatPhotosAdapter.ViewHolder>{





    private List<PhotosModel> dataList;
    private ChatPhotosAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;




    public interface OnItemClickListener {
        void clickImage(int position, int id);


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public ChatPhotosAdapter(List<PhotosModel> dataList, FragmentManager fragmentManager, Context context) {
        this.dataList =dataList;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }


    @NonNull
    @Override
    public ChatPhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        return new ChatPhotosAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull ChatPhotosAdapter.ViewHolder holder, int position) {
        PhotosModel item = dataList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(ConnectionFile.returnURL()+ "storage/attachments/" +item.getURL())
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.sharedImageView);

    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sharedImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sharedImageView = itemView.findViewById(R.id.sharedImageView);

        }
    }

}
