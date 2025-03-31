package com.example.rp_android.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rp_android.R;
import com.example.rp_android.models.SpinnerModel;

import java.util.List;

/**
 * Soubor nacita seznam objektu
 */
public class SpinnerAdapter extends ArrayAdapter<SpinnerModel> {

    public SpinnerAdapter(Context context, List<SpinnerModel> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.spinnerIcon);
        TextView text = convertView.findViewById(R.id.spinnerText);

        SpinnerModel item = getItem(position);
        if (item != null) {
            icon.setImageDrawable(item.getIcon());
            text.setText(item.getName());
        }

        return convertView;
    }
}