package com.example.coursework;

public class JournalModel {
    private int id;
    private String date, journal;

    public JournalModel(int id, String date, String journal) {
        this.id = id;
        this.date = date;
        this.journal = journal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    @Override
    public String toString() {
        return "JournalModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", journal='" + journal + '\'' +
                '}';
    }
}
