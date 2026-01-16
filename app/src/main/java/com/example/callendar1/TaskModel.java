package com.example.callendar1;



public class TaskModel {
    private int id;
    private String opis;
    private String kiedykoniec;
    private String status;

    public TaskModel(int id, String opis, String kiedykoniec, String status) {
        this.id = id;
        this.opis = opis;
        this.kiedykoniec = kiedykoniec;
        this.status = status;
    }

    public TaskModel() {
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", opis='" + opis + '\'' +
                ", kiedykoniec=" + kiedykoniec +
                ", status='" + status + '\'' +
                '}';
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKiedykoniec() {
        return kiedykoniec;
    }

    public void setKiedykoniec(String kiedykoniec) {
        this.kiedykoniec = kiedykoniec;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
