package com.example.rp_android.ui.chat_menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatMenuViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ChatMenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}