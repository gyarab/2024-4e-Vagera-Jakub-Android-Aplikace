package com.example.rp_android.ui.my_shifts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyShiftsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyShiftsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is shifts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}