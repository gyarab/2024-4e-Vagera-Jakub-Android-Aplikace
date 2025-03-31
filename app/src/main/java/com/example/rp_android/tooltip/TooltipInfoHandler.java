package com.example.rp_android.tooltip;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.rp_android.R;

public class TooltipInfoHandler {
    public static void showOfferInfo(Context context, View anchorView, String user, String time) {
        // Inflate tooltip layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tooltipView = inflater.inflate(R.layout.tooltip_offer_info, null);
        TextView tooltipUserText = tooltipView.findViewById(R.id.toolTipUserText);
        tooltipUserText.setText(user);
        TextView tooltipTimeText = tooltipView.findViewById(R.id.toolTipTimeText);
        tooltipTimeText.setText(time);

        // Create PopupWindow
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(tooltipView, width, height, false);

        // Show tooltip near the anchor view
        popupWindow.showAsDropDown(anchorView, 0, -anchorView.getHeight() - 20, Gravity.TOP);

        // Auto-dismiss after 2 seconds
        new Handler().postDelayed(popupWindow::dismiss, 2000);
    }

}