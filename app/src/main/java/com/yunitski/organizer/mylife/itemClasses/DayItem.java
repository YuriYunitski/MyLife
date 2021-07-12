package com.yunitski.organizer.mylife.itemClasses;

public class DayItem {

    private String dayItemId;

    private String dayItemText;

    private String dayItemTime;

    private String dayItemStatus;

    private String dayItemDate;

    public DayItem(String dayItemId, String dayItemText, String dayItemTime, String dayItemStatus, String dayItemDate) {
        this.dayItemId = dayItemId;
        this.dayItemText = dayItemText;
        this.dayItemTime = dayItemTime;
        this.dayItemStatus = dayItemStatus;
        this.dayItemDate = dayItemDate;
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
}
