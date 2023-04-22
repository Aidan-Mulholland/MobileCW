package com.example.coursework;

public class AlarmModel {
    private int id;
    private String timeText;
    private long timeInt;
    private boolean active;

    public AlarmModel(int id, String timeText, long timeInt, boolean active) {
        this.id = id;
        this.timeText = timeText;
        this.timeInt = timeInt;
        this.active = active;
    }

    @Override
    public String toString() {
        return "AlarmModel{" +
                "id=" + id +
                ", timeText='" + timeText + '\'' +
                ", timeInt=" + timeInt +
                ", active=" + active +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeText() {
        return this.timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public long getTimeInt() {
        return this.timeInt;
    }

    public void setTimeInt(int timeInt) {
        this.timeInt = timeInt;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

