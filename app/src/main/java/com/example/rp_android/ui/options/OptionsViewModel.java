package com.example.rp_android.ui.options;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/*public class OptionsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
}*/
public class OptionsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OptionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}