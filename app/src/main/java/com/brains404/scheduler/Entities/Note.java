package com.brains404.scheduler.Entities;

import java.util.Date;

public class Note {
    private int idNote;
    private String title;
    private String content;
    private int idLabel;
    Date dateCreation;


    public Note(int idNote) {
        this.idNote = idNote;
    }

    public Note(int idNote, String title, String content, Date dateCreation, int idLabel) {
        this.idNote = idNote;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.idLabel = idLabel;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIdLabel() {
        return idLabel;
    }

    public void setIdLabel(int idLabel) {
        this.idLabel = idLabel;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    @Override
    public String toString() {
        return "Note{" +
                "idNote=" + idNote +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", dateCreation=" + dateCreation +
                ", idLabel=" + idLabel +
                '}';
    }
}
