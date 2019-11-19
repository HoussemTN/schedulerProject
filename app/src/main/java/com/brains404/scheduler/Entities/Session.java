package com.brains404.scheduler.Entities;

public class Session {
    private int idSession ;
    private String title ;
    private String place ;
    private String startTime;
    private String endTime;
    private int idDay ;

    public Session(int idSession, String title, String place, String startTime, String endTime, int idDay) {
        this.idSession = idSession;
        this.title = title;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.idDay = idDay;
    }

    public Session(int idSession) {
        this.idSession = idSession;
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

    public int getIdDay() {
        return idDay;
    }

    public void setIdDay(int idDay) {
        this.idDay = idDay;
    }

    @Override
    public String toString() {
        return "Session{" +
                "idSession=" + idSession +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", idDay=" + idDay +
                '}';
    }
}
