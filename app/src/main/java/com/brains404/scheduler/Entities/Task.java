package com.brains404.scheduler.Entities;

public class Task {
    private int idTask ;
    private String title ;
    private String description ;
    private String startTime;
    private int status ;
    private int idDay ;



    public Task(int idTask, String title, String description, String startTime, int status,int idDay) {
        this.idTask = idTask;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.status = status;
        this.idDay = idDay;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idSession) {
        this.idTask = idTask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getIdDay() {
        return idDay;
    }

    public void setIdDay(int idDay) {
        this.idDay = idDay;
    }
}
