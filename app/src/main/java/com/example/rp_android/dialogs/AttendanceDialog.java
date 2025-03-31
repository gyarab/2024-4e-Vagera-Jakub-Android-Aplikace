package com.example.rp_android.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Vyskakovaci okno - v soucasnosti ho program nevyuziva
 */
public class AttendanceDialog extends DialogFragment {

    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private DialogClickListener listener;

    public interface DialogClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }

    public AttendanceDialog() {
        // Empty constructor required
    }

    public static AttendanceDialog newInstance(
            String title,
            String message,
            String positiveButtonText,
            String negativeButtonText) {

        AttendanceDialog fragment = new AttendanceDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        args.putString("negativeButtonText", negativeButtonText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            message = getArguments().getString("message");
            positiveButtonText = getArguments().getString("positiveButtonText");
            negativeButtonText = getArguments().getString("negativeButtonText");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(
                requireContext(),
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog
        );

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    if (listener != null) {
                        listener.onPositiveClick();
                    }
                })
                .setNegativeButton(negativeButtonText, (dialog, which) -> {
                    if (listener != null) {
                        listener.onNegativeClick();
                    }
                });

        return builder.create();
    }
}
