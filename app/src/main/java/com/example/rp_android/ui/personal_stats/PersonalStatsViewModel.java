package com.example.rp_android.ui.personal_stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class PersonalStatsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PersonalStatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}