package com.example.rp_android.models;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Prvni kalendarni model pro nacteni cele organizace
 */
public class CalendarModel {


    private Integer dayList;
    private String monthList;
    private String weekList;


    private Drawable vacationStatus;
    private Drawable workStatus;
    private Drawable holidayStatus;

    private List<CalendarOrganizationModel> calendarOrganizationModel;

    private List<OffersModel> offersModel;



    public CalendarModel(Integer dayList, String monthList, String weekList,  Drawable vacationStatus, Drawable workStatus, Drawable holidayStatus, List<CalendarOrganizationModel> calendarOrganizationModel) {
        this.dayList = dayList;
        this.monthList = monthList;
        this.weekList = weekList;
        this.vacationStatus = vacationStatus;
        this.workStatus = workStatus;
        this.holidayStatus = holidayStatus;
        this.calendarOrganizationModel = calendarOrganizationModel;
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

    public List<CalendarOrganizationModel> getCalendarOrganizationModel() {
        return calendarOrganizationModel;
    }

    public void setCalendarOrganizationModel(List<CalendarOrganizationModel> calendarOrganizationModel) {
        this.calendarOrganizationModel = calendarOrganizationModel;
    }
}
