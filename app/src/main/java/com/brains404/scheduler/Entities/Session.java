package com.brains404.scheduler.Entities;

public class Session {
    private int idSession ;
    private String title ;
    private String place ;
    private String startTime;
    private String endTime;
    private String day;


    public Session(int idSession) {
        this.idSession = idSession;
    }

    public Session(int idSession, String name, String place, String startTime, String endTime, String day) {
        this.idSession = idSession;
        this.title = name;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public int getIdSession() {
        return idSession;
    }

    public void setIdSession(int idSession) {
        this.idSession = idSession;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Session{" +
                "idSession=" + idSession +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
