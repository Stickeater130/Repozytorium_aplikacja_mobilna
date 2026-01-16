package com.example.callendar1;



public class WakeUpModel {
    private int id;
    private String godzina;
    private String kiedywakeup;

    public WakeUpModel(int id, String godzina, String kiedywakeup) {
        this.id = id;
        this.godzina = godzina;
        this.kiedywakeup = kiedywakeup;
    }

    public WakeUpModel() {
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", godzina='" + godzina + '\'' +
                ", kiedywakeup=" + kiedywakeup + '}';
    }

    public String getGodzina() {
        return godzina;
    }

    public void setGodzina(String godzina) {
        this.godzina = godzina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getkiedywakeup() {
        return kiedywakeup;
    }

    public void setKiedywakeup(String kiedywakeup) {
        this.kiedywakeup = kiedywakeup;
    }
}
