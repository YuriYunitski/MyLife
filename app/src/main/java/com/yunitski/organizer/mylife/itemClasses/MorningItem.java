package com.yunitski.organizer.mylife.itemClasses;

public class MorningItem {

    private String morningItemText;

    private String morningItemTime;

    public MorningItem() {
    }

    public MorningItem(String morningItemText, String morningItemTime) {
        this.morningItemText = morningItemText;
        this.morningItemTime = morningItemTime;
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
}
