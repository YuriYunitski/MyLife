package com.yunitski.organizer.mylife.itemClasses;

public class DayItem implements Comparable<DayItem>{

    private String dayItemId;

    private String dayItemText;

    private String dayItemTime;

    private String dayItemStatus;

    private String dayItemDate;

    private int dayItemTimeInMinutes;

    public DayItem(String dayItemId, String dayItemText, String dayItemTime, String dayItemStatus, String dayItemDate, int dayItemTimeInMinutes) {
        this.dayItemId = dayItemId;
        this.dayItemText = dayItemText;
        this.dayItemTime = dayItemTime;
        this.dayItemStatus = dayItemStatus;
        this.dayItemDate = dayItemDate;
        this.dayItemTimeInMinutes = dayItemTimeInMinutes;
    }

    public String getDayItemId() {
        return dayItemId;
    }

    public void setDayItemId(String dayItemId) {
        this.dayItemId = dayItemId;
    }

    public String getDayItemText() {
        return dayItemText;
    }

    public void setDayItemText(String dayItemText) {
        this.dayItemText = dayItemText;
    }

    public String getDayItemTime() {
        return dayItemTime;
    }

    public void setDayItemTime(String dayItemTime) {
        this.dayItemTime = dayItemTime;
    }

    public String getDayItemStatus() {
        return dayItemStatus;
    }

    public void setDayItemStatus(String dayItemStatus) {
        this.dayItemStatus = dayItemStatus;
    }

    public String getDayItemDate() {
        return dayItemDate;
    }

    public void setDayItemDate(String dayItemDate) {
        this.dayItemDate = dayItemDate;
    }

    public int getDayItemTimeInMinutes() {
        return dayItemTimeInMinutes;
    }

    public void setDayItemTimeInMinutes(int dayItemTimeInMinutes) {
        this.dayItemTimeInMinutes = dayItemTimeInMinutes;
    }

    @Override
    public int compareTo(DayItem o) {
        int compareTime = o.getDayItemTimeInMinutes();
        return this.dayItemTimeInMinutes-compareTime;
    }
}
