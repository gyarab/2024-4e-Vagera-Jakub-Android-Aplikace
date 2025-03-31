package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

public class OptionsModel {


    private Integer dayList;
    private String monthList;

    private String weekList;
    private String monthNameList;
    private String fromList;
    private String toList;
    private Drawable vacationStatus;
    private Drawable workStatus;
    private Drawable holidayStatus;




    public OptionsModel(Integer dayList, String monthList, String weekList, String monthNameList, String fromList, String toList, Drawable vacationStatus, Drawable workStatus, Drawable holidayStatus) {
        this.dayList = dayList;
        this.monthList = monthList;
        this.weekList = weekList;
        this.monthNameList = monthNameList;
        this.fromList= fromList;
        this.toList= toList;
        this.vacationStatus = vacationStatus;
        this.workStatus = workStatus;
        this.holidayStatus = holidayStatus;
    }

    public int getDay() {
        return dayList;
    }

    public String getMonth() {
        return monthList;
    }
    public String getWeek() {
        return weekList;
    }

    public String getMonthName() {
        return monthNameList;
    }
    public void setMonth(String time) {
        this.monthList = time;
    }
    public void setFromList(String time) {
        this.fromList= time;
    }
    public void setToList(String time) {
        this.toList = time;
    }
    public String getFromList() {
        return fromList;
    }
    public String getToList() {
        return toList;
    }

    public Drawable getHolidayStatus() {
        return holidayStatus;
    }

    public Drawable getVacationStatus() {
        return vacationStatus;
    }

    public Drawable getWorkStatus() {
        return workStatus;
    }

    public void setHolidayStatus(Drawable holidayStatus) {
        this.holidayStatus = holidayStatus;
    }

    public void setVacationStatus(Drawable vacationStatus) {
        this.vacationStatus = vacationStatus;
    }

    public void setWorkStatus(Drawable workStatus) {
        this.workStatus = workStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same memory reference
        if (obj == null || getClass() != obj.getClass()) return false;

        OptionsModel that = (OptionsModel) obj;
        return dayList == that.dayList; // Compare by unique identifier
    }
    /*public boolean isHoliday() {
        return this.holidayStatus == 1;
    }*/
}
