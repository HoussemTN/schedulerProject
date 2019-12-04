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



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }



    public String getStartTime() {
        return startTime;
    }


    public String getEndTime() {
        return endTime;
    }

    public int getIdDay() {
        return idDay;
    }


}
