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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rp_android.R;
import com.example.rp_android.models.BoardModel;

import java.util.List;

/**
 * Soubor nacítá informační tabule v RecycleView
 */
public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>{




    private List<BoardModel> dataList;
    private BoardAdapter.OnItemClickListener listener;
    private Context context;
    private int parent;
    private FragmentManager fragmentManager;




    public interface OnItemClickListener {


        default FragmentManager getParentFragmentManager() {
            return null;
        }
    }

    public BoardAdapter(List<BoardModel> dataList, FragmentManager fragmentManager, Context context) {
        this.dataList =dataList;
        this.fragmentManager = fragmentManager;
        this.context = context;


    }


    @NonNull
    @Override
    public BoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.information_layout, parent, false);
        return new BoardAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.ViewHolder holder, int position) {
        BoardModel item = dataList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.captionTextView.setText(item.getCaption());
        holder.contentTextView.setText(item.getContent());
        holder.colorImageView.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.creationTextView.setText(item.getCreaton());

        Glide.with(holder.itemView.getContext())
                .load(item.getProfileURL())
                .placeholder(R.drawable.border_profile)
                .error(R.drawable.border_profile)
                .into(holder.profileImage);
        holder.boardImageView.setVisibility(item.getVisibility());
            Glide.with(holder.itemView.getContext())
                    .load(item.getBoardURL())
                    .placeholder(R.drawable.empty_fill)
                    .error(R.drawable.empty_fill)

                    .into(holder.boardImageView);


    }

    @Override
    public int getItemCount() {

        return dataList == null ? 0 : dataList.size();

    }


    public void setImgae(int position) {


        notifyItemChanged(position);  // Refresh konkretni tabule


    }







    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView colorImageView;
        ImageView boardImageView;
        TextView captionTextView;
        TextView contentTextView;
        ImageView profileImage;

        TextView creationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            colorImageView = itemView.findViewById(R.id.colorImageView);
            boardImageView = itemView.findViewById(R.id.boardImageView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            profileImage = itemView.findViewById(R.id.profileImage);
            creationTextView = itemView.findViewById(R.id.creationTextView);
        }
    }

}
