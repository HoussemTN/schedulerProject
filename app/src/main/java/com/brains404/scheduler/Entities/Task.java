package com.brains404.scheduler.Entities;

public class Task {
    int idSession ;
    String title ;
    String description ;
    int toWeek ;
    int status ;

    public Task() {
    }

    public Task(int idSession, String title, String description, int toWeek, int status) {
        this.idSession = idSession;
        this.title = title;
        this.description = description;
        this.toWeek = toWeek;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getToWeek() {
        return toWeek;
    }

    public void setToWeek(int toWeek) {
        this.toWeek = toWeek;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
