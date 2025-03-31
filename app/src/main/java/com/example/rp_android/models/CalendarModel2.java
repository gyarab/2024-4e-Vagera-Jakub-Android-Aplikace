package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Druhy kalendarni model pro nacteni nabitky volnych smen
 */
public class CalendarModel2 {


    private Integer dayList;
    private String monthList;
    private String weekList;


    private Drawable vacationStatus;
    private Drawable workStatus;
    private Drawable holidayStatus;

    private List<OffersModel> offersModel;



    public CalendarModel2(Integer dayList, String monthList, String weekList,  Drawable vacationStatus, Drawable workStatus, Drawable holidayStatus, List<OffersModel> offersModel) {
        this.dayList = dayList;
        this.monthList = monthList;
        this.weekList = weekList;
        this.vacationStatus = vacationStatus;
        this.workStatus = workStatus;
        this.holidayStatus = holidayStatus;
        this.offersModel = offersModel;
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

    public void setMonth(String time) {
        this.monthList = time;
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

    public List<OffersModel> getOffersModel() {
        return offersModel;
    }

    public void setOffersModel(List<OffersModel> offersModel) {
        this.offersModel = offersModel;
    }
}