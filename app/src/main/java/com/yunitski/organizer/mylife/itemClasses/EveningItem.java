package com.yunitski.organizer.mylife.itemClasses;

public class EveningItem {

    private String eveningItemId;

    private String eveningItemText;

    private String eveningItemTime;

    private String eveningItemStatus;

    private String eveningItemDate;

    public EveningItem(String eveningItemId, String eveningItemText, String eveningItemTime, String eveningItemStatus, String eveningItemDate) {
        this.eveningItemId = eveningItemId;
        this.eveningItemText = eveningItemText;
        this.eveningItemTime = eveningItemTime;
        this.eveningItemStatus = eveningItemStatus;
        this.eveningItemDate = eveningItemDate;
    }

    public String getEveningItemId() {
        return eveningItemId;
    }

    public void setEveningItemId(String eveningItemId) {
        this.eveningItemId = eveningItemId;
    }

    public String getEveningItemText() {
        return eveningItemText;
    }

    public void setEveningItemText(String eveningItemText) {
        this.eveningItemText = eveningItemText;
    }

    public String getEveningItemTime() {
        return eveningItemTime;
    }

    public void setEveningItemTime(String eveningItemTime) {
        this.eveningItemTime = eveningItemTime;
    }

    public String getEveningItemStatus() {
        return eveningItemStatus;
    }

    public void setEveningItemStatus(String eveningItemStatus) {
        this.eveningItemStatus = eveningItemStatus;
    }

    public String getEveningItemDate() {
        return eveningItemDate;
    }

    public void setEveningItemDate(String eveningItemDate) {
        this.eveningItemDate = eveningItemDate;
    }
}
