package com.example.rp_android.ui.chat_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatDetailViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ChatDetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}