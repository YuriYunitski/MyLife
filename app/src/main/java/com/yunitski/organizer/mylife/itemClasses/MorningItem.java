package com.yunitski.organizer.mylife.itemClasses;

public class MorningItem implements Comparable<MorningItem>{

    private String morningItemId;

    private String morningItemText;

    private String morningItemTime;

    private String morningItemStatus;

    private String morningItemDate;

    private int morningItemTimeInMinutes;

    public MorningItem() {
    }


    public MorningItem(String morningItemId, String morningItemText, String morningItemTime, String morningItemStatus, String morningItemDate, int morningItemTimeInMinutes) {
        this.morningItemId = morningItemId;
        this.morningItemText = morningItemText;
        this.morningItemTime = morningItemTime;
        this.morningItemStatus = morningItemStatus;
        this.morningItemDate = morningItemDate;
        this.morningItemTimeInMinutes = morningItemTimeInMinutes;
    }

    public String getMorningItemText() {
        return morningItemText;
    }

    public void setMorningItemText(String morningItemText) {
        this.morningItemText = morningItemText;
    }

    public String getMorningItemTime() {
        return morningItemTime;
    }

    public void setMorningItemTime(String morningItemTime) {
        this.morningItemTime = morningItemTime;
    }

    public String getMorningItemStatus() {
        return morningItemStatus;
    }

    public void setMorningItemStatus(String morningItemStatus) {
        this.morningItemStatus = morningItemStatus;
    }

    public String getMorningItemId() {
        return morningItemId;
    }

    public void setMorningItemId(String morningItemId) {
        this.morningItemId = morningItemId;
    }

    public String getMorningItemDate() {
        return morningItemDate;
    }

    public void setMorningItemDate(String morningItemDate) {
        this.morningItemDate = morningItemDate;
    }

    public int getMorningItemTimeInMinutes() {
        return morningItemTimeInMinutes;
    }

    public void setMorningItemTimeInMinutes(int morningItemTimeInMinutes) {
        this.morningItemTimeInMinutes = morningItemTimeInMinutes;
    }

    @Override
    public int compareTo(MorningItem o) {
        int compareTime = o.getMorningItemTimeInMinutes();
        return this.morningItemTimeInMinutes-compareTime;
    }
}
