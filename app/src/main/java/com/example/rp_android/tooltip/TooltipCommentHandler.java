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

/**
 * Tooltip
 */
public class TooltipCommentHandler {
    public static void showOfferComment(Context context, View anchorView, String comment) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tooltipView = inflater.inflate(R.layout.tooltip_offer_comment, null);
        TextView tooltipCommentText = tooltipView.findViewById(R.id.toolTipCommentText);
        tooltipCommentText.setText(comment);


        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(tooltipView, width, height, false);

        popupWindow.showAsDropDown(anchorView, 0, -anchorView.getHeight() - 20, Gravity.TOP);

        new Handler().postDelayed(popupWindow::dismiss, 2000);
    }
}
