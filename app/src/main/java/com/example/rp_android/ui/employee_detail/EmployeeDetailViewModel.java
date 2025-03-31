package com.example.rp_android.ui.employee_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class EmployeeDetailViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EmployeeDetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}